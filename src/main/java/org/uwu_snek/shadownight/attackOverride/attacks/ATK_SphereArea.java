package org.uwu_snek.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;




public final class ATK_SphereArea extends ATK {
    private final double dist;

    public ATK_SphereArea(final double _dist) {
        dist = _dist;
    }



    @Override
    public void execute(final @NotNull LivingEntity damager, @Nullable final LivingEntity directTarget, final @NotNull Location origin, @Nullable final ItemStack item) {
        for(Entity entity : damager.getNearbyEntities(dist, dist, dist)){
            if(entity instanceof LivingEntity e && e.getLocation().distance(origin) < dist) {
                executeBasicAttack(damager, e, origin, false, item, 1, false, null);
            }
        }
        simulateSweepingEffect(origin);
    }
}
