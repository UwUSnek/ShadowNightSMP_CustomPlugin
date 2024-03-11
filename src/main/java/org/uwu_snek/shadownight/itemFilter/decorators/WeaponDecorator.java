package org.uwu_snek.shadownight.itemFilter.decorators;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight._generated._enchantment_overrides;
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





    public static void decorateCustomMelee(final @NotNull ItemStack item, final @NotNull IM manager){
        final List<TextComponent> lore = new ArrayList<>(generateStatsLore(manager));
        utils.addAllIfNotNull(lore, Decorator.generateEnchantsLore(item));
        lore.addAll(generateAbilityLore(manager));
        lore.add(Component.empty());
        ItemUtils.setLore(item, lore);
    }


    public static void decorateVanillaMelee(final @NotNull ItemStack item){
        final List<TextComponent> lore = new ArrayList<>(generateStatsLore(item));
        utils.addAllIfNotNull(lore, Decorator.generateEnchantsLore(item));
        lore.add(Component.empty());
        ItemUtils.setLore(item, lore);
    }






    public static void decorateCustomRanged(final @NotNull ItemStack item, final @NotNull IM manager){
        final List<TextComponent> lore = new ArrayList<>();
        utils.addAllIfNotNull(lore, Decorator.generateEnchantsLore(item));
        lore.add(Component.empty());
        ItemUtils.setLore(item, lore);
    }


    public static void decorateVanillaRanged(final @NotNull ItemStack item){
        final List<TextComponent> lore = new ArrayList<>();
        utils.addAllIfNotNull(lore, Decorator.generateEnchantsLore(item));
        lore.add(Component.empty());
        ItemUtils.setLore(item, lore);
    }
}
