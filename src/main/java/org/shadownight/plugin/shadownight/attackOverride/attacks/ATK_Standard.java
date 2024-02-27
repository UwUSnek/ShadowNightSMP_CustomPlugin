package org.shadownight.plugin.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;




public final class ATK_Standard extends ATK {
    private static final double sweepingMinCharge = 0.848d;

    @Override
    public void execute(@NotNull final LivingEntity damager, @Nullable final LivingEntity directTarget, @NotNull final Location origin, @Nullable final ItemStack item) {
        if(directTarget != null) {
            // Normal attack
            executeBasicAttack(damager, directTarget, origin, item, getEntityCharge(damager), true, null);

            // Sweeping attack (follow Vanilla's requirements and area, but use custom damage and effects)
            if(
                damager instanceof Player player &&
                getEntityCharge(player) > sweepingMinCharge &&
                damager.isOnGround() &&
                !player.isSprinting() &&
                player.getVehicle() == null
            ) {

                simulateSweepingEffect(origin);
            }
        }
    }
}
