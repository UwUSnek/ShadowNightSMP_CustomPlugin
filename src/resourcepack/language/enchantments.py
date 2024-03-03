

custom_color = "§3"

overrides = [
    { "key": "enchantment.minecraft.binding_curse",         "spigot_id": "BINDING_CURSE",   "key_override": "enchantment.minecraft.reeling", "name_override": "Reeling" },
    { "key": "enchantment.minecraft.vanishing_curse",       "spigot_id": "VANISHING_CURSE", "key_override": "enchantment.minecraft.tmp1",     "name_override": "tmp1" },
    { "key": "enchantment.minecraft.sweeping",              "spigot_id": "SWEEPING_EDGE",   "key_override": "enchantment.minecraft.tmp2",     "name_override": "tmp2" },
    { "key": "enchantment.minecraft.frost_walker",          "spigot_id": "FROST_WALKER",    "key_override": "enchantment.minecraft.tmp3",     "name_override": "tmp3" },
    { "key": "enchantment.minecraft.mending",               "spigot_id": "MENDING",         "key_override": "enchantment.minecraft.tmp4",     "name_override": "tmp4" },
    { "key": "enchantment.minecraft.soul_speed",            "spigot_id": "SOUL_SPEED",      "key_override": "enchantment.minecraft.tmp5",     "name_override": "tmp5" },
    { "key": "enchantment.minecraft.swift_sneak",           "spigot_id": "SWIFT_SNEAK",     "key_override": "enchantment.minecraft.tmp6",     "name_override": "tmp6" },
]

unchanged = [
    { "key": "enchantment.minecraft.aqua_affinity",         "spigot_id": "WATER_WORKER",             "name": "Aqua Affinity" },
    { "key": "enchantment.minecraft.bane_of_arthropods",    "spigot_id": "DAMAGE_ARTHROPODS",        "name": "Bane of Arthropods" },
    { "key": "enchantment.minecraft.blast_protection",      "spigot_id": "PROTECTION_EXPLOSIONS",    "name": "Blast Protection" },
    { "key": "enchantment.minecraft.channeling",            "spigot_id": "CHANNELING",               "name": "Channeling" },
    { "key": "enchantment.minecraft.depth_strider",         "spigot_id": "DEPTH_STRIDER",            "name": "Depth Strider" },
    { "key": "enchantment.minecraft.efficiency",            "spigot_id": "DIG_SPEED",                "name": "Efficiency" },
    { "key": "enchantment.minecraft.feather_falling",       "spigot_id": "PROTECTION_FALL",          "name": "Feather Falling" },
    { "key": "enchantment.minecraft.fire_aspect",           "spigot_id": "FIRE_ASPECT",              "name": "Fire Aspect" },
    { "key": "enchantment.minecraft.fire_protection",       "spigot_id": "PROTECTION_FIRE",          "name": "Fire Protection" },
    { "key": "enchantment.minecraft.flame",                 "spigot_id": "ARROW_FIRE",               "name": "Flame" },
    { "key": "enchantment.minecraft.fortune",               "spigot_id": "LOOT_BONUS_BLOCKS",        "name": "Fortune" },
    { "key": "enchantment.minecraft.impaling",              "spigot_id": "IMPALING",                 "name": "Impaling" },
    { "key": "enchantment.minecraft.infinity",              "spigot_id": "ARROW_INFINITE",           "name": "Infinity" },
    { "key": "enchantment.minecraft.knockback",             "spigot_id": "KNOCKBACK",                "name": "Knockback" },
    { "key": "enchantment.minecraft.looting",               "spigot_id": "LOOT_BONUS_MOBS",          "name": "Looting" },
    { "key": "enchantment.minecraft.loyalty",               "spigot_id": "LOYALTY",                  "name": "Loyalty" },
    { "key": "enchantment.minecraft.luck_of_the_sea",       "spigot_id": "LUCK",                     "name": "Luck of the Sea" },
    { "key": "enchantment.minecraft.lure",                  "spigot_id": "LURE",                     "name": "Lure" },
    { "key": "enchantment.minecraft.multishot",             "spigot_id": "MULTISHOT",                "name": "Multishot" },
    { "key": "enchantment.minecraft.piercing",              "spigot_id": "PIERCING",                 "name": "Piercing" },
    { "key": "enchantment.minecraft.power",                 "spigot_id": "ARROW_DAMAGE",             "name": "Power" },
    { "key": "enchantment.minecraft.projectile_protection", "spigot_id": "PROTECTION_PROJECTILE",    "name": "Projectile Protection" },
    { "key": "enchantment.minecraft.protection",            "spigot_id": "PROTECTION_ENVIRONMENTAL", "name": "Protection" },
    { "key": "enchantment.minecraft.punch",                 "spigot_id": "ARROW_KNOCKBACK",          "name": "Punch" },
    { "key": "enchantment.minecraft.quick_charge",          "spigot_id": "QUICK_CHARGE",             "name": "Quick Charge" },
    { "key": "enchantment.minecraft.respiration",           "spigot_id": "OXYGEN",                   "name": "Respiration" },
    { "key": "enchantment.minecraft.riptide",               "spigot_id": "RIPTIDE",                  "name": "Riptide" },
    { "key": "enchantment.minecraft.sharpness",             "spigot_id": "DAMAGE_ALL",               "name": "Sharpness" },
    { "key": "enchantment.minecraft.silk_touch",            "spigot_id": "SILK_TOUCH",               "name": "Silk Touch" },
    { "key": "enchantment.minecraft.smite",                 "spigot_id": "DAMAGE_UNDEAD",            "name": "Smite" },
    { "key": "enchantment.minecraft.thorns",                "spigot_id": "THORNS",                   "name": "Thorns" },
    { "key": "enchantment.minecraft.unbreaking",            "spigot_id": "DURABILITY",               "name": "Unbreaking" },
]




with open("./build/language/enchantments.json", "w+") as json, open("../main/java/org/uwu_snek/shadownight/_generated/_enchantment_overrides.java", "w+") as java:
    json.write("{\n")
    java.write(
        'package org.uwu_snek.shadownight._generated;\n'
        'import org.bukkit.enchantments.Enchantment;\n'
        'import org.jetbrains.annotations.NotNull;\n'
        'import java.util.AbstractMap;\n'
        'import java.util.Map;\n'
        'public final class _enchantment_overrides extends org.uwu_snek.shadownight.utils.UtilityClass {\n'
        '    private static final Map<String, Enchantment> overrides = Map.ofEntries(\n'
    )


    for i, e in enumerate(overrides):
        json.write(f'    "{ e["key"] }": "{ custom_color }{ e["name_override"] }",\n')
        java.write(f'        new AbstractMap.SimpleEntry<>("{ e["key_override"] }", Enchantment.{ e["spigot_id"] }),\n')


    json.write('\n')
    java.write('\n')


    for i, e in enumerate(unchanged):
        json.write(f'    "{ e["key"] }": "{ e["name"] }"{"," if i < len(unchanged) - 1 else "" }\n')
        java.write(f'        new AbstractMap.SimpleEntry<>("{ e["key"] }", Enchantment.{ e["spigot_id"] }){ "," if i < len(unchanged) - 1 else "" }\n')


    json.write('}')
    java.write(
        '    );\n\n'
        '    public static Enchantment getOverride(final @NotNull String e){ return overrides.get(e); }\n'
        '}'
    )

