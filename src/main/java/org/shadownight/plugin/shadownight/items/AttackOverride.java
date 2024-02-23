package org.shadownight.plugin.shadownight.items;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;

import java.time.LocalTime;
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
    public static HashMap<UUID, CircularFifoQueue<AttackData>> attacks;







    private static @NotNull Vector getBaseVelocity(@Nullable final ItemStack item) {
        final Vector base = new Vector(1.552f,  0.8125f, 1.552f);
        if(item != null && item.getType() != Material.AIR) {
            final Long itemId = ItemUtils.getCustomItemId(item);
            if(itemId != null) base.multiply(ItemManager.getValueFromId(itemId).getHitKnockback());
        }
        return base;
    }

    private static @NotNull Vector getVelocity(@Nullable final ItemStack item, @NotNull final LivingEntity damager) {
        Vector velocity = getBaseVelocity(item); // Default attack knockback

        // Calculate enchantments
        if(item != null && item.getType() != Material.AIR) {
            int knockbackLv = item.getEnchantmentLevel(Enchantment.KNOCKBACK);
            if(knockbackLv != 0) {
                velocity.add(new Vector(2.586, 0, 2.586).multiply(knockbackLv));
                velocity.setY(1f);
            }
            //TODO calculate raveling enchant
        }

        return velocity;
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

        // Calculate critical multiplier //TODO calculate critical enchant
        if(canCrit && isCritical(damager)) {
            damage *= 1.5;
            damager.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 10);
        }

        // Calculate enchantments
        if(item != null && item.getType() != Material.AIR) {
            for(Map.Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()) {
                Enchantment key = e.getKey();
                if(key == Enchantment.DAMAGE_ALL)        damage += 0.5 + e.getValue() * 0.5;
                if(key == Enchantment.DAMAGE_ARTHROPODS) if(target.getCategory() == EntityCategory.ARTHROPOD) damage += 2.5 * e.getValue();
                if(key == Enchantment.DAMAGE_UNDEAD)     if(target.getCategory() == EntityCategory.UNDEAD)    damage += 2.5 * e.getValue();
                if(key == Enchantment.IMPALING)          if(target.getCategory() == EntityCategory.WATER)     damage += 2.5 * e.getValue();
            }
        }

        // Calculate potion effects
        PotionEffect str = damager.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
        if(str != null) damage += 3 * str.getAmplifier();

        // Return effective damage
        return damage;
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
        final Vector velocity = getVelocity(item, damager);
        target.setVelocity(target.getVelocity().add(velocity));

        target.setVelocity(velocity);
    }
}
