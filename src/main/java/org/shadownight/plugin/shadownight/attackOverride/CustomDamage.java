package org.shadownight.plugin.shadownight.attackOverride;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.items.ItemManager;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.AbstractMap;
import java.util.Map;
import java.util.logging.Level;




public final class CustomDamage extends UtilityClass {
    /**
     * A map containing the Vanilla damage of each weapon and tool because apparently Bukkit doesn't have a way to retrieve that information.
     */
    private static final Map<Material, Double> vanillaDamage = Map.ofEntries(
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


    /**
     * Calculates the base damage of an attack from <damager> using the item <item>.
     * This doesn't include Enchantments or Potion effects, but it does take into account the base attack damage of the entity and the attack damage of custom items.
     * @param item The item to use
     * @param damager The attacking entity
     * @return The base damage
     */
    private static double getBaseDamage(@Nullable final ItemStack item, @NotNull final LivingEntity damager) {
        // Get item base attack damage
        final Long itemId = ItemUtils.getCustomItemId(item);
        if(itemId != null) return ItemManager.getValueFromId(itemId).getHitDamage();                    // If item is a custom item, get the base damage from its item manager
        else if(item != null && item.getType() != Material.AIR) {                                       // If item is a vanilla item
            final Double _vanillaDamage = vanillaDamage.get(item.getType());                                // If the item has a vanilla hard coded damage value, use that
            return _vanillaDamage == null ? 1d : _vanillaDamage;                                            // If not, return the default 1 damage
        }
        else {                                                                                          // If no item was used (most mobs & players punching)
            final AttributeInstance attribute = damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);      // If the entity can attack (has an attack damage attribute) use that
            return attribute == null ? 0 : attribute.getBaseValue();                                        // If not, return the default 0 damage
        }
    }


    /**
     * Calculates the total damage the entity <target> should receive after being attacked by <damager> using the item <item>.
     * This doesn't take into account any debuff (Armor points, Enchantments, Resistance effect) applied by the target entity.
     * @param item The item used to attack
     * @param canCrit Whether the attack can produce a critical hit
     * @param damager The attacking entity
     * @param target The attacked entity
     * @return The total damage
     */
    public static double getDamage(@Nullable final ItemStack item, boolean canCrit, @NotNull final LivingEntity damager, @NotNull final LivingEntity target, final double charge) {
        double damage = getBaseDamage(item, damager);
        double enchantDamage = 0d;


        // Calculate potion effects
        PotionEffect str = damager.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
        if(str != null) damage += 3 * str.getAmplifier();


        // Calculate critical multiplier //TODO calculate critical enchant
        if(canCrit && entityCanCrit(damager)) damage *= 1.5;


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


        // Return effective damage
        return
            damage *        (0.2 + Math.pow(charge, 2) * 0.8) +
            enchantDamage * (0.2 +          charge     * 0.8)
        ;
    }




    /**
     * Checks if an entity can deal critical hits in its current state.
     * @param e The damager entity
     * @return True if it can deal critical hits, false otherwise
     */
    private static boolean entityCanCrit(@NotNull final LivingEntity e) {
        return
            !e.isOnGround() &&
            e.getFallDistance() > 0f &&
            !e.isClimbing() &&
            !e.isInWater() &&
            e.getVehicle() == null &&
            e.getPotionEffect(PotionEffectType.BLINDNESS) == null &&
            !(e instanceof Player player && player.isSprinting())
        ;
    }
}
