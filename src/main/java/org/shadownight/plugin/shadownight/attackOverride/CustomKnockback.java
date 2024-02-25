package org.shadownight.plugin.shadownight.attackOverride;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.items.ItemManager;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;

import java.util.HashMap;
import java.util.UUID;




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
     * This doesn't include the target entity's knockback resistance.
     * @param item The item to use
     * @return The knockback multiplier
     */
    private static double getBaseKnockbackMultiplier(@Nullable final ItemStack item) {
        double base = 1;
        if(item != null && item.getType() != Material.AIR) {
            final Long itemId = ItemUtils.getCustomItemId(item);
            if(itemId != null) base *= ItemManager.getValueFromId(itemId).getHitKnockbackMultiplier();
        }
        return base;
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
        Vector knockback = direction.clone().multiply(defaultKnockbackXZ).multiply(getBaseKnockbackMultiplier(item)).setY(defaultKnockbackY);



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
            if(knockbackLv > 0) knockback.setY(enchantKnockbackY);
            //TODO calculate reeling enchant
        }



        // Calculate resistance
        final AttributeInstance attribute = target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        double resistance = attribute == null ? 0 : attribute.getBaseValue(); // 0 to 100

        // Return effective knockback
        return knockback.multiply(1 - resistance);
    }
}
