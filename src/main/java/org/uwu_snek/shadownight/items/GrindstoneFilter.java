package org.uwu_snek.shadownight.items;

import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.inventory.GrindstoneInventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.Objects;




public final class GrindstoneFilter extends UtilityClass {
    public static void onPrepareGrindstone(@NotNull final PrepareGrindstoneEvent event) {
        GrindstoneInventory inv = event.getInventory();
        ItemStack item1 = inv.getUpperItem();
        ItemStack item2 = inv.getLowerItem();

        if(item1 != null && item2 != null && item1.getType() == item2.getType()) {
            Long id1 = ItemUtils.getCustomItemId(inv.getUpperItem());
            Long id2 = ItemUtils.getCustomItemId(inv.getLowerItem());
            if(!(id1 == null && id2 == null) && !Objects.equals(id1, id2)) event.setResult(null);
        }
    }
}
