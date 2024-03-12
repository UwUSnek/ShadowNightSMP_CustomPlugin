package org.uwu_snek.shadownight.customItems.itemFilter.decorators;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.utils;

import java.util.ArrayList;
import java.util.List;




public final class ArmorDecorator extends UtilityClass {
    public static void decorateVanilla(final @NotNull ItemStack item) {
        final List<TextComponent> lore = new ArrayList<>();
        utils.addAllIfNotNull(lore, Decorator.generateEnchantsLore(item));
        lore.add(Component.empty());
        ItemUtils.setLore(item, lore);
    }
}
