package org.uwu_snek.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;




public final class ATK_CylinderArea extends ATK {
    private final double dist;
    private final double h;

    public ATK_CylinderArea(final double _dist, final double _h) {
        dist = _dist;
        h = _h;
    }



    @Override
    public void execute(final @NotNull LivingEntity damager, @Nullable final LivingEntity directTarget, final @NotNull Location origin, @Nullable final ItemStack item) {
        for(Entity entity : origin.getNearbyEntities(dist, h, dist)){
            if(entity instanceof LivingEntity e){
                Location targetPos = e.getLocation();
                if(new Vector(targetPos.getX(), origin.getY(), targetPos.getZ()).distance(origin.toVector()) < dist && Math.abs(targetPos.getY() - origin.getY()) < h) {
                    executeBasicAttack(damager, e, origin, false, item, 1, false, null);
                }
            }
        }
        simulateSweepingEffect(origin);
    }
}
