package org.shadownight.plugin.shadownight.attackOverride;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.items.ItemManager;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;




public final class CustomKnockback extends UtilityClass {
    private static final HashMap<UUID, Boolean> knockbackSprintBuff = new HashMap<>();
    private static final double defaultKnockbackXZ = 0.4d;  // The default knockback that is dealt on the XZ Plane using no enchantments or vanilla knockback buff mechanics.
    private static final double defaultKnockbackY = 0.325d; // The default knockback that is dealt on the Y  Axis  using no enchantments or vanilla knockback buff mechanics.
    private static final double enchantKnockbackXZ = 0.3d;  // The additional knockback deal on the XZ Plane by the Vanilla Knockback Enchantment
    private static final double enchantKnockbackY = 0.4d;   // The total      knockback deal on the Y  axis  by the Vanilla Knockback Enchantment


    /**
     * Resets the knockback buffed that a player gains after they start sprinting.
     * @param event The sprint toggle event
     */
    public static void resetKnockbackSprintBuff(@NotNull final PlayerToggleSprintEvent event){
        if(event.isSprinting()) knockbackSprintBuff.put(event.getPlayer().getUniqueId(), true);
    }


    /**
     * Calculates the knockback multiplier of an item.
     * @param item The item to use
     * @return The knockback multiplier
     */
    private static double getItemKnockbackMultiplier(@Nullable final ItemStack item) {
        double base = 1;
        if(item != null && item.getType() != Material.AIR) {
            final Long itemId = ItemUtils.getCustomItemId(item);
            if(itemId != null) base *= ItemManager.getValueFromId(itemId).getHitKnockbackMultiplier();
        }
        return base;
    }


    /**
     * Calculates the knockback resistance of an entity (0f-1f).
     * This includes its base knockback resistance and its armor attributes.
     * @param target The target entity
     * @return The knockback resistance
     */
    private static double getTargetKnockbackResistance(@NotNull final LivingEntity target) {
        double resistance = 0;

        // Base resistance
        final AttributeInstance attribute = target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        resistance += attribute == null ? 0 : attribute.getBaseValue();

        // Item attributes
        EntityEquipment equipment = target.getEquipment();
        if(equipment != null) {
            for(ItemStack a : equipment.getArmorContents()) if(a != null) {
                Material type = a.getType();
                if(type == Material.NETHERITE_HELMET || type == Material.NETHERITE_CHESTPLATE || type == Material.NETHERITE_LEGGINGS || type == Material.NETHERITE_BOOTS) {
                    resistance += 0.1; // Netherite's knockback resistance is not an attribute, for some reason. Go figure
                }
                if(a.hasItemMeta()) {
                    ItemMeta meta = a.getItemMeta();
                    Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
                    if(modifiers != null) for(AttributeModifier m : modifiers) {
                        resistance += m.getAmount();
                    }
                }
            }
        }

        return resistance;
    }




    /**
     * Calculates the final knockback the entity <target> should receive after being hit by <damager> using the item <item>.
     * This doesn't include the current velocity of the target nor the default gravity value.
     * @param item The item used to hit the target
     * @param damager The attacking entity
     * @param target The attacked entity
     * @return The final knockback vector
     */
    static @NotNull Vector getKnockback(@Nullable final ItemStack item, @NotNull final LivingEntity damager, @NotNull final LivingEntity target) {
        // Calculate base knockback
        Vector direction = damager.getLocation().getDirection().setY(0).normalize();
        Vector knockback = direction.clone().multiply(defaultKnockbackXZ).multiply(getItemKnockbackMultiplier(item)).setY(defaultKnockbackY);



        // Vanilla sprint mechanic
        int hasSprintbuff = 0;
        if(damager instanceof Player player && player.isSprinting()) {
            final Boolean sprintBuff = knockbackSprintBuff.get(player.getUniqueId());
            if(sprintBuff != null && sprintBuff) {
                knockbackSprintBuff.put(player.getUniqueId(), false);
                ++hasSprintbuff;
            }
        }
        // Calculate enchantments
        if(item != null && item.getType() != Material.AIR) {
            int knockbackLv = item.getEnchantmentLevel(Enchantment.KNOCKBACK);
            knockback.add(new Vector(enchantKnockbackXZ, 0, enchantKnockbackXZ).multiply(knockbackLv + hasSprintbuff).multiply(direction));
            if(knockbackLv + hasSprintbuff > 0) knockback.setY(enchantKnockbackY);
            //TODO calculate reeling enchant
        }


        // Return effective knockback
        return knockback.multiply(1 - getTargetKnockbackResistance(target));
    }
}
