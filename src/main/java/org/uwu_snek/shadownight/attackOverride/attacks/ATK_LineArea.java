package org.uwu_snek.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.utils.math.Func;

import java.util.Collection;




public final class ATK_LineArea extends ATK {
    public double len;
    public final double width;

    public ATK_LineArea(final double _len, final double _width) {
        len = _len;
        width = _width;
    }


    @Override
    public void execute(final @NotNull LivingEntity damager, @Nullable final LivingEntity directTarget, final @NotNull Location origin, @Nullable final ItemStack item) {
        Vector o = origin.toVector();
        Vector p2 = o.clone().add(origin.getDirection().multiply(len));

        BoundingBox box = new BoundingBox(o.getX(), o.getY(), o.getZ(), p2.getX(), p2.getY(), p2.getZ()).expand(width);
        Collection<Entity> entities = damager.getWorld().getNearbyEntities(box);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity e && Func.distToLine(o, p2, e.getLocation().toVector()) <= width && e != damager) {
                executeBasicAttack(damager, e, origin, true, item, 1, false, null);
            }
        }
    }
}
