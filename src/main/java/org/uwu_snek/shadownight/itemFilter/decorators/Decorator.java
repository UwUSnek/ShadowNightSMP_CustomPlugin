package org.uwu_snek.shadownight.itemFilter.decorators;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.items.IM_MeleeWeapon;
import org.uwu_snek.shadownight.items.IM_RangedWeapon;
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;




public final class Decorator extends UtilityClass {
    public static void decorate(final @NotNull ItemStack item, final boolean force){


        // Enchantment books
        if(item.getType() == Material.ENCHANTED_BOOK) {
            item.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
            EnchantDecorator.decorateBook(item);
        }


        else {
            final Long customId = ItemUtils.getCustomItemId(item);
            if(customId != null) {
                item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
                final IM manager = ItemManager.getValueFromId(customId);

                // Custom melee weapons
                if(manager instanceof IM_MeleeWeapon) {
                    WeaponDecorator.decorateCustomMelee(item, manager);
                }

                // Vanilla ranged weapons
                else if(manager instanceof IM_RangedWeapon) {
                    WeaponDecorator.decorateCustomRanged(item, manager);
                }
            }
            else {
                final Material itemType = item.getType();


                // Vanilla melee weapons
                if(
                    Tag.ITEMS_SWORDS.isTagged(itemType) ||
                    Tag.ITEMS_AXES.isTagged(itemType) ||
                    Tag.ITEMS_PICKAXES.isTagged(itemType) ||
                    Tag.ITEMS_SHOVELS.isTagged(itemType) ||
                    Tag.ITEMS_HOES.isTagged(itemType) ||
                    itemType == Material.TRIDENT
                ) {
                    item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
                    WeaponDecorator.decorateVanillaMelee(item);
                }


                // Vanilla ranged weapons
                else if(
                    itemType == Material.BOW ||
                    itemType == Material.CROSSBOW
                ) {
                    item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
                    WeaponDecorator.decorateVanillaRanged(item);
                }
            }
        }
    }
}
