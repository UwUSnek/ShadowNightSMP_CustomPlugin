package org.uwu_snek.shadownight.itemFilter;

import org.bukkit.entity.Item;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public final class VolatileItemManager extends UtilityClass {
    public static void onItemDrop(final @NotNull Item item) {
        if (ItemUtils.isVolatile(item.getItemStack())) item.remove();
    }


    public static void onDragEvent(final @NotNull InventoryDragEvent event) {
        final Inventory inv = event.getInventory();
        if (inv.getType() != InventoryType.PLAYER) {
            if (ItemUtils.isVolatile(event.getOldCursor())) {
                for(int i : event.getRawSlots()){
                    if(i < inv.getSize()) Scheduler.run(() -> inv.setItem(i, null));
                }
            }
        }
    }


    public static void onClickEvent(final @NotNull InventoryClickEvent event) {
        final Inventory inv = event.getClickedInventory();
        if (inv != null) {
            if (inv.getType() != InventoryType.PLAYER) {
                // Normal placement
                final InventoryAction a = event.getAction();
                if (
                    a == InventoryAction.PLACE_ONE ||
                    a == InventoryAction.PLACE_SOME ||
                    a == InventoryAction.PLACE_ALL ||
                    a == InventoryAction.SWAP_WITH_CURSOR
                ) {

                    if (ItemUtils.isVolatile(event.getCursor())) {
                        event.setCancelled(true);
                        event.getWhoClicked().setItemOnCursor(null);
                    }
                }
                // Hotbar swap
                else if (event.getClick() == ClickType.NUMBER_KEY) {
                    final PlayerInventory playerInventory = event.getWhoClicked().getInventory();
                    if (ItemUtils.isVolatile(playerInventory.getItem(event.getHotbarButton()))) {
                        event.setCancelled(true);
                        playerInventory.setItem(event.getHotbarButton(), null);
                    }
                }
                // Offhand swap
                else if (event.getClick() == ClickType.SWAP_OFFHAND) {
                    final PlayerInventory playerInventory = event.getWhoClicked().getInventory();
                    if (ItemUtils.isVolatile(playerInventory.getItemInOffHand())) {
                        playerInventory.setItemInOffHand(null);
                    }
                }
            }
            // Shift clicks
            else if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
                if (ItemUtils.isVolatile(event.getCurrentItem())) {
                    event.setCancelled(true);
                    event.getWhoClicked().getInventory().setItem(event.getSlot(), null);
                }
            }
        }
    }
}
