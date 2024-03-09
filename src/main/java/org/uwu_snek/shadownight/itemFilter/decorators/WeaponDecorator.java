package org.uwu_snek.shadownight.itemFilter.decorators;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.attackOverride.CustomDamage;
import org.uwu_snek.shadownight.items.Ability;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.items.VanillaProvider;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;




public final class WeaponDecorator extends UtilityClass {
    private static final TextColor COLOR_violet = TextColor.color(0xAAAAFF);
    private static final TextColor COLOR_purple = TextColor.color(0xFFAAFF);
    private static final TextColor COLOR_gray = TextColor.color(0xAAAAAA);
    private static final DecimalFormat valueFormat = new DecimalFormat("############.##");




    private static List<TextComponent> generateAbilityDescription(final @NotNull Ability ability, final @NotNull String activation) {
        final List<TextComponent> r = new ArrayList<>();
        r.add(Component.empty());

        // Name and activation mode
        r.add(
            Component.text("Ability: " + ability.getName())
            .color(COLOR_violet)
            .decoration(TextDecoration.ITALIC, false)
            .append(
                Component.text(" (" + activation + ")")
                .color(COLOR_gray)
                .decoration(TextDecoration.ITALIC, false)
            )
        );

        // Description
        for(String l : ability.getDescription()) r.add( // This prevents parts of the converted string from reverting to the default style
            Component.empty().color(COLOR_gray).append(
            Component.text("  " + l)
            .color(COLOR_gray)
            .decoration(TextDecoration.ITALIC, false)
        ));

        // Cooldown
        r.add(
            Component.text("  Cooldown: " + (ability.getCooldown() == 0 ? "none" : valueFormat.format((float)ability.getCooldown() / 1000) + "s"))
            .color(COLOR_gray)
            .decoration(TextDecoration.ITALIC, false)
        );

        return r;
    }




    private static List<TextComponent> generateAbilityLore(final @NotNull IM manager) {
        final List<TextComponent> r = new ArrayList<>();
        if(manager.abilityL != null) r.addAll(generateAbilityDescription(manager.abilityL, "lclick"));
        if(manager.abilityLS != null && (manager.abilityL == null || manager.abilityLS != manager.abilityL)) r.addAll(generateAbilityDescription(manager.abilityLS, "sneak + lclick"));
        if(manager.abilityR != null) r.addAll(generateAbilityDescription(manager.abilityR, "rclick"));
        if(manager.abilityRS != null && (manager.abilityR == null || manager.abilityRS != manager.abilityR)) r.addAll(generateAbilityDescription(manager.abilityRS, "sneak + rclick"));
        return r;
    }



    private static TextComponent decorateStat(final @NotNull String name, final double value, final @Nullable String unit) {
        return Component.empty().decoration(TextDecoration.ITALIC, false)
            .append(Component.text(name + ": ")).color(COLOR_violet)
            .append(Component.text(valueFormat.format(value) + (unit == null ? "" : unit)).color(COLOR_gray))
        ;
    }

    private static List<TextComponent> generateStatsLore(final @NotNull IM manager) {
        final List<TextComponent> r = new ArrayList<>();
        r.add(decorateStat("DMG", manager.getHitDamage(),      null));
        r.add(decorateStat("ATS", manager.getAttackSpeed(),    "s"));
        r.add(decorateStat("KB", manager.getHitKbMultiplier(), "x"));
        return r;
    }
    private static List<TextComponent> generateStatsLore(final @NotNull ItemStack item) {
        final List<TextComponent> r = new ArrayList<>();
        r.add(decorateStat("DMG", VanillaProvider.getDamage(item.getType()), null));
        r.add(decorateStat("ATS", VanillaProvider.getAts(item.getType()), "s"));
        r.add(decorateStat("KB", 1d, "x"));
        return r;
    }





    private static List<TextComponent> generateEnchantsLore(final @NotNull ItemStack item) {
        final List<TextComponent> r_list = new ArrayList<>(List.of(
            Component.empty(),
            Component.text("Enchantments:").color(COLOR_purple).decoration(TextDecoration.ITALIC, false)
        ));
        StringBuilder r = new StringBuilder("  ");
        int n = 0;

        // For each enchant on the item
        for(Map.Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()){
            if(n > 0) {
                r.append(", ");

                // Caveman word wrapping technology
                if(n % 3 == 0) {
                    r_list.add(Component.text(r.toString()).color(COLOR_gray).decoration(TextDecoration.ITALIC, false));
                    r = new StringBuilder("  ");
                }
            }

            // Add its display name
            r.append(PlainTextComponentSerializer.plainText().serialize(e.getKey().displayName(e.getValue())));
            ++n;
        }
        r_list.add(Component.text(r.toString()).color(COLOR_gray).decoration(TextDecoration.ITALIC, false));

        // Return the lines or null if the item is not enchanted
        return n == 0 ? null : r_list;
    }




    public static void decorateCustomMelee(final @NotNull ItemStack item, final @NotNull IM manager){
        List<TextComponent> lore = new ArrayList<>(generateStatsLore(manager));
        utils.addAllIfNotNull(lore, generateEnchantsLore(item));
        lore.addAll(generateAbilityLore(manager));
        lore.add(Component.empty());
        ItemUtils.setLore(item, lore);
    }


    public static void decorateVanillaMelee(final @NotNull ItemStack item){
        List<TextComponent> lore = new ArrayList<>(generateStatsLore(item));
        utils.addAllIfNotNull(lore, generateEnchantsLore(item));
        lore.add(Component.empty());
        ItemUtils.setLore(item, lore);
    }






    public static void decorateCustomRanged(final @NotNull ItemStack item, final @NotNull IM manager){
        //ItemMeta meta = item.getItemMeta();
        //meta.displayName(Component.text("TEST WEAPON"));
        //item.setItemMeta(meta);
    }


    public static void decorateVanillaRanged(final @NotNull ItemStack item){
        //ItemMeta meta = item.getItemMeta();
        //meta.displayName(Component.text("TEST WEAPON"));
        //item.setItemMeta(meta);
    }
}
