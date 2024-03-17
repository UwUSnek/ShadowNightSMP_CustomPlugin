from resourcepack import utils




custom_color = "§3"

overrides = [
    { "key": "enchantment.minecraft.binding_curse",         "spigot_id": "BINDING_CURSE",   "override_key": "enchantment.minecraft.reeling",   "name": "Curse of Binding",   "override_name": "Reeling" },
    { "key": "enchantment.minecraft.vanishing_curse",       "spigot_id": "VANISHING_CURSE", "override_key": "enchantment.minecraft.unused1",   "name": "Curse of Vanishing", "override_name": "unused1" },
    { "key": "enchantment.minecraft.sweeping",              "spigot_id": "SWEEPING_EDGE",   "override_key": "enchantment.minecraft.unused2",   "name": "Sweeping Edge",      "override_name": "unused2" },
    { "key": "enchantment.minecraft.frost_walker",          "spigot_id": "FROST_WALKER",    "override_key": "enchantment.minecraft.unused3",   "name": "Frost Walker",       "override_name": "unused3" },
    { "key": "enchantment.minecraft.mending",               "spigot_id": "MENDING",         "override_key": "enchantment.minecraft.unused4",   "name": "Mending",            "override_name": "unused4" },
    { "key": "enchantment.minecraft.soul_speed",            "spigot_id": "SOUL_SPEED",      "override_key": "enchantment.minecraft.unused5",   "name": "Soul Speed",         "override_name": "unused5" },
    { "key": "enchantment.minecraft.swift_sneak",           "spigot_id": "SWIFT_SNEAK",     "override_key": "enchantment.minecraft.unused6",   "name": "Swift Sneak",        "override_name": "unused6" },
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




with open("./build/language/enchantments.json", "w+") as json, open(utils.target_java + "/_enchantment_overrides.java", "w+") as java:
    json.write("{\n")
    java.write(
        '// ' + utils.credit + '\n\n\n\n'
        'package org.uwu_snek.shadownight._generated;\n'
        'import org.bukkit.enchantments.Enchantment;\n'
        'import org.jetbrains.annotations.NotNull;\n'
        'import java.util.AbstractMap;\n'
        'import java.util.Map;\n'
        'public final class _enchantment_overrides extends org.uwu_snek.shadownight.utils.UtilityClass {\n'
    )




    # Create overrides
    java.write('    private static final Map<String, Enchantment> overrides = Map.ofEntries(\n')
    for i, e in enumerate(overrides):
        json.write(f'    "{ e["key"] }": "{ custom_color }{ e["override_name"] }",\n')
        java.write(f'        new AbstractMap.SimpleEntry<>("{ e["override_key"] }", Enchantment.{ e["spigot_id"] }),\n')
    json.write('\n')
    java.write('\n')
    for i, e in enumerate(unchanged):
        json.write(f'    "{ e["key"] }": "{ e["name"] }"{"," if i < len(unchanged) - 1 else "" }\n')
        java.write(f'        new AbstractMap.SimpleEntry<>("{ e["key"] }", Enchantment.{ e["spigot_id"] }){ "," if i < len(unchanged) - 1 else "" }\n')
    java.write('    );\n\n')




    # Create real name hooks
    java.write('    private static final Map<String, String> realNames = Map.ofEntries(\n')
    for i, e in enumerate(overrides):
        java.write(f'        new AbstractMap.SimpleEntry<>("{ e["override_key"] }", "{ e["override_name"] }"),\n')
        java.write(f'        new AbstractMap.SimpleEntry<>("{ e["key"] }", "{ e["name"] }"),\n')
    java.write('\n')
    for i, e in enumerate(unchanged):
        java.write(f'        new AbstractMap.SimpleEntry<>("{ e["key"] }", "{ e["name"] }"){ "," if i < len(unchanged) - 1 else "" }\n')
    java.write('    );\n\n')




    json.write('}')
    java.write(
        '    public static Enchantment getOverride(final @NotNull String e){ return overrides.get(e); }\n'
        '\n'
        '    private static final String[] lvlDisplay = new String[]{ "", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X", "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX", "XX" };\n'
        '    public static String getRealDisplayName(final @NotNull String e, final int lvl){ return realNames.get(e) + " " + (lvl > 20 ? lvl : lvlDisplay[lvl]); }\n'
        '}'
    )

