package org.uwu_snek.shadownight.qol;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;




public final class MvpAnvilFormatting extends UtilityClass {
    public static void onPrepareAnvil(final @NotNull PrepareAnvilEvent event) {
        // Skip if player doesn't have Mvp
        for(HumanEntity e : event.getViewers()) if(!e.hasPermission("group.mvp")) return;


        // If result is not null
        final AnvilInventory inv = event.getInventory();
        final ItemStack r = inv.getResult();
        if(r != null) {
            // Get Vanilla display name output and format it
            final ItemMeta meta = r.getItemMeta();
            final String itemName = ItemUtils.getPlainItemName(r);

            // Paste a new copy of the item with the modified name (block output if it doesn't contain visible characters)
            if(!ChatUtils.stripColor(itemName).isEmpty()) {
                meta.displayName(ChatUtils.translateColorToComponent(itemName));
                r.setItemMeta(meta);
                event.setResult(r);
            }

            // No item output if the name doesn't contain any visible character
            else event.setResult(null);
        }
    }
}
