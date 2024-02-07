package org.shadownight.plugin.shadownight.items;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.shadownight.plugin.shadownight.ShadowNight;

import java.util.Objects;

public class ItemManager {
    public static NamespacedKey itemIdKey = new NamespacedKey(ShadowNight.plugin, "customItemId");

    public static class CusotmItemId {
        public static final int IRON_SCYTHE      = 0;
        public static final int DIAMOND_SCYTHE   = 1;
        public static final int NETHERITE_SCYTHE = 2;
        public static final int KLAUE_SCYTHE     = 3;
    }


    static public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if(item != null && event.getHand() == EquipmentSlot.HAND && item.getType() != Material.AIR) { // Check if item in being used in the main hand
            PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta(), "Item meta is null").getPersistentDataContainer();
            if(container.has(itemIdKey)) {
                event.setCancelled(true);
                int customItemId = container.get(itemIdKey, PersistentDataType.INTEGER);

                switch (customItemId) {
                    case CusotmItemId.KLAUE_SCYTHE:
                        Scythe.onInteractKlaue(event);
                        // Intentionally falling through
                    case CusotmItemId.IRON_SCYTHE:
                    case CusotmItemId.DIAMOND_SCYTHE:
                    case CusotmItemId.NETHERITE_SCYTHE:
                        Scythe.onInteractNormal(event);
                }
            }
        }
    }


    static public void onAttack(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item.getType() != Material.AIR) {
                PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta(), "Item meta is null").getPersistentDataContainer();
                if (container.has(itemIdKey)) {
                    int customItemId = container.get(itemIdKey, PersistentDataType.INTEGER);

                    switch (customItemId) {
                        case CusotmItemId.KLAUE_SCYTHE:
                        case CusotmItemId.IRON_SCYTHE:
                        case CusotmItemId.DIAMOND_SCYTHE:
                        case CusotmItemId.NETHERITE_SCYTHE:
                            Scythe.onAttack(event);
                            break;
                    }
                }
            }
        }
    }
}
