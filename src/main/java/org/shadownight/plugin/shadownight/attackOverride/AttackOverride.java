package org.shadownight.plugin.shadownight.attackOverride;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.*;
import java.util.logging.Level;




/**
 * This class is used to manage player attacks because Bukkit's system is atrocious.
 * Allows for custom attacks and overrides vanilla attacks.
 * Provides actually useful data for the death message manager.
 */
public final class AttackOverride extends UtilityClass {
    public static class AttackData {
        public final LivingEntity damager;
        public final ItemStack usedItem;
        public final Long time;

        // This is used to determine if an event has to actually damage the entity and trigger vanilla mob behaviours.
        // It's set to true after the mirror event is fired (event got mirrored, no need to do that again).
        public boolean mirrored = false;

        public AttackData(LivingEntity _damager, ItemStack _usedItem, Long _time) {
            damager = _damager;
            usedItem = _usedItem;
            time = _time;
        }
    }
    public static final HashMap<UUID, CircularFifoQueue<AttackData>> attacks = new HashMap<>();
    //TODO use this for more detailed death messages
    //TODO maybe save any damage in the queue



    /**
     * Replaces a Vanilla EntityDamageByEntityEvent event with a custom attack.
     * @param e The event to replace
     * @param canCrit True if the attack can crit, false otherwise.
     *                Whether the attack will effectively be critical depends on the current status of the attacking entity
     */
    public static void customAttack(@NotNull final EntityDamageByEntityEvent e, final boolean canCrit) {
        CircularFifoQueue<AttackData> attackQueue = attacks.get(e.getEntity().getUniqueId());
        if(attackQueue != null) {
            AttackData lastAttack = attackQueue.get(attackQueue.size() == 1 ? 0 : 1);
            if(lastAttack.damager == e.getDamager() && !lastAttack.mirrored) {
                utils.log(Level.WARNING, "[" + e.getDamager().getType() + "] Actual damage let through: " + e.getFinalDamage());
                lastAttack.mirrored = true;
                return;
            }
        }

        // Use Vanilla creeper explosions
        if(e.getDamager().getType() == EntityType.CREEPER) return;

        // Cancel event and compute custom attack
        if (e.getDamager() instanceof LivingEntity damager && e.getEntity() instanceof LivingEntity target) {
            if(target.getNoDamageTicks() == 0) {
                utils.log(Level.SEVERE, "[" + damager.getType() + "] Entity attack detected. Vanilla damage: " + e.getFinalDamage());
                e.setCancelled(true);
                customAttack(damager, target, canCrit);
            }
        }
    }




    /**
     * Registers a custom attack.
     * This method is also used to replace Vanilla EntityDamageByEntityEvent events.
     * @param damager The attacking entity
     * @param target The damaged entity
     * @param canCrit True if the attack can crit, false otherwise.
     *                Whether the attack will effectively be critical depends on the current status of the attacking entity
     */
    public static void customAttack(@NotNull final LivingEntity damager, @NotNull final LivingEntity target, final boolean canCrit) {
        // Cancel the vanilla event and save the used item
        final EntityEquipment equipment = damager.getEquipment();
        final ItemStack item = equipment == null ? null : equipment.getItemInMainHand();

        // Save the attack data
        final UUID targetId = target.getUniqueId();
        attacks.putIfAbsent(targetId, new CircularFifoQueue<>(2)); //TODO remove when entity is killed or removed
        attacks.get(targetId).add(new AttackData(
            damager,
            item,
            System.currentTimeMillis()
        ));




        // Calculate pre-hit velocity
        final Vector velocity = target.getVelocity();       // Starting velocity
        velocity.setY(velocity.getY() + 0.0784);            // Account for mob's default negative Y velocity
        velocity.add(CustomKnockback.getKnockback(item, damager, target));  // Add knockback

        // Damage the target
        final double damage = CustomDamage.getDamage(item, canCrit, damager, target);
        utils.log(Level.WARNING, "[" + damager.getType() + "] Custom damage sent: " + damage);
        target.damage(damage, damager);

        //TODO review and simplify custom scythe attacks


        // Apply new velocity (and override .damage's Vanilla knockback)
        target.setVelocity(velocity);
    }


    public static void onDeath(EntityDeathEvent e) {
        attacks.remove(e.getEntity().getUniqueId());
    }
}
