package org.shadownightsmp.plugin.shadownightsmp.QOL;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.shadownightsmp.plugin.shadownightsmp.utils.utils;

import java.util.ArrayList;


public class StarterKit {
    private static final String kit_prefix = "§c[Starter Kit] §r";


    public static void give(Player player){
        PlayerInventory inv = player.getInventory();
        ItemStack c = new ItemStack(Material.IRON_CHESTPLATE);
        ItemStack a = new ItemStack(Material.STONE_AXE);
        ItemStack p = new ItemStack(Material.STONE_PICKAXE);
        ItemStack b = new ItemStack(Material.BREAD, 32);

        ArrayList<String> lore = new ArrayList<>();
        lore.add("§cNotice: this item is part of the starter kit.");
        lore.add("§cIt will be deleted if you remove it from your inventory or die");
        ItemMeta meta_c = c.getItemMeta(); meta_c.setLore(lore); meta_c.setDisplayName(kit_prefix + "Iron Chestplate"); c.setItemMeta(meta_c);
        ItemMeta meta_a = a.getItemMeta(); meta_a.setLore(lore); meta_a.setDisplayName(kit_prefix + "Stone Axe");       a.setItemMeta(meta_a);
        ItemMeta meta_p = p.getItemMeta(); meta_p.setLore(lore); meta_p.setDisplayName(kit_prefix + "Stone Pickaxe");   p.setItemMeta(meta_p);
        ItemMeta meta_b = p.getItemMeta(); meta_b.setLore(lore); meta_b.setDisplayName(kit_prefix + "Bread");           b.setItemMeta(meta_b);

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



    public static boolean isBLacklisted(ItemStack item) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            return (meta != null && utils.stripColor(meta.getDisplayName()).startsWith(utils.stripColor(kit_prefix)));
        }
        return false;
    }




    public static void onItemDrop(Item item) {
        if (isBLacklisted(item.getItemStack())) item.remove();
    }


    public static void onDragEvent(InventoryDragEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.PLAYER) {
            if (isBLacklisted(event.getOldCursor())) event.setCancelled(true);
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
                    if (isBLacklisted(event.getCursor())) {
                        event.setCancelled(true);
                        event.getWhoClicked().setItemOnCursor(null);
                    }
                }
                // Hotbar swap
                else if (event.getClick() == ClickType.NUMBER_KEY) {
                    PlayerInventory playerInventory = event.getWhoClicked().getInventory();
                    if (isBLacklisted(playerInventory.getItem(event.getHotbarButton()))) {
                        event.setCancelled(true);
                        playerInventory.setItem(event.getHotbarButton(), null);
                    }
                }
                // Offhand swap
                else if (event.getClick() == ClickType.SWAP_OFFHAND) {
                    PlayerInventory playerInventory = event.getWhoClicked().getInventory();
                    if (isBLacklisted(playerInventory.getItemInOffHand())) {
                        event.setCancelled(true);
                        playerInventory.setItemInOffHand(null);
                    }
                }
            }
            // Shift clicks
            else if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                if (isBLacklisted(event.getCurrentItem())) {
                    event.setCancelled(true);
                    event.getWhoClicked().getInventory().setItem(event.getSlot(), null);
                }
            }
        }
    }
}


