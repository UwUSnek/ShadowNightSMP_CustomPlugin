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




public final class NoAnvilLimit extends UtilityClass {
    public static void onPrepareAnvil(final @NotNull PrepareAnvilEvent event) {
        event.getInventory().setMaximumRepairCost(9999);
    }
}
