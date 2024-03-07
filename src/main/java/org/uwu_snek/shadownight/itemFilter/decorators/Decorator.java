package org.uwu_snek.shadownight.itemFilter.decorators;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.items.IM_MeleeWeapon;
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;




public final class Decorator extends UtilityClass {
    public static void decorate(final @NotNull ItemStack item){


        if(item.getType() == Material.ENCHANTED_BOOK) EnchantDecorator.decorateBook(item);
        else {
            final Long customId = ItemUtils.getCustomItemId(item);
            if(customId != null) {
                final IM manager = ItemManager.getValueFromId(customId);
                if(manager instanceof IM_MeleeWeapon) {
                    WeaponDecorator.decorateCustomMelee(item, manager);
                }
            }
            else {
                final Material itemType = item.getType();
                if(
                    Tag.ITEMS_SWORDS.isTagged(itemType) ||
                    Tag.ITEMS_AXES.isTagged(itemType) ||
                    Tag.ITEMS_PICKAXES.isTagged(itemType) ||
                    Tag.ITEMS_SHOVELS.isTagged(itemType) ||
                    Tag.ITEMS_HOES.isTagged(itemType)
                ) {
                    WeaponDecorator.decorateVanillaMelee(item);
                }
            }
        }
    }
}
