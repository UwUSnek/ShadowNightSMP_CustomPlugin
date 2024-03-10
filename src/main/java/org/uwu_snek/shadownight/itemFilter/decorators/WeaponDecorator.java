package org.uwu_snek.shadownight.itemFilter.decorators;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.items.Ability;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.items.VanillaProvider;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;




public final class WeaponDecorator extends UtilityClass {




    private static List<TextComponent> generateAbilityDescription(final @NotNull Ability ability, final @NotNull String activation) {
        final List<TextComponent> r = new ArrayList<>();
        r.add(Component.empty());

        // Name and activation mode
        r.add(
            Component.text("Ability: " + ability.getName(), Decorator.COLOR_violet)
            .decoration(TextDecoration.ITALIC, false)
            .append(
                Component.text(" (" + activation + ")", Decorator.COLOR_gray)
                .decoration(TextDecoration.ITALIC, false)
            )
        );

        // Description
        r.addAll(Decorator.formatParagraph(ability.getDescription(), 2, Decorator.COLOR_gray));

        // Cooldown
        r.add(
            Component.text("  Cooldown: " + (ability.getCooldown() == 0 ? "none" : Decorator.valueFormat.format((float)ability.getCooldown() / 1000) + "s"), Decorator.COLOR_gray)
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
            .append(Component.text(name + ": ", Decorator.COLOR_violet))
            .append(Component.text(Decorator.valueFormat.format(value) + (unit == null ? "" : unit), Decorator.COLOR_gray))
        ;
    }

    private static List<TextComponent> generateStatsLore(final @NotNull IM manager) {
        final List<TextComponent> r = new ArrayList<>();
        r.add(decorateStat("Damage", manager.getHitDamage(),      null));
        r.add(decorateStat("Attack speed", manager.getAttackSpeed(),    "s"));
        r.add(decorateStat("knockback", manager.getHitKbMultiplier(), "x"));
        return r;
    }
    private static List<TextComponent> generateStatsLore(final @NotNull ItemStack item) {
        final List<TextComponent> r = new ArrayList<>();
        r.add(decorateStat("Damage", VanillaProvider.getDamage(item.getType()), null));
        r.add(decorateStat("Attack speed", VanillaProvider.getAts(item.getType()), "s"));
        r.add(decorateStat("knockback", 1d, "x"));
        return r;
    }





    private static List<TextComponent> generateEnchantsLore(final @NotNull ItemStack item) {
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
            list.append(PlainTextComponentSerializer.plainText().serialize(e.getKey().displayName(e.getValue())));
            ++n;
        }
        r.addAll(Decorator.formatParagraph(list.toString(), 2, Decorator.COLOR_gray));


        // Return the lines or null if the item is not enchanted
        return n == 0 ? null : r;
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
