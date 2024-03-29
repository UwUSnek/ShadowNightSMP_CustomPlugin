package org.uwu_snek.shadownight.attackOverride.attacks;


import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.attackOverride.AttackOverride;
import org.uwu_snek.shadownight.attackOverride.CustomDamage;
import org.uwu_snek.shadownight.attackOverride.CustomKnockback;
import org.uwu_snek.shadownight.utils.ResetPotionEffect;
import org.uwu_snek.shadownight.utils.Rnd;
import org.uwu_snek.shadownight.utils.math.Func;
import org.uwu_snek.shadownight.utils.spigot.ClaimUtils;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;
import org.uwu_snek.shadownight.utils.utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;




public abstract class ATK implements Rnd {
    public abstract void execute(final @NotNull LivingEntity damager, @Nullable final LivingEntity directTarget, final @NotNull Location origin, @Nullable final ItemStack item);




    /**
     * Applies enchantment effects to the target.
     * This includes any enchantment on the target's armor.
     * Protection is handled by Vanilla Minecraft.
     * Sharpness and variants are handled by the damage calculation system.
     * @param damager The attacking entity
     * @param target The attacked entity
     * @param item The item used to attack
     */
    private static void applyEnchantEffects(final @NotNull LivingEntity damager, final @NotNull LivingEntity target, @Nullable ItemStack item) {
        // Fire aspect
        if (item != null) {
            int fireAspectLv = item.getEnchantmentLevel(Enchantment.FIRE_ASPECT);
            if (fireAspectLv > 0) target.setFireTicks(fireAspectLv * 80);
        }

        // Target's armor enchants
        EntityEquipment equipment = target.getEquipment();
        if(equipment != null) {
            ArrayList<ItemStack> thornsArmorPieces = new ArrayList<>();
            int totalThornDamage = 0;
            for(ItemStack a : equipment.getArmorContents()) if(a != null) {
                for(Map.Entry<Enchantment, Integer> e : a.getEnchantments().entrySet()) {
                    Enchantment key = e.getKey();
                    if(key == Enchantment.THORNS) {
                        thornsArmorPieces.add(a);
                        if(rnd.nextFloat() < e.getValue() * 0.15d) totalThornDamage += rnd.nextInt(4) + 1;
                    }
                }
            }
            if(totalThornDamage > 0) {
                executeBasicAttack(target, damager, target.getLocation(), false, null, 1, false, (double)Func.clampMax(totalThornDamage, 4));
                ItemUtils.damageItem(target, thornsArmorPieces.get(rnd.nextInt(thornsArmorPieces.size())), 2);
            }
        }
    }

    /**
     * Applies the Vanilla mob effects to the target.
     * @param damager The attacking entity
     * @param target The attacked entity
     * @param item The item used to attack
     */
    private static void applyMobEffects(final @NotNull LivingEntity damager, final @NotNull LivingEntity target, @Nullable ItemStack item) {
        Difficulty difficulty = target.getWorld().getDifficulty();
        double regionalDifficulty = utils.getRegionalDifficulty(difficulty, target.getLocation());
        switch (damager.getType()) {
            case HUSK: {
                if (item == null || item.getType() == Material.AIR) ResetPotionEffect.reset(target, PotionEffectType.HUNGER, 7 * 20 * (int)Math.floor(regionalDifficulty), 0);
                // Intentional fallthrough
            }
            case ZOMBIE, ZOMBIE_VILLAGER: {
                if (damager.getFireTicks() > 0 && rnd.nextFloat() < 0.3 * regionalDifficulty) Scheduler.delay(() -> target.setFireTicks(20 * 2 * (int)Math.floor(regionalDifficulty)), 1L);
                break;
            }
            case CAVE_SPIDER: {
                /**/ if (difficulty == Difficulty.NORMAL) ResetPotionEffect.reset(target, PotionEffectType.POISON,  7 * 20, 0);
                else if (difficulty == Difficulty.HARD)   ResetPotionEffect.reset(target, PotionEffectType.POISON, 15 * 20, 0);
                break;
            }
            case BEE: {
                /**/ if (difficulty == Difficulty.NORMAL) ResetPotionEffect.reset(target, PotionEffectType.POISON, 10 * 20, 0);
                else if (difficulty == Difficulty.HARD)   ResetPotionEffect.reset(target, PotionEffectType.POISON, 18 * 20, 0);
                break;
            }
            case WITHER_SKELETON: {
                ResetPotionEffect.reset(target, PotionEffectType.WITHER, 10 * 20, 0);
                break;
            }
        }
    }


    /**
     * Returns the attack charge of an entity.
     * @param damager The entity
     * @return The attack charge. 0 to 1, with 1 = fully charged
     */
    protected static double getEntityCharge(final @NotNull LivingEntity damager) {
        return (damager instanceof Player player) ? player.getAttackCooldown() : 1;
        //utils.log(Level.WARNING,"[" + damager.getType() + "] Using charge: " + charge);
    }




    /**
     * Executes a basic, single-target attack from an entity to another using the specified attack charge.
     * The damage is based on the attack charge and the item's attack damage.
     * Potion effects, Vanilla enchantments, Custom enchantments, Vanilla mob effects and attributes on both the attacker's weapon and the target's armor pieces are also accounted for.
     * @param damager The attacking entity
     * @param target The attacked entity
     * @param charge The attack charge
     * @param canCrit True if the attack can trigger a critical hit
     * @param damageOverride If not null, the damage calculation is skipped and this value is used instead
     */
    public static void executeBasicAttack(
        final @NotNull LivingEntity damager,
        final @NotNull LivingEntity target,
        final @NotNull Location origin,
        boolean useOriginDirection,
        final @Nullable ItemStack item,
        final double charge,
        final boolean canCrit,
        final Double damageOverride
    ) {
        // Stop damager from hitting itself
        if(damager == target) return;

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


        // Save the attack data
        final UUID targetId = target.getUniqueId();
        AttackOverride.attacks.putIfAbsent(targetId, new CircularFifoQueue<>(2)); //TODO remove when entity is killed or removed
        AttackOverride.attacks.get(targetId).add(new AttackOverride.AttackData(
            damager,
            item,
            System.currentTimeMillis()
        ));




        // Calculate pre-hit velocity
        final Vector velocity = target.getVelocity();                       // Set starting velocity
        velocity.add(CustomKnockback.getKnockback(damager, target, origin, useOriginDirection, item));  // Add attack knockback


        // Calculate damage
        final double damage = damageOverride == null ? CustomDamage.getDamage(item, canCrit, damager, target, charge) : damageOverride;
        applyMobEffects(damager, target, item);         // Apply vanilla mob effects
        applyEnchantEffects(damager, target, item);     // Apply enchantment effects
        target.damage(damage, damager);                 // Damage the target


        // Apply new velocity (and override .damage()'s Vanilla knockback)
        target.setVelocity(velocity);
    }


    /**
     * Simulates the Vanilla sweeping attack effect and sound in front of the location <pos> based on the pitch and yaw specified in it.
     * @param pos The target location
     */
    public static void simulateSweepingEffect(final @NotNull Location pos) {
        World world = pos.getWorld();
        if(world == null) throw new RuntimeException("Failed to simulate sweeping effect: World is null");
        pos.getWorld().playSound(pos, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);

        // Using spawnParticle(particle, location, count, offsetX, offsetY, offsetZ, extra (speed) (value 0 on sweep particle makes it of constant size)
        pos.getWorld().spawnParticle(Particle.SWEEP_ATTACK, pos.clone().add(pos.getDirection().multiply(new Vector(2, 0, 2))).add(new Vector(0, 1, 0)), 1, 0, 0, 0, 0);
    }
}
