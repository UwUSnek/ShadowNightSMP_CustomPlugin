package org.uwu_snek.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.utils.math.Func;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;




public final class ATK_ConeArea extends ATK {
    private final double attackRange;


    public ATK_ConeArea(final double _attackRange) {
        attackRange = _attackRange;
    }



    @Override
    public void execute(final @NotNull LivingEntity damager, @Nullable final LivingEntity directTarget, final @NotNull Location origin, @Nullable final ItemStack item) {
        final Vector damagerDirection = origin.getDirection();
        final List<Entity> entities = damager.getNearbyEntities(attackRange, attackRange, attackRange);

        // Save initial attack charge and hit each entity individually (attack charge gets reset by basic attacks)
        final double attackCharge = getEntityCharge(damager);
        for (Entity entity : entities) {
            if (
                entity instanceof LivingEntity e &&
                origin.distance(e.getLocation()) < attackRange &&
                Func.isInCone(origin.toVector(), damagerDirection, e.getLocation().toVector(), 3)
            ) {
                executeBasicAttack(damager, e, origin, false, item, attackCharge, false, null);
            }
        }
        simulateSweepingEffect(origin);
    }
}
