package org.shadownight.plugin.shadownight.items;

import org.bukkit.damage.DamageSource;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;

import java.util.HashMap;
import java.util.UUID;




/**
 * This class is used to manage player attacks because Bukkit's system is atrocious.
 * Allows for custom attacks and overrides vanilla attacks.
 * Provides actually useful data for the death message manager.
 */
public final class AttackOverride extends UtilityClass {
    public static class AttackData {
        public Player attacker;
        public ItemStack usedItem;
        public Long time;
    }
    public static HashMap<UUID, Pair<AttackData, AttackData>> attacks;



    public static void onAttack(@NotNull final EntityDamageByEntityEvent e) {
        if(e.getDamager() instanceof Player damager && e.getEntity() instanceof LivingEntity target) {
            e.setCancelled(true);
            target.damage(10);
            final ItemStack item = damager.getInventory().getItemInMainHand();
        }
    }
}
