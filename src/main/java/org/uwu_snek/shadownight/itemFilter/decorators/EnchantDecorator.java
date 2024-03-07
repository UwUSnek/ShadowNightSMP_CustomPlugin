package org.uwu_snek.shadownight.itemFilter.decorators;

import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;




public final class EnchantDecorator extends UtilityClass {
    public static void decorateBook(final @NotNull ItemStack item){
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text("TEST"));
        item.setItemMeta(meta);
    }
}
