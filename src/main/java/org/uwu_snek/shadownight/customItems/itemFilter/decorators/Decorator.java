package org.uwu_snek.shadownight.customItems.itemFilter.decorators;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight._generated._build_counter;
import org.uwu_snek.shadownight._generated._enchantment_overrides;
import org.uwu_snek.shadownight.customItems.IM;
import org.uwu_snek.shadownight.customItems.IM_MeleeWeapon;
import org.uwu_snek.shadownight.customItems.IM_RangedWeapon;
import org.uwu_snek.shadownight.customItems.ItemManager;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




public final class Decorator extends UtilityClass {
    // Colors
    public static final TextColor COLOR_violet = TextColor.color(0xAAAAFF);
    public static final TextColor COLOR_purple = TextColor.color(0xFFAAFF);
    public static final TextColor COLOR_gray = TextColor.color(0xAAAAAA);
    public static final TextColor COLOR_red = TextColor.color(0xFF4444);

    // Value formats
    public static final DecimalFormat valueFormat = new DecimalFormat("############.##");
    public static @NotNull String formatValue(final @NotNull String value) { return "§a" + value + "§7"; }
    public static @NotNull String formatValue(final @NotNull Number value) { return formatValue(value.toString()); }

    // Wrapping
    public static final int maxLineLen = 150;
    /**
     * Splits a string into <Decorator.maxLineLen> characters long lines and converts each string into a TextComponent.
     * Lines are indented using <indentation> spaces, colored with the color <color> and their italic is set to false.
     * @param s The string to split and convert
     * @param indentation The amount of indentation in characters
     * @param color The output color
     * @return A list of TextComponents each containing a line
     */
    public static @NotNull List<@NotNull TextComponent> formatParagraph(final @NotNull String s, final int indentation, final @NotNull TextColor color) {
        final List<TextComponent> r = new ArrayList<>();
        final String prefix = StringUtils.repeat(" ", indentation);
        for(String l : ChatUtils.wrap(s, maxLineLen).split("\n")) r.add(
            Component.empty().color(color).append( // This prevents parts of the converted string from reverting to the default style
            Component.text(prefix + l, color)
            .decoration(TextDecoration.ITALIC, false)
        ));
        ChatUtils.wrap(s, maxLineLen);
        return r;
    }





    public static List<TextComponent> generateEnchantsLore(final @NotNull ItemStack item) {
        // Title
        final List<TextComponent> r = new ArrayList<>(List.of(
            Component.empty(),
            Component.text("Enchantments:", Decorator.COLOR_purple).decoration(TextDecoration.ITALIC, false)
        ));


        // Enchantment list
        StringBuilder list = new StringBuilder();
        int n = 0;
        for(Map.Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()) {
            if(n > 0) list.append(", ");
            list.append(_enchantment_overrides.getRealDisplayName(e.getKey().translationKey(), e.getValue()));
            ++n;
        }
        r.addAll(Decorator.formatParagraph(list.toString(), 2, Decorator.COLOR_gray));


        // Return the lines or null if the item is not enchanted
        return n == 0 ? null : r;
    }






    private static final NamespacedKey decorationVersionKey = new NamespacedKey(ShadowNight.plugin, "decoration_version");
    public static void decorate(final @NotNull ItemStack item, final boolean force){

        // Check and update the version and return if the item is up-to-date
        final ItemMeta meta = item.getItemMeta();
        final PersistentDataContainer container = meta.getPersistentDataContainer();
        final Integer version = container.get(decorationVersionKey, PersistentDataType.INTEGER);
        if(!force) {
            if (version != null && version == _build_counter.getCurrentValue()) return;
        }
        if(version == null || version != _build_counter.getCurrentValue()) {
            container.set(decorationVersionKey, PersistentDataType.INTEGER, _build_counter.getCurrentValue());
            item.setItemMeta(meta);
        }



        // Enchantment books
        if(item.getType() == Material.ENCHANTED_BOOK) {
            item.addItemFlags(ItemFlag.HIDE_ITEM_SPECIFICS);
            EnchantDecorator.decorateBook(item);
        }


        else {
            final Long customId = ItemUtils.getCustomItemId(item);
            if(customId != null) {
                final IM manager = ItemManager.getValueFromId(customId);

                // Custom melee weapons
                if(manager instanceof IM_MeleeWeapon)  {
                    WeaponDecorator.decorateCustomMelee(item,  manager);
                }

                // Custom ranged weapons
                else if(manager instanceof IM_RangedWeapon) {
                    WeaponDecorator.decorateCustomRanged(item, manager);
                }

                //TODO check for custom armor and utility items here

                // Hide Vanilla info from decorated items
                else return;
                item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
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
                    WeaponDecorator.decorateVanillaMelee(item);
                }


                // Vanilla ranged weapons
                else if(
                    itemType == Material.BOW ||
                    itemType == Material.CROSSBOW
                ) {
                    WeaponDecorator.decorateVanillaRanged(item);
                }


                // Vanilla armor
                else if(
                    Tag.ITEMS_TRIMMABLE_ARMOR.isTagged(itemType) ||
                    itemType == Material.TURTLE_HELMET ||
                    itemType == Material.ELYTRA
                ) {
                    //item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
                    ArmorDecorator.decorateVanilla(item);
                }


                // Vanilla utility items
                else if(
                    itemType == Material.FISHING_ROD ||
                    itemType == Material.SHEARS ||
                    itemType == Material.FLINT_AND_STEEL ||
                    itemType == Material.CARROT_ON_A_STICK ||
                    itemType == Material.WARPED_FUNGUS_ON_A_STICK ||
                    itemType == Material.SHIELD ||
                    itemType == Material.BRUSH
                ) {
                    UtilityDecorator.decorateVanilla(item);
                }


                // Hide Vanilla info from decorated items
                else return;
                item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE);
            }
        }
    }
}
