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

import java.util.HashMap;
import java.util.List;
import java.util.UUID;




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
        final Location damagerPos = damager.getLocation();
        final UUID damagerId = damager.getUniqueId();
        final long currentTime = System.currentTimeMillis();


        final Long last_time = last_times.get(damagerId);
        if(last_time == null || currentTime - last_time >= cooldown) {
            last_times.put(damagerId, currentTime);
            final Vector playerDirection = damagerPos.getDirection();
            final List<Entity> entities = damager.getNearbyEntities(attackRange, attackRange, attackRange);

            final double attackCharge = getEntityCharge(damager);
            //final double damage = vanillaCooldown * Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE), "Attack damage attribute is null").getValue();

            int damagedEntities = 0;
            for (Entity entity : entities) {
                if (
                    entity instanceof LivingEntity e &&
                    damagerPos.distance(e.getLocation()) < attackRange &&
                    Func.isInCone(damagerPos.toVector(), playerDirection, e.getLocation().toVector(), 3)
                ) {
                    ++damagedEntities;
                    //attackQueue.put(playerId, e.getUniqueId());
                    //((LivingEntity) e).damage(damage, player);
                    executeBasicAttack(damager, e, item, attackCharge, false);
                    //attackQueue.remove(playerId, e.getUniqueId());
                    //e.setVelocity(e.getVelocity().add(playerDirection.clone().multiply(new Vector(1, 0, 1)).multiply(vanillaCooldown))); // Double the normal kb (Damaging e already gives it normal kb)
                }
            }


            if (damagedEntities > 0) ItemUtils.damageItem(damager, item);
            damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            damager.getWorld().spawnParticle(Particle.SWEEP_ATTACK, damagerPos.clone().add(playerDirection.clone().multiply(new Vector(2, 0, 2))).add(new Vector(0, 1, 0)), 1, 0, 0, 0);
        }
    }
}
