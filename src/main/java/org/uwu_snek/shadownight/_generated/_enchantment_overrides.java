package org.uwu_snek.shadownight._generated;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import java.util.AbstractMap;
import java.util.Map;
public final class _enchantment_overrides extends org.uwu_snek.shadownight.utils.UtilityClass {
    private static final Map<String, Enchantment> overrides = Map.ofEntries(
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.reeling", Enchantment.BINDING_CURSE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.tmp1", Enchantment.VANISHING_CURSE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.tmp2", Enchantment.SWEEPING_EDGE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.tmp3", Enchantment.FROST_WALKER),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.tmp4", Enchantment.MENDING),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.tmp5", Enchantment.SOUL_SPEED),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.tmp6", Enchantment.SWIFT_SNEAK),

        new AbstractMap.SimpleEntry<>("enchantment.minecraft.aqua_affinity", Enchantment.WATER_WORKER),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.bane_of_arthropods", Enchantment.DAMAGE_ARTHROPODS),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.blast_protection", Enchantment.PROTECTION_EXPLOSIONS),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.channeling", Enchantment.CHANNELING),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.depth_strider", Enchantment.DEPTH_STRIDER),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.efficiency", Enchantment.DIG_SPEED),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.feather_falling", Enchantment.PROTECTION_FALL),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.fire_aspect", Enchantment.FIRE_ASPECT),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.fire_protection", Enchantment.PROTECTION_FIRE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.flame", Enchantment.ARROW_FIRE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.fortune", Enchantment.LOOT_BONUS_BLOCKS),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.impaling", Enchantment.IMPALING),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.infinity", Enchantment.ARROW_INFINITE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.knockback", Enchantment.KNOCKBACK),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.looting", Enchantment.LOOT_BONUS_MOBS),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.loyalty", Enchantment.LOYALTY),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.luck_of_the_sea", Enchantment.LUCK),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.lure", Enchantment.LURE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.multishot", Enchantment.MULTISHOT),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.piercing", Enchantment.PIERCING),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.power", Enchantment.ARROW_DAMAGE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.projectile_protection", Enchantment.PROTECTION_PROJECTILE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.protection", Enchantment.PROTECTION_ENVIRONMENTAL),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.punch", Enchantment.ARROW_KNOCKBACK),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.quick_charge", Enchantment.QUICK_CHARGE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.respiration", Enchantment.OXYGEN),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.riptide", Enchantment.RIPTIDE),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.sharpness", Enchantment.DAMAGE_ALL),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.silk_touch", Enchantment.SILK_TOUCH),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.smite", Enchantment.DAMAGE_UNDEAD),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.thorns", Enchantment.THORNS),
        new AbstractMap.SimpleEntry<>("enchantment.minecraft.unbreaking", Enchantment.DURABILITY)
    );

    public static Enchantment getOverride(final @NotNull String e){ return overrides.get(e); }
}