package org.shadownight.plugin.shadownight.attackOverride;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityRemoveEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.attackOverride.attacks.ATK_Standard;
import org.shadownight.plugin.shadownight.items.ItemManager;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.ClaimUtils;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;
import org.shadownight.plugin.shadownight.utils.spigot.Scheduler;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.*;
import java.util.logging.Level;




/**
 * This class is used to manage player attacks because Bukkit's system is atrocious.
 * Allows for custom attacks and overrides vanilla attacks.
 * Provides actually useful data for the death message manager.
 */
public final class AttackOverride extends UtilityClass implements Rnd {
    public static final HashMap<UUID, CircularFifoQueue<AttackData>> attacks = new HashMap<>();
    private static final ATK_Standard vanillaAttack = new ATK_Standard();



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
    //TODO use this for more detailed death messages
    //TODO maybe save any damage in the queue


    /**
     * Replaces a Vanilla EntityDamageByEntityEvent event with a custom attack.
     * @param e The event to replace
     */
    public static void customAttack(@NotNull final EntityDamageByEntityEvent e) {
        // Use Vanilla creeper explosions
        if (e.getDamager().getType() == EntityType.CREEPER) return;

        // Skip mirror events
        CircularFifoQueue<AttackData> attackQueue = attacks.get(e.getEntity().getUniqueId());
        if (attackQueue != null) {
            AttackData lastAttack = attackQueue.get(attackQueue.size() == 1 ? 0 : 1);
            if (lastAttack.damager == e.getDamager() && !lastAttack.mirrored) {
                lastAttack.mirrored = true;
                return;
            }
        }

        // If damager is a living entity
        if (e.getDamager() instanceof LivingEntity damager && e.getEntity() instanceof LivingEntity target) {
            // Cancel Vanilla event
            e.setCancelled(true);

            // Get used item and eventual CustomItemID
            final EntityEquipment equipment = damager.getEquipment();
            final ItemStack item = equipment == null ? null : equipment.getItemInMainHand();
            Long customItemId = ItemUtils.getCustomItemId(item);

            // Use specified custom attacks if attacker is using custom item. If not, compute standard attack on the vanilla item
            if (customItemId != null) ItemManager.getValueFromId(customItemId).attack.execute(damager, target, item);
            else vanillaAttack.execute(damager, target, item);
        }
    }





    public static void onDeath(@NotNull final EntityDeathEvent e) {
        attacks.remove(e.getEntity().getUniqueId());
    }
}
