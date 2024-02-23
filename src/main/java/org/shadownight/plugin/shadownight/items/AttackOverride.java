package org.shadownight.plugin.shadownight.items;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;




/**
 * This class is used to manage player attacks because Bukkit's system is atrocious.
 * Allows for custom attacks and overrides vanilla attacks.
 * Provides actually useful data for the death message manager.
 */
public final class AttackOverride extends UtilityClass {
    private static final Map<Material, Double> defaultDamage = Map.ofEntries(
        new AbstractMap.SimpleEntry<>(Material.WOODEN_SWORD,      4d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_SWORD,      4d),
        new AbstractMap.SimpleEntry<>(Material.STONE_SWORD,       5d),
        new AbstractMap.SimpleEntry<>(Material.IRON_SWORD,        6d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_SWORD,     7d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_SWORD,   8d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_AXE,        7d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_AXE,        7d),
        new AbstractMap.SimpleEntry<>(Material.STONE_AXE,         9d),
        new AbstractMap.SimpleEntry<>(Material.IRON_AXE,          9d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_AXE,       9d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_AXE,     10d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_PICKAXE,    2d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_PICKAXE,    2d),
        new AbstractMap.SimpleEntry<>(Material.STONE_PICKAXE,     3d),
        new AbstractMap.SimpleEntry<>(Material.IRON_PICKAXE,      4d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_PICKAXE,   5d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_PICKAXE, 6d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_SHOVEL,     2.5d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_SHOVEL,     2.5d),
        new AbstractMap.SimpleEntry<>(Material.STONE_SHOVEL,      3.5d),
        new AbstractMap.SimpleEntry<>(Material.IRON_SHOVEL,       4.5d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_SHOVEL,    5.5d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_SHOVEL,  6.5d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_HOE,        1d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_HOE,        1d),
        new AbstractMap.SimpleEntry<>(Material.STONE_HOE,         1d),
        new AbstractMap.SimpleEntry<>(Material.IRON_HOE,          1d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_HOE,       1d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_HOE,     1d),

        new AbstractMap.SimpleEntry<>(Material.TRIDENT,           9d)
    );


    public static class AttackData {
        public LivingEntity damager;
        public ItemStack usedItem;
        public Long time;

        public AttackData(LivingEntity _damager, ItemStack _usedItem, Long _time) {
            damager = _damager;
            usedItem = _usedItem;
            time = _time;
        }
    }


    public static HashMap<UUID, CircularFifoQueue<AttackData>> attacks = new HashMap<>();
    public static HashMap<UUID, Boolean> knockbackSprintBuff = new HashMap<>();






    public static void resetKnockbackSprintBuff(@NotNull final PlayerToggleSprintEvent event){
        if(event.isSprinting()) knockbackSprintBuff.put(event.getPlayer().getUniqueId(), true);
    }

    private static double getBaseKnockback(@Nullable final ItemStack item) {
        double base = 1.552f;
        if(item != null && item.getType() != Material.AIR) {
            final Long itemId = ItemUtils.getCustomItemId(item);
            if(itemId != null) base *= ItemManager.getValueFromId(itemId).getHitKnockback();
        }
        return base;
    }

    private static @NotNull Vector getKnockback(@Nullable final ItemStack item, @NotNull final LivingEntity damager, @NotNull final LivingEntity target) {
        // Out of water squids, wardens and shulkers are completely immune to knockback
        final EntityType targetType = target.getType();
        if(
            ((targetType == EntityType.SQUID || targetType == EntityType.GLOW_SQUID) && !target.isInWater()) ||
            targetType == EntityType.WARDEN || targetType == EntityType.SHULKER
        ) return new Vector(0, 0, 0);


        // Calculate base knockback
        Vector knockback;
        if(targetType == EntityType.IRON_GOLEM) knockback = new Vector(0, 0, 0); // Iron Golems ignore the base knockback but not enchants
        else knockback = damager.getLocation().getDirection().multiply(getBaseKnockback(item)); // Set default attack knockback for other entities
        knockback.setY(0.8125f);


        // Vanilla sprint mechanic
        int knockbackLv = 0;
        if(damager instanceof Player player && player.isSprinting()) {
            final Boolean sprintBuff = knockbackSprintBuff.get(player.getUniqueId());
            if(sprintBuff != null && sprintBuff) {
                knockbackSprintBuff.put(player.getUniqueId(), false);
                ++knockbackLv;
            }
        }


        // Calculate enchantments
        if(item != null && item.getType() != Material.AIR) {
            knockbackLv += item.getEnchantmentLevel(Enchantment.KNOCKBACK);
            if(knockbackLv != 0) {
                knockback.add(new Vector(2.586, 0, 2.586).multiply(knockbackLv));
                knockback.setY(1f);
            }
            //TODO calculate raveling enchant
        }

        return knockback;
    }




    private static double getBaseDamage(@Nullable final ItemStack item) {
        if(item == null || item.getType() == Material.AIR) return 1;
        final Long itemId = ItemUtils.getCustomItemId(item);
        if(itemId != null) return ItemManager.getValueFromId(itemId).hitDamage;
        else {
            final Double _damage = defaultDamage.get(item.getType());
            if(_damage != null) return _damage;
            else return 1;
        }
    }

    private static double getDamage(@Nullable final ItemStack item, boolean canCrit, @NotNull final LivingEntity damager, @NotNull final LivingEntity target) {
        double damage = getBaseDamage(item);
        double enchantDamage = 0d;
        double charge = 20; // The ticks passed since the last attack
        if(damager instanceof Player player) charge = player.getAttackCooldown();


        // Calculate critical multiplier //TODO calculate critical enchant
        if(canCrit && isCritical(damager)) {
            damage *= 1.5;
            damager.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 10);
        }


        // Calculate enchantments (Separate charge multiplier)
        if(item != null && item.getType() != Material.AIR) {
            for(Map.Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()) {
                Enchantment key = e.getKey();
                if(key == Enchantment.DAMAGE_ALL)        enchantDamage += 0.5 + e.getValue() * 0.5;
                if(key == Enchantment.DAMAGE_ARTHROPODS) if(target.getCategory() == EntityCategory.ARTHROPOD) enchantDamage += 2.5 * e.getValue();
                if(key == Enchantment.DAMAGE_UNDEAD)     if(target.getCategory() == EntityCategory.UNDEAD)    enchantDamage += 2.5 * e.getValue();
                if(key == Enchantment.IMPALING)          if(target.getCategory() == EntityCategory.WATER)     enchantDamage += 2.5 * e.getValue();
            }
        }


        // Calculate potion effects
        PotionEffect str = damager.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
        if(str != null) damage += 3 * str.getAmplifier();


        // Return effective damage
        return
            damage *        (0.2 + Math.pow(charge, 2) * 0.8) +
            enchantDamage * (0.2 +          charge     * 0.8)
        ;
    }




    /**
     * Checks if an entity can deal critical hits in its current state.
     * @param damager The damager entity
     * @return True if it can deal critical hits, false otherwise
     */
    private static boolean isCritical(@NotNull final LivingEntity damager) {
        return
            damager.getFallDistance() > 0f &&
            !damager.isOnGround() &&
            !damager.isClimbing() &&
            !damager.isInWater() &&
            damager.getVehicle() == null &&
            damager.getPotionEffect(PotionEffectType.BLINDNESS) == null &&
            !(damager instanceof Player player && player.isSprinting())
        ;
    }



    /**
     * Replaces a Vanilla EntityDamageByEntityEvent event with a custom attack.
     * @param e The event to replace
     * @param canCrit True if the attack can crit, false otherwise.
     *                Whether the attack will effectively be critical depends on the current status of the attacking entity
     */
    public static void customAttack(@NotNull final EntityDamageByEntityEvent e, final boolean canCrit) {
        if(e.getDamager() instanceof LivingEntity damager && e.getEntity() instanceof LivingEntity target) {
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
        // Cancel the vanilla event and save the used item
        final EntityEquipment equipment = damager.getEquipment();
        final ItemStack item = equipment == null ? null : equipment.getItemInMainHand();

        // Save the attack data
        final UUID targetId = target.getUniqueId();
        attacks.putIfAbsent(targetId, new CircularFifoQueue<>(2));
        attacks.get(targetId).add(new AttackData(
            damager,
            item,
            System.currentTimeMillis()
        ));


        // Damage the target
        final double damage = getDamage(item, canCrit, damager, target);
        target.damage(damage);
        Bukkit.broadcastMessage("Damaged for " + damage);
        //TODO review and simplify custom scythe attacks

        // Knockback the target
        final Vector velocity = getKnockback(item, damager, target);
        target.setVelocity(target.getVelocity().add(velocity));

        target.setVelocity(velocity);
    }
}
