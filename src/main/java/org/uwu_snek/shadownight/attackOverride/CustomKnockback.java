package org.uwu_snek.shadownight.attackOverride;

import org.bukkit.Location;
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
import org.uwu_snek.shadownight.enchantments.CustomEnchant_Spigot;
import org.uwu_snek.shadownight.enchantments.implementations.Reeling;
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;




public final class CustomKnockback extends UtilityClass {
    public static final HashMap<UUID, Boolean> knockbackSprintBuff = new HashMap<>();
    public static final double defaultKnockbackXZ = 0.4d;  // The default knockback that is dealt on the XZ Plane using no enchantments or vanilla knockback buff mechanics.
    public static final double defaultKnockbackY = 0.325d; // The default knockback that is dealt on the Y  Axis  using no enchantments or vanilla knockback buff mechanics.
    public static final double enchantKnockbackXZ = 0.3d;  // The additional knockback deal on the XZ Plane by the Vanilla Knockback Enchantment
    public static final double enchantKnockbackY = 0.4d;   // The total      knockback deal on the Y  axis  by the Vanilla Knockback Enchantment
    public static final double gravityY = -0.0784d;        //The default vertical velocity of non-onGround() entities


    /**
     * Resets the knockback buffed that a player gains after they start sprinting.
     * @param event The sprint toggle event
     */
    public static void resetKnockbackSprintBuff(final @NotNull PlayerToggleSprintEvent event){
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
    private static double getTargetKnockbackResistance(final @NotNull LivingEntity target) {
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
     *
     * @param damager The attacking entity
     * @param target  The attacked entity
     * @param item    The item used to hit the target
     * @return The final knockback vector
     */
    public static @NotNull Vector getKnockback(final @NotNull LivingEntity damager, final @NotNull LivingEntity target, final @NotNull Location origin, boolean follorOriginDirection, @Nullable final ItemStack item) {
        // Calculate base knockback
        Vector direction = follorOriginDirection ? origin.getDirection() : origin.toVector().subtract(target.getLocation().toVector()).setY(0).normalize().multiply(-1);
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
            // Knockback
            int knockbackLv = item.getEnchantmentLevel(Enchantment.KNOCKBACK);
            knockback.add(new Vector(enchantKnockbackXZ, 0, enchantKnockbackXZ).multiply(knockbackLv + hasSprintbuff).multiply(direction));
            if(knockbackLv + hasSprintbuff > 0) {
                knockback.setY(enchantKnockbackY);
            }

            // Reeling
            int reelingLv = item.getEnchantmentLevel(CustomEnchant_Spigot.REELING);
            if(reelingLv > 0) {
                knockback.add(Reeling.getVelocity(reelingLv).multiply(direction));
            }
        }


        // Apply resistance
        knockback.multiply(1 - getTargetKnockbackResistance(target));

        // Calculate gravity or Vanilla no-vertical-knockback-if-no-on-ground mechanic which i don't know the name of
        if(!target.isOnGround()) knockback.setY(0);           // If not on ground, set Y knockback to 0
        else knockback.setY(knockback.getY() - gravityY);     // If on ground, account for gravity

        // Return effective knockback
        return knockback;
    }
}
