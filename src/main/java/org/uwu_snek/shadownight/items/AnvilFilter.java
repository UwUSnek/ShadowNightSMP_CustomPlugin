package org.uwu_snek.shadownight.items;

import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.chatManager.ChatManager;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.utils;

import java.util.Objects;
import java.util.logging.Level;




public final class AnvilFilter extends UtilityClass {
    public static void onPrepareAnvil(@NotNull final PrepareAnvilEvent event) {
        AnvilInventory inv = event.getInventory();
        ItemStack item1 = inv.getFirstItem();
        ItemStack item2 = inv.getSecondItem();

        if(item1 != null && item2 != null && item1.getType() == item2.getType()) {
            Long id1 = ItemUtils.getCustomItemId(inv.getFirstItem());
            Long id2 = ItemUtils.getCustomItemId(inv.getSecondItem());
            utils.log(Level.WARNING, id1 == null ? "null" : id1.toString());
            utils.log(Level.WARNING, id2 == null ? "null" : id2.toString());
            if(!(id1 == null && id2 == null) && !Objects.equals(id1, id2)) event.setResult(null);
        }
    }
}
