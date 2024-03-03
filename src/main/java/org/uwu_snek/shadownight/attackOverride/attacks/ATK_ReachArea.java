package org.uwu_snek.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;




public final class ATK_ReachArea extends ATK {
    private final double dist;

    public ATK_ReachArea(final double _dist) {
        dist = _dist;
    }



    @Override
    public void execute(final @NotNull LivingEntity damager, @Nullable final LivingEntity directTarget, final @NotNull Location origin, @Nullable final ItemStack item) {
    }
}
