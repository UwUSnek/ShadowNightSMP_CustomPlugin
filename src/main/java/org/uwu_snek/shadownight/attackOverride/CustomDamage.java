package org.uwu_snek.shadownight.attackOverride;

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
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.items.VanillaProvider;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.AbstractMap;
import java.util.Map;




public final class CustomDamage extends UtilityClass {
    /**
     * Calculates the base damage of an attack from <damager> using the item <item>.
     * This doesn't include Enchantments or Potion effects, but it does take into account the base attack damage of the entity and the attack damage of custom items.
     * @param item The item to use
     * @param damager The attacking entity
     * @return The base damage
     */
    private static double getBaseDamage(@Nullable final ItemStack item, final @NotNull LivingEntity damager) {
        // If an item was used, return its base damage
        final Long itemId = ItemUtils.getCustomItemId(item);
        if(itemId != null) return ItemManager.getValueFromId(itemId).getHitDamage();
        else if(item != null && item.getType() != Material.AIR) return VanillaProvider.getDamage(item.getType());

        // If no item was used (most mobs & players punching), return the entity's base damage if it can attack, or 0 if not.
        else {
            final AttributeInstance attribute = damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            return attribute == null ? 0 : attribute.getBaseValue();
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
    public static double getDamage(@Nullable final ItemStack item, boolean canCrit, final @NotNull LivingEntity damager, final @NotNull LivingEntity target, final double charge) {
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
    private static boolean entityCanCrit(final @NotNull LivingEntity e) {
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
