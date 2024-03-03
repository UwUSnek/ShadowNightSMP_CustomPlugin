package org.uwu_snek.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.attackOverride.CustomDamage;




public final class ATK_Standard extends ATK {
    private static final double sweepingMinCharge = 0.848d;
    private static final double sweepingMaxDist = 3d;
    private static final Vector sweepingTargetBox = new Vector(1f, 0.25f, 1f);

    @Override
    public void execute(final @NotNull LivingEntity damager, @Nullable final LivingEntity directTarget, final @NotNull Location origin, @Nullable final ItemStack item) {
        if(directTarget != null) {
            // Main attack
            double charge = getEntityCharge(damager);
            executeBasicAttack(damager, directTarget, origin, false, item, charge, true, null);

            // Sweeping attack (follow Vanilla's requirements and area, but use custom damage and effects)
            if(item == null) return;
            Material type = item.getType();
            if(
                damager instanceof Player player && (
                    type == Material.WOODEN_SWORD ||
                    type == Material.STONE_SWORD ||
                    type == Material.IRON_SWORD ||
                    type == Material.DIAMOND_SWORD ||
                    type == Material.NETHERITE_SWORD
                ) &&
                charge > sweepingMinCharge &&
                damager.isOnGround() &&
                !player.isSprinting() &&
                player.getVehicle() == null
            ) {
                simulateSweepingEffect(damager.getLocation());
                BoundingBox box = directTarget.getBoundingBox().expand(sweepingTargetBox);
                for(Entity entity : directTarget.getWorld().getNearbyEntities(box)){
                    if(entity instanceof LivingEntity e && e != directTarget && e.getLocation().distance(damager.getLocation()) <= sweepingMaxDist) {
                        executeBasicAttack(damager, e, origin, false, item, charge, false, CustomDamage.getDamage(item, false, damager, e, charge) * 0.8);
                    }
                }
            }
        }
    }
}
