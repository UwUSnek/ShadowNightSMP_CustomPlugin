package org.shadownight.plugin.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.utils.math.Func;

import java.util.Collection;




public final class ATK_ReachArea extends ATK {
    private final double dist;

    public ATK_ReachArea(final double _dist) {
        dist = _dist;
    }



    @Override
    public void execute(@NotNull final LivingEntity damager, @Nullable final LivingEntity directTarget, @NotNull final Location origin, @Nullable final ItemStack item) {
    }
}
