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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.shadownight.plugin.shadownight.utils.utils;


public class StarterKit extends UtilityClass {
    private static final String kit_prefix = "§c[Starter Kit] §r";


    /**
     * Gives the starter kit to the player <player>.
     * @param player The target player
     */
    public static void give(final Player player){
        final PlayerInventory inv = player.getInventory();
        final String lore0 = "§cNotice: this item is part of the starter kit.";
        final String lore1 = "§cIt will be deleted if you remove it from your inventory or die";

        inv.setChestplate(utils.createItemStack(Material.IRON_CHESTPLATE, 1,  kit_prefix + "Iron Chestplate", lore0, lore1));
        inv.addItem      (utils.createItemStack(Material.STONE_AXE,       1,  kit_prefix + "Stone Axe",       lore0, lore1));
        inv.addItem      (utils.createItemStack(Material.STONE_PICKAXE,   1,  kit_prefix + "Stone Pickaxe",   lore0, lore1));
        inv.addItem      (utils.createItemStack(Material.BREAD,           32, kit_prefix + "Bread",           lore0, lore1));
    }


    public static void onJoin(final @NotNull Player player) {
        if(!player.hasPlayedBefore()) give(player);
    }
    public static void onRespawn(final @NotNull Player player, final @NotNull PlayerRespawnEvent.RespawnReason reason) {
        if(reason == PlayerRespawnEvent.RespawnReason.DEATH) give(player);
    }


    /**
     * Checks if an item is blacklisted.
     * @param item The item to check
     * @return True if the item is blacklisted, false otherwise
     */
    public static boolean isBlacklisted(@Nullable final ItemStack item) {
        if (item != null) {
            final ItemMeta meta = item.getItemMeta();
            return (meta != null && Chat.stripColor(meta.getDisplayName()).startsWith(Chat.stripColor(kit_prefix)));
        }
        return false;
    }




    public static void onItemDrop(final @NotNull Item item) {
        if (isBlacklisted(item.getItemStack())) item.remove();
    }


    public static void onDragEvent(final @NotNull InventoryDragEvent event) {
        final Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.PLAYER) {
            if (isBlacklisted(event.getOldCursor())) event.setCancelled(true);
        }
    }


    public static void onClickEvent(final @NotNull InventoryClickEvent event) {
        final Inventory inventory = event.getClickedInventory();
        if (inventory != null) {
            if (inventory.getType() != InventoryType.PLAYER) {
                // Normal placement
                final InventoryAction a = event.getAction();
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
                    final PlayerInventory playerInventory = event.getWhoClicked().getInventory();
                    if (isBlacklisted(playerInventory.getItem(event.getHotbarButton()))) {
                        event.setCancelled(true);
                        playerInventory.setItem(event.getHotbarButton(), null);
                    }
                }
                // Offhand swap
                else if (event.getClick() == ClickType.SWAP_OFFHAND) {
                    final PlayerInventory playerInventory = event.getWhoClicked().getInventory();
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


