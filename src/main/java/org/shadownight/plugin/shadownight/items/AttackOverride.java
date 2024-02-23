package org.shadownight.plugin.shadownight.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.damage.DamageSource;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
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
        public Player attacker;
        public ItemStack usedItem;
        public Long time;
    }
    public static HashMap<UUID, Pair<AttackData, AttackData>> attacks;



    public static void onAttack(@NotNull final EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player damager && e.getEntity() instanceof LivingEntity target) {
            // Cancel the vanilla event and save the used item
            e.setCancelled(true);
            final ItemStack item = damager.getInventory().getItemInMainHand();

            // Get base attack damage
            double damage;
            final Long itemId = ItemUtils.getCustomItemId(item);
            if(itemId != null) damage = ItemManager.getValueFromId(itemId).hitDamage;
            else {
                final Double _damage = defaultDamage.get(item.getType());
                if(_damage != null) damage = _damage;
                else damage = 1;
            }

            // Save the attack data and damage the target
            target.damage(10);
            Bukkit.broadcastMessage("Damaged for " + damage);
        }
    }
}
