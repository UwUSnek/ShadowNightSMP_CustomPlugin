package org.shadownight.plugin.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.Warning;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;




public final class ATK_Standard extends ATK {
    private static final double sweepingMinCharge = 0.848d;
    private static final double sweepingMaxDist = 3d;
    private static final Vector sweepingTargetBox = new Vector(1f, 0.25f, 1f);

    @Override
    public void execute(@NotNull final LivingEntity damager, @Nullable final LivingEntity directTarget, @NotNull final Location origin, @Nullable final ItemStack item) {
        if(directTarget != null) {
            // Main attack
            double charge = getEntityCharge(damager);
            executeBasicAttack(damager, directTarget, origin, item, charge, true, null);

            // Sweeping attack (follow Vanilla's requirements and area, but use custom damage and effects)
            if(
                damager instanceof Player player &&
                charge > sweepingMinCharge &&
                damager.isOnGround() &&
                !player.isSprinting() &&
                player.getVehicle() == null
            ) {
                simulateSweepingEffect(origin);
                BoundingBox box = directTarget.getBoundingBox().expand(sweepingTargetBox);
                for(Entity entity : directTarget.getWorld().getNearbyEntities(box)){
                    if(entity instanceof LivingEntity e && e != directTarget && e.getLocation().distance(damager.getLocation()) <= sweepingMaxDist) {
                        executeBasicAttack(damager, e, origin, item, charge, false, null);
                    }
                }
            }
        }
    }
}
