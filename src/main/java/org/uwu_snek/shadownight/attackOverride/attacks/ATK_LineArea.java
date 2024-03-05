package org.uwu_snek.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.utils.math.Func;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;




public final class ATK_LineArea extends ATK {
    public double len;
    public final double width;
    public final boolean ignoreCharge;

    public ATK_LineArea(final double _len, final double _width, final boolean _ignoreCharge) {
        len = _len;
        width = _width;
        ignoreCharge = _ignoreCharge;
    }


    @Override
    public void execute(final @NotNull LivingEntity damager, @Nullable final LivingEntity directTarget, final @NotNull Location origin, @Nullable final ItemStack item) {        final UUID damagerId = damager.getUniqueId();
        final Vector o = origin.toVector();
        final Vector p2 = o.clone().add(origin.getDirection().multiply(len));

        final BoundingBox box = new BoundingBox(o.getX(), o.getY(), o.getZ(), p2.getX(), p2.getY(), p2.getZ()).expand(width);
        final Collection<Entity> entities = damager.getWorld().getNearbyEntities(box);
        final double charge = ignoreCharge ? 1 : getEntityCharge(damager);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity e && Func.distToLine(o, p2, e.getLocation().toVector()) <= width && e != damager) {
                executeBasicAttack(damager, e, origin, true, item, charge, false, null);
            }
        }
    }
}
