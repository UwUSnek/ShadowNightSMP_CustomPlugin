package org.uwu_snek.shadownight.customItems.itemFilter.decorators;

import kotlin.collections.ArrayDeque;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated._enchantment_overrides;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;




public final class EnchantDecorator extends UtilityClass {
    private static final Map<NamespacedKey, Function<Integer, String>> enchantmentDescriptions = Map.ofEntries(
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "reeling"),
            (lvl) -> "Pulls damaged entities towards you."
        ),

        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "aqua_affinity"),
            (lvl) -> "Lets you mine at normal speed when underwater."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "bane_of_arthropods"),
            (lvl) -> "Increases damage dealt to arthropods by " + Decorator.formatValue(Decorator.valueFormat.format(2.5 * lvl)) + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "blast_protection"),
            (lvl) -> "Reduces explosion damage by " + Decorator.formatValue(8 * lvl + "%") + " and explosion knockback by " + Decorator.formatValue(15 * lvl + "%") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "channeling"),
            (lvl) -> "Allows tridents to summon lightning bolts by hitting an entity during a thunderstorm."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "binding_curse"),
            (lvl) -> "Makes the item impossible to remove from an armor slot."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "vanishing_curse"),
            (lvl) -> "Makes the item disappear on death."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "depth_strider"),
            (lvl) -> "Reduces the amount of speed reduction when underwater by " + Decorator.formatValue(Decorator.valueFormat.format(1d / 3d * lvl) + "%") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "efficiency"),
            (lvl) -> "Increases a tool's mining speed by " + Decorator.formatValue(lvl * lvl + 1) + "."
            //TODO https://minecraft.wiki/w/Breaking#Speed make this stat visible
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "feather_falling"),
            (lvl) -> "Reduces fall damage by " + Decorator.formatValue(12 * lvl + "%") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "fire_aspect"),
            (lvl) -> "Sets damaged entities on fire for " + Decorator.formatValue(4 * lvl + "s") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "fire_protection"),
            (lvl) -> "Reduces damage from fire and lava by " + Decorator.formatValue(8 * lvl + "%") + " and fire duration by " + Decorator.formatValue(15 * lvl + "%") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "flame"),
            (lvl) -> "Lights your arrows on fire, allowing them to ignite TNTs, Campfires and Candles.\n" +
                     "Hit entities are set on fire for " + Decorator.formatValue(5 + "s") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "fortune"),
            (lvl) -> "Increases drops from mineral ores and metal ores by " +
                     Decorator.formatValue(Decorator.valueFormat.format(((1d / (lvl + 2d)) + ((lvl + 1d) / 2d)) * 100d) + "%") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "frost_walker"),
            (lvl) -> "Replaces still water in a " + Decorator.formatValue(2 + lvl + " blocks") + " radius with Frosted Ice.\n" +
                     "Protects the wearer from all damage caused by Magma Blocks and Campfires."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "impaling"),
            (lvl) -> "Increases damage dealt to aquatic mobs by " + Decorator.formatValue(Decorator.valueFormat.format(2.5d * lvl)) + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "infinity"),
            (lvl) -> "Allows you to shoot arrows without consuming them."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "knockback"),
            (lvl) -> "Pushes damaged entities away."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "looting"),
            (lvl) -> "Increases the maximum number of items of common mob drops by " + Decorator.formatValue(lvl * 100 + "%") + ".\n" +
                     "Increases the chance of rare mob drops and equipment drops by " + Decorator.formatValue(lvl + "%") + ".\n" +
                     // This 2nd attempts is probably too technical and negligible to be included in the user-friendly description
                     //"Rare mob drops have an additional " + Decorator.formatValue(Decorator.valueFormat.format(lvl / (lvl + 1d))) + " chance of being obtained if the first attempt failed.\n" +
                     "This enchantment doesn't increase drops from Withers, Iron Golems, Cats, Fishes and Snow Golems. It also doesn't increase the amount of Wool, Sponges, Sculk Catalysts or Totems of Undying."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "loyalty"),
            (lvl) -> "Makes a trident return to its thrower.\n" +
                     "Be careful: this enchantment isn't powerful enough to make tridents return from The Void."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "luck_of_the_sea"),
            (lvl) -> "Increases the chance of fishing up a treasure (instead of junk) by " + Decorator.formatValue(Decorator.valueFormat.format(2.1d * lvl) + "%") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "lure"),
            (lvl) -> "Reduces lure time by " + Decorator.formatValue(5 * lvl + "s") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "mending"),
            (lvl) -> "Repairs the item using Experience Orbs."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "multishot"),
            (lvl) -> "Makes a crossbow shoot 3 arrows instead of 1, without consuming additional items."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "piercing"),
            (lvl) -> "Allows your arrows to pierce through " + Decorator.formatValue(lvl) + " entities, ignore shields and be retrieved afterwards.\n" +
                     "This enchantment doesn't affect Firework Rockets"
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "power"),
            (lvl) -> "Increases arrow damage dealt to entities by " + Decorator.formatValue(25 * (lvl + 1) + "%") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "projectile_protection"),
            (lvl) -> "Reduces projectile damage by " + Decorator.formatValue(8 * lvl + "%") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "protection"),
            (lvl) -> "Reduces received damage by " + Decorator.formatValue(4 * lvl + "%") + ".\n" +
                     "This enchantment doesn't protect from damage caused by hunger, Sonic Boom attacks or The Void"
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "punch"),
            (lvl) -> "Makes your arrows push damaged entities away."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "quick_charge"),
            (lvl) -> "Reduces draw time by " + Decorator.formatValue(Decorator.valueFormat.format(0.25d * lvl) + "s") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "respiration"),
            (lvl) -> "Grants a " + Decorator.formatValue(Decorator.valueFormat.format((double)lvl / (lvl + 1d) * 100d) + "%") + " chance to not decrease your air level when underwater."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "riptide"),
            (lvl) -> "Allows you to launch yourself by throwing your trident.\n" +
                     "Riptide tridents can only be thrown while standing in water or being exposed to rain."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "sharpness"),
            (lvl) -> "Increases damage dealt to entities by " + Decorator.formatValue(Decorator.valueFormat.format(0.5d * lvl + 0.5d)) + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "silk_touch"),
            (lvl) -> "Allows you to obtain delicate items that don't normally drop when mined."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "smite"),
            (lvl) -> "Increases damage dealt to undead mobs by " + Decorator.formatValue(Decorator.valueFormat.format(2.5d * lvl)) + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "soul_speed"),
            // TODO add a measurement unit to the speed value
            (lvl) -> "Removes the movement speed reduction of Soul Sand and increases walking speed on Soul Sand and Soul Soil by " +
                     Decorator.formatValue(Decorator.valueFormat.format(lvl * 0.105d + 1.3d)) + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "sweeping"),
            (lvl) -> { throw new RuntimeException("Requested enchantment description for blacklisted enchant Sweeping Edge"); }
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "swift_sneak"),
            (lvl) -> "Increases walking speed while sneaking by " + Decorator.formatValue(50 * lvl + "%") + "."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "thorns"),
            (lvl) -> "Grants a " + Decorator.formatValue(15 * lvl + "%") + " chance of dealing 1-4 damage to any entity that hits you."
        ),
        new AbstractMap.SimpleEntry<NamespacedKey, Function<Integer, String>>(
            new NamespacedKey(NamespacedKey.MINECRAFT, "unbreaking"),
            (lvl) -> "Grants a " + Decorator.formatValue(Decorator.valueFormat.format(1d - (1d / (lvl + 1d))) + "%") + " chance of not damaging a tool when using it." +
                     "Grants a " + Decorator.formatValue(Decorator.valueFormat.format(1d - (1d / (60d + (40 / (lvl + 1d))))) + "%") + " chance of not damaging an armor piece when taking damage."
        )
    );



    private static @NotNull List<Component> generateEnchantLore(final @NotNull Enchantment enchant, final int lvl) {
        final List<Component> r = new ArrayDeque<>();
        r.add(
            Component.text(_enchantment_overrides.getRealDisplayName(enchant.translationKey(), lvl))
            .color(enchant.isCursed() ? Decorator.COLOR_red : Decorator.COLOR_purple)
            .decoration(TextDecoration.ITALIC, false)
        );
        r.addAll(Decorator.formatParagraph(enchantmentDescriptions.get(enchant.getKey()).apply(lvl), 2, Decorator.COLOR_gray));
        return r;
    }



    public static void decorateBook(final @NotNull ItemStack item){
        final List<Component> lore = new ArrayDeque<>();
        for(Map.Entry<Enchantment, Integer> e : ((EnchantmentStorageMeta)item.getItemMeta()).getStoredEnchants().entrySet()) {
            lore.add(Component.empty());
            lore.addAll(generateEnchantLore(e.getKey(), e.getValue()));
        }
        lore.add(Component.empty());
        ItemUtils.setLore(item, lore);
    }
}
