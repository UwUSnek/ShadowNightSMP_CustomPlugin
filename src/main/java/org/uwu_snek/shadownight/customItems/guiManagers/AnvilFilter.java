package org.uwu_snek.shadownight.customItems.guiManagers;

import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.Objects;




public final class AnvilFilter extends UtilityClass {
    public static void onPrepareAnvil(final @NotNull PrepareAnvilEvent event) {
        final AnvilInventory inv = event.getInventory();
        final ItemStack item1 = inv.getFirstItem();
        final ItemStack item2 = inv.getSecondItem();

        if(item1 != null && item2 != null && item1.getType() == item2.getType()) {
            final Long id1 = ItemUtils.getCustomItemId(inv.getFirstItem());
            final Long id2 = ItemUtils.getCustomItemId(inv.getSecondItem());
            if(!(id1 == null && id2 == null) && !Objects.equals(id1, id2)) event.setResult(null);
        }
    }
}
