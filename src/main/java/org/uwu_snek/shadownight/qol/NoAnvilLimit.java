package org.uwu_snek.shadownight.qol;

import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;




public final class NoAnvilLimit extends UtilityClass {
    public static void onPrepareAnvil(final @NotNull PrepareAnvilEvent event) {
        event.getInventory().setMaximumRepairCost(9999);
    }
}
