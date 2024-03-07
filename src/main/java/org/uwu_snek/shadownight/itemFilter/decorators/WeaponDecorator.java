package org.uwu_snek.shadownight.itemFilter.decorators;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.utils.UtilityClass;




public final class WeaponDecorator extends UtilityClass {
    public static void decorateCustomMelee(final @NotNull ItemStack item, final @NotNull IM manager){
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("TEST WEAPON"));
        item.setItemMeta(meta);
    }


    public static void decorateVanillaMelee(final @NotNull ItemStack item){
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("TEST WEAPON"));
        item.setItemMeta(meta);
    }
}
