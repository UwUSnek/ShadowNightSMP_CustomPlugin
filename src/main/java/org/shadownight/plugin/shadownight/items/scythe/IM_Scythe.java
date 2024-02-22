package org.shadownight.plugin.shadownight.items.scythe;

import com.google.common.collect.HashMultimap;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.items.IM;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public abstract class IM_Scythe extends IM {
    public IM_Scythe() {}

    protected abstract double getAttackSpeed();
    protected abstract double getDamage();



    @Override
    protected void setItemAttributes() {
        final ItemMeta meta = defaultItem.getItemMeta();
        Objects.requireNonNull(meta, "Object meta is null");
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,  new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed",  getAttackSpeed(), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", getDamage(),      AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        defaultItem.setItemMeta(meta);
    }




    static private void breakBlocks(final @NotNull Player player) {
        Location playerPos = player.getLocation();
        Vector playerDirection = playerPos.getDirection();

        player.sendMessage("debug: used rclick ability");
        //for()
    }





    public static final HashMultimap<UUID, UUID> attackQueue = HashMultimap.create();
    private static final HashMap<UUID, Long> last_times = new HashMap<>();
    private static final double attackRange = 6;
    private static final long cooldown = 500;

    protected void customAttack(final @NotNull Player player, final @NotNull ItemStack item) {
        final Location playerPos = player.getLocation();
        final UUID playerId = player.getUniqueId();
        final long currentTime = System.currentTimeMillis();


        final Long last_time = last_times.get(playerId);
        if(last_time == null || currentTime - last_time >= cooldown) {
            last_times.put(playerId, currentTime);
            final Vector playerDirection = playerPos.getDirection();
            final List<Entity> entities = player.getNearbyEntities(attackRange, attackRange, attackRange);

            final double vanillaCooldown = player.getAttackCooldown();
            final double damage = vanillaCooldown * Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE), "Attack damage attribute is null").getValue();

            int damagedEntities = 0;
            for (Entity e : entities) {
                if (
                    e instanceof LivingEntity &&
                        playerPos.distance(e.getLocation()) < attackRange &&
                        utils.isInCone(playerPos.toVector(), playerDirection, e.getLocation().toVector(), 3)
                ) {
                    ++damagedEntities;
                    attackQueue.put(playerId, e.getUniqueId());
                    ((LivingEntity) e).damage(damage, player);
                    attackQueue.remove(playerId, e.getUniqueId());
                    e.setVelocity(e.getVelocity().add(playerDirection.clone().multiply(new Vector(1, 0, 1)).multiply(vanillaCooldown))); // Double the normal kb (Damaging e already gives it normal kb)
                }
            }


            if (damagedEntities > 0) utils.damageItem(player, item);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, playerPos.clone().add(playerDirection.clone().multiply(new Vector(2, 0, 2))).add(new Vector(0, 1, 0)), 1, 0, 0, 0);
        }
    }



    @Override
    protected void onInteract(final @NotNull PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            customAttack(event.getPlayer(), event.getItem());
        }
        else if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            breakBlocks(event.getPlayer());
        }
    }


    @Override
    protected void onAttack(final @NotNull EntityDamageByEntityEvent event) {
        final Player player = (Player) event.getDamager();
        final Entity target = event.getEntity();

        final UUID playerId = player.getUniqueId();
        final UUID targetId = target.getUniqueId();


        if(attackQueue.containsEntry(playerId, targetId)) {
            attackQueue.remove(playerId, targetId);
        }
        else {
            event.setCancelled(true);
            customAttack(player, player.getInventory().getItemInMainHand());
        }
    }
}
