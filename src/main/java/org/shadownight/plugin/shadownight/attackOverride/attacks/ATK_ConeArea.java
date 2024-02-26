package org.shadownight.plugin.shadownight.attackOverride.attacks;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.attackOverride.AttackOverride;
import org.shadownight.plugin.shadownight.utils.math.Func;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;




public final class ATK_ConeArea extends ATK {
    private static final HashMap<UUID, Long> last_times = new HashMap<>(); //TODO implement an attack cooldown system straight into the IM class. save the timestamp INTO THE ITEM ITSELF
    private final double attackRange;
    private final long cooldown;


    public ATK_ConeArea(final double _attackRange, final long _cooldown) {
        attackRange = _attackRange;
        cooldown = _cooldown;
    }



    @Override
    public void execute(@NotNull final LivingEntity damager, @Nullable final LivingEntity directTarget, @Nullable final ItemStack item) {
        utils.log(Level.SEVERE, "Detected ConeArea attack");
        final Location damagerPos = damager.getLocation();
        final UUID damagerId = damager.getUniqueId();
        final long currentTime = System.currentTimeMillis();


        final Long last_time = last_times.get(damagerId);
        if(last_time == null || currentTime - last_time >= cooldown) {
            last_times.put(damagerId, currentTime);
            final Vector damagerDirection = damagerPos.getDirection();
            final List<Entity> entities = damager.getNearbyEntities(attackRange, attackRange, attackRange);

            // Save initial attack charge and hit each entity individually (attack charge gets reset by basic attacks)
            final double attackCharge = getEntityCharge(damager);
            for (Entity entity : entities) {
                if (
                    entity instanceof LivingEntity e &&
                    damagerPos.distance(e.getLocation()) < attackRange &&
                    Func.isInCone(damagerPos.toVector(), damagerDirection, e.getLocation().toVector(), 3)
                ) {
                    executeBasicAttack(damager, e, item, attackCharge, false);
                }
            }
            simulateSweepingEffect(damagerPos);
        }
    }
}
