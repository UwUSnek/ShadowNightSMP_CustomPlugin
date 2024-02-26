package org.shadownight.plugin.shadownight.attackOverride;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.ClaimUtils;
import org.shadownight.plugin.shadownight.utils.spigot.Scheduler;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.*;
import java.util.logging.Level;




/**
 * This class is used to manage player attacks because Bukkit's system is atrocious.
 * Allows for custom attacks and overrides vanilla attacks.
 * Provides actually useful data for the death message manager.
 */
public final class AttackOverride extends UtilityClass implements Rnd {
    public static class AttackData {
        public final LivingEntity damager;
        public final ItemStack usedItem;
        public final Long time;

        // This is used to determine if an event has to actually damage the entity and trigger vanilla mob behaviours.
        // It's set to true after the mirror event is fired (event got mirrored, no need to do that again).
        public boolean mirrored = false;

        public AttackData(LivingEntity _damager, ItemStack _usedItem, Long _time) {
            damager = _damager;
            usedItem = _usedItem;
            time = _time;
        }
    }
    //TODO use this for more detailed death messages
    //TODO maybe save any damage in the queue
    public static final HashMap<UUID, CircularFifoQueue<AttackData>> attacks = new HashMap<>();
    private static final double gravityY = -0.0784d;


    /**
     * Applies enchantment effects to the target.
     * This includes any enchantment on the target's armor.
     * Protection is handled by Vanilla Minecraft.
     * Sharpness and variants are handled by the damage calculation system.
     * @param damager The attacking entity
     * @param target The attacked entity
     * @param item The item used to attack
     */
    private static void applyEnchantEffects(@NotNull final LivingEntity damager, @NotNull final LivingEntity target, @Nullable ItemStack item) {
        // Fire aspect
        if (item != null) {
            int fireAspectLv = item.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
            if (fireAspectLv > 0) Scheduler.delay(() -> target.setFireTicks(fireAspectLv * 80), 1L);
        }
    }



    private static void resetPotion(@NotNull final LivingEntity target, @NotNull final PotionEffectType type, final int duration, final int amplifier) {
        if(target.hasPotionEffect(type)) {
            PotionEffect effect = target.getPotionEffect(type);
            if(effect != null && effect.getDuration() <= duration && amplifier == effect.getAmplifier()) target.removePotionEffect(type);
        }
        target.addPotionEffect(new PotionEffect(type, duration, amplifier));
    }

    /**
     * Applies the Vanilla mob effects to the target.
     * @param damager The attacking entity
     * @param target The attacked entity
     * @param item The item used to attack
     */
    private static void applyMobEffects(@NotNull final LivingEntity damager, @NotNull final LivingEntity target, @Nullable ItemStack item) {
        Difficulty difficulty = target.getWorld().getDifficulty();
        double regionalDifficulty = utils.getRegionalDifficulty(difficulty, target.getLocation());
        switch (damager.getType()) {
            case HUSK: {
                if (item == null || item.getType() == Material.AIR) resetPotion(target, PotionEffectType.HUNGER, 7 * 20 * (int)Math.floor(regionalDifficulty), 0);
                // Intentional fallthrough
            }
            case ZOMBIE, ZOMBIE_VILLAGER: {
                if (damager.getFireTicks() > 0 && rnd.nextFloat() < 0.3 * regionalDifficulty) Scheduler.delay(() -> target.setFireTicks(20 * 2 * (int)Math.floor(regionalDifficulty)), 1L);
                break;
            }
            case CAVE_SPIDER: {
                /**/ if (difficulty == Difficulty.NORMAL) resetPotion(target, PotionEffectType.POISON,  7 * 20, 0);
                else if (difficulty == Difficulty.HARD)   resetPotion(target, PotionEffectType.POISON, 15 * 20, 0);
                break;
            }
            case BEE: {
                /**/ if (difficulty == Difficulty.NORMAL) resetPotion(target, PotionEffectType.POISON, 10 * 20, 0);
                else if (difficulty == Difficulty.HARD)   resetPotion(target, PotionEffectType.POISON, 18 * 20, 0);
                break;
            }
            case WITHER_SKELETON: {
                resetPotion(target, PotionEffectType.WITHER, 10 * 20, 0);
                break;
            }
        }
    }



    /**
     * Replaces a Vanilla EntityDamageByEntityEvent event with a custom attack.
     * @param e The event to replace
     * @param canCrit True if the attack can crit, false otherwise.
     *                Whether the attack will effectively be critical depends on the current status of the attacking entity
     */
    public static void customAttack(@NotNull final EntityDamageByEntityEvent e, final boolean canCrit) {
        // Use Vanilla creeper explosions
        if(e.getDamager().getType() == EntityType.CREEPER) return;

        // Skip mirror events
        CircularFifoQueue<AttackData> attackQueue = attacks.get(e.getEntity().getUniqueId());
        if(attackQueue != null) {
            AttackData lastAttack = attackQueue.get(attackQueue.size() == 1 ? 0 : 1);
            if(lastAttack.damager == e.getDamager() && !lastAttack.mirrored) {
                lastAttack.mirrored = true;
                return;
            }
        }

        // Cancel event and compute custom attack
        if (e.getDamager() instanceof LivingEntity damager && e.getEntity() instanceof LivingEntity target) {
            e.setCancelled(true);
            customAttack(damager, target, canCrit);
        }
    }




    /**
     * Registers a custom attack.
     * This method is also used to replace Vanilla EntityDamageByEntityEvent events.
     * @param damager The attacking entity
     * @param target The damaged entity
     * @param canCrit True if the attack can crit, false otherwise.
     *                Whether the attack will effectively be critical depends on the current status of the attacking entity
     */
    public static void customAttack(@NotNull final LivingEntity damager, @NotNull final LivingEntity target, final boolean canCrit) {
        // Ignore attacks on protected entities from players with no permissions
        if(
            damager instanceof Player player && !(target instanceof Player) &&          // If a player attacks a non-player &&
            ClaimUtils.isEntityProtected(target, player)                                // target is protected from attacker
        ) return;

        // Ignore attacks on other player's dogs
        if(
            target instanceof Wolf wolf && (wolf.getOwner() != null && wolf.getOwner() != damager) && // If target is a dog and is not owner by the damaged player &&
            wolf.getTarget() != damager                                  // dog isn't targeting the damager
        ) return;




        // Save the used item
        final EntityEquipment equipment = damager.getEquipment();
        final ItemStack item = equipment == null ? null : equipment.getItemInMainHand();

        // Save the attack data
        final UUID targetId = target.getUniqueId();
        attacks.putIfAbsent(targetId, new CircularFifoQueue<>(2)); //TODO remove when entity is killed or removed
        attacks.get(targetId).add(new AttackData(
            damager,
            item,
            System.currentTimeMillis()
        ));




        // Calculate pre-hit velocity
        final Vector velocity = target.getVelocity();                       // Set starting velocity
        velocity.add(CustomKnockback.getKnockback(item, damager, target));  // Add attack knockback
        if(!target.isOnGround()) velocity.setY(0);                          // If not on ground, set Y knockback to 0
        else velocity.setY(velocity.getY() - gravityY);                     // If on ground, account for gravity


        //TODO review and simplify custom scythe attacks
        final double damage = CustomDamage.getDamage(item, canCrit, damager, target);
        target.damage(damage, damager);                 // Damage the target
        applyEnchantEffects(damager, target, item);     // Apply enchantment effects
        applyMobEffects(damager, target, item);         // Apply vanilla mob effects


        // Apply new velocity (and override .damage's Vanilla knockback)
        target.setVelocity(velocity);
    }


    public static void onDeath(@NotNull final EntityDeathEvent e) {
        attacks.remove(e.getEntity().getUniqueId());
    }
}
