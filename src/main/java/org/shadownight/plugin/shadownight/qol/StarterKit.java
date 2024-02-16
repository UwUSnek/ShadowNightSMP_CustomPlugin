package org.shadownight.plugin.shadownight.qol;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.shadownight.plugin.shadownight.utils.utils;


public class StarterKit {
    private static final String kit_prefix = "§c[Starter Kit] §r";


    public static void give(Player player){
        PlayerInventory inv = player.getInventory();
        String lore0 = "§cNotice: this item is part of the starter kit.";
        String lore1 = "§cIt will be deleted if you remove it from your inventory or die";

        ItemStack c = utils.createItemStack(Material.IRON_CHESTPLATE, 1,  kit_prefix + "Iron Chestplate", lore0, lore1);
        ItemStack a = utils.createItemStack(Material.STONE_AXE,       1,  kit_prefix + "Stone Axe",       lore0, lore1);
        ItemStack p = utils.createItemStack(Material.STONE_PICKAXE,   1,  kit_prefix + "Stone Pickaxe",   lore0, lore1);
        ItemStack b = utils.createItemStack(Material.BREAD,           32, kit_prefix + "Bread",           lore0, lore1);

        inv.setChestplate(c);
        inv.addItem(a);
        inv.addItem(p);
        inv.addItem(b);
    }

    public static void onJoin(Player player) {
        if(!player.hasPlayedBefore()) give(player);
    }
    public static void onRespawn(Player player, PlayerRespawnEvent.RespawnReason reason) {
        if(reason == PlayerRespawnEvent.RespawnReason.DEATH) give(player);
    }



    public static boolean isBlacklisted(ItemStack item) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            return (meta != null && Chat.stripColor(meta.getDisplayName()).startsWith(Chat.stripColor(kit_prefix)));
        }
        return false;
    }




    public static void onItemDrop(Item item) {
        if (isBlacklisted(item.getItemStack())) item.remove();
    }


    public static void onDragEvent(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.PLAYER) {
            if (isBlacklisted(event.getOldCursor())) event.setCancelled(true);
        }
    }


    public static void onClickEvent(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory != null) {
            if (inventory.getType() != InventoryType.PLAYER) {
                // Normal placement
                InventoryAction a = event.getAction();
                if (
                    a == InventoryAction.PLACE_ONE ||
                    a == InventoryAction.PLACE_SOME ||
                    a == InventoryAction.PLACE_ALL ||
                    a == InventoryAction.SWAP_WITH_CURSOR
                ) {
                    if (isBlacklisted(event.getCursor())) {
                        event.setCancelled(true);
                        event.getWhoClicked().setItemOnCursor(null);
                    }
                }
                // Hotbar swap
                else if (event.getClick() == ClickType.NUMBER_KEY) {
                    PlayerInventory playerInventory = event.getWhoClicked().getInventory();
                    if (isBlacklisted(playerInventory.getItem(event.getHotbarButton()))) {
                        event.setCancelled(true);
                        playerInventory.setItem(event.getHotbarButton(), null);
                    }
                }
                // Offhand swap
                else if (event.getClick() == ClickType.SWAP_OFFHAND) {
                    PlayerInventory playerInventory = event.getWhoClicked().getInventory();
                    if (isBlacklisted(playerInventory.getItemInOffHand())) {
                        event.setCancelled(true);
                        playerInventory.setItemInOffHand(null);
                    }
                }
            }
            // Shift clicks
            else if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                if (isBlacklisted(event.getCurrentItem())) {
                    event.setCancelled(true);
                    event.getWhoClicked().getInventory().setItem(event.getSlot(), null);
                }
            }
        }
    }
}


