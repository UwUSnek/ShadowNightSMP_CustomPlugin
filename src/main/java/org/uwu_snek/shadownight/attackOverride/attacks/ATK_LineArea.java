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

    private static final HashMap<UUID, Long> last_times = new HashMap<>(); //TODO implement an attack cooldown system straight into the IM class. save the timestamp INTO THE ITEM ITSELF
    private final long cooldown;

    public ATK_LineArea(final double _len, final double _width, final boolean _ignoreCharge, final long _cooldown) {
        len = _len;
        width = _width;
        ignoreCharge = _ignoreCharge;
        cooldown = _cooldown;
    }


    @Override
    public void execute(final @NotNull LivingEntity damager, @Nullable final LivingEntity directTarget, final @NotNull Location origin, @Nullable final ItemStack item) {        final UUID damagerId = damager.getUniqueId();
        final long currentTime = System.currentTimeMillis();
        final Long last_time = last_times.get(damagerId);
        if(last_time == null || currentTime - last_time >= cooldown) {
            last_times.put(damagerId, currentTime);

            Vector o = origin.toVector();
            Vector p2 = o.clone().add(origin.getDirection().multiply(len));

            BoundingBox box = new BoundingBox(o.getX(), o.getY(), o.getZ(), p2.getX(), p2.getY(), p2.getZ()).expand(width);
            Collection<Entity> entities = damager.getWorld().getNearbyEntities(box);
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity e && Func.distToLine(o, p2, e.getLocation().toVector()) <= width && e != damager) {
                    executeBasicAttack(damager, e, origin, true, item, ignoreCharge ? 1 : (damager instanceof Player p ? p.getAttackCooldown() : 1), false, null);
                }
            }
        }
    }
}
