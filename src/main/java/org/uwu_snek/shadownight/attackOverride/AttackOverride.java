package org.uwu_snek.shadownight.attackOverride;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_Standard;
import org.uwu_snek.shadownight.customItems.IM;
import org.uwu_snek.shadownight.customItems.IM_MeleeWeapon;
import org.uwu_snek.shadownight.customItems.ItemManager;
import org.uwu_snek.shadownight.utils.Rnd;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.*;




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
     * @param event The event to replace
     */
    public static void customAttack(final @NotNull EntityDamageByEntityEvent event) {
        // Use Vanilla creeper explosions
        if (event.getDamager().getType() == EntityType.CREEPER) return;

        // Skip mirror events
        CircularFifoQueue<AttackData> attackQueue = attacks.get(event.getEntity().getUniqueId());
        if (attackQueue != null) {
            AttackData lastAttack = attackQueue.get(attackQueue.size() == 1 ? 0 : 1);
            if (lastAttack.damager == event.getDamager() && !lastAttack.mirrored) {
                lastAttack.mirrored = true;
                return;
            }
        }

        // If damager is a living entity
        if (event.getDamager() instanceof LivingEntity damager && event.getEntity() instanceof LivingEntity target) {
            // Cancel Vanilla event
            event.setCancelled(true);

            // Get used item and eventual CustomItemID
            final EntityEquipment equipment = damager.getEquipment();
            final ItemStack item = equipment == null ? null : equipment.getItemInMainHand();
            Long customItemId = ItemUtils.getCustomItemId(item);

            // Use specified custom attacks if attacker is using custom item. If not, compute standard attack on the vanilla item
            if (customItemId != null) {
                IM itemManager = ItemManager.getValueFromId(customItemId);
                if(itemManager.checkCooldown(damager.getUniqueId(), customItemId)) {
                    itemManager.attack.execute(damager, target, damager.getLocation(), item);
                    if(itemManager instanceof IM_MeleeWeapon) {
                        ItemUtils.damageItem(damager, item, 1);
                    }
                }
            }
            else {
                vanillaAttack.execute(damager, target, damager.getLocation(), item);
                if(item != null) {
                    final Material itemType = item.getType();
                    if(
                        Tag.ITEMS_SWORDS.isTagged(itemType) ||
                        itemType == Material.TRIDENT
                    ) {
                        ItemUtils.damageItem(damager, item, 1);
                    }
                    else if(
                        Tag.ITEMS_PICKAXES.isTagged(itemType) ||
                        Tag.ITEMS_SHOVELS.isTagged(itemType) ||
                        Tag.ITEMS_AXES.isTagged(itemType) ||
                        Tag.ITEMS_HOES.isTagged(itemType)
                    ) {
                        ItemUtils.damageItem(damager, item, 2);
                    }
                }
            }
        }
    }





    public static void onDeath(final @NotNull EntityDeathEvent e) {
        attacks.remove(e.getEntity().getUniqueId());
    }
}
