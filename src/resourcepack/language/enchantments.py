

custom_color = "§3"

overrides = [
    { "key": "enchantment.minecraft.binding_curse",   "id": 10, "override": "Reeling"},
    { "key": "enchantment.minecraft.vanishing_curse", "id": 37, "override": ""},
    { "key": "enchantment.minecraft.sweeping",        "id": 18, "override": ""},
    { "key": "enchantment.minecraft.frost_walker",    "id":  9, "override": ""},
    { "key": "enchantment.minecraft.mending",         "id": 36, "override": ""},
    { "key": "enchantment.minecraft.soul_speed",      "id": 11, "override": ""},
    #{ "key": "enchantment.minecraft.swift_sneak",     "id": , "override": ""}, # swift sneak is a treasure enchant, but it doesn't seem to have a numerical ID
]

unchanged = [
    { "key": "enchantment.minecraft.swift_sneak",           "name": "Swift Sneak"}, # swift sneak is a treasure enchant, but it doesn't seem to have a numerical ID
    { "key": "enchantment.minecraft.aqua_affinity",         "name": "Aqua Affinity" },
    { "key": "enchantment.minecraft.bane_of_arthropods",    "name": "Bane of Arthropods" },
    { "key": "enchantment.minecraft.blast_protection",      "name": "Blast Protection" },
    { "key": "enchantment.minecraft.channeling",            "name": "Channeling" },
    { "key": "enchantment.minecraft.depth_strider",         "name": "Depth Strider" },
    { "key": "enchantment.minecraft.efficiency",            "name": "Efficiency" },
    { "key": "enchantment.minecraft.feather_falling",       "name": "Feather Falling" },
    { "key": "enchantment.minecraft.fire_aspect",           "name": "Fire Aspect" },
    { "key": "enchantment.minecraft.fire_protection",       "name": "Fire Protection" },
    { "key": "enchantment.minecraft.flame",                 "name": "Flame" },
    { "key": "enchantment.minecraft.fortune",               "name": "Fortune" },
    { "key": "enchantment.minecraft.impaling",              "name": "Impaling" },
    { "key": "enchantment.minecraft.infinity",              "name": "Infinity" },
    { "key": "enchantment.minecraft.knockback",             "name": "Knockback" },
    { "key": "enchantment.minecraft.looting",               "name": "Looting" },
    { "key": "enchantment.minecraft.loyalty",               "name": "Loyalty" },
    { "key": "enchantment.minecraft.luck_of_the_sea",       "name": "Luck of the Sea" },
    { "key": "enchantment.minecraft.lure",                  "name": "Lure" },
    { "key": "enchantment.minecraft.multishot",             "name": "Multishot" },
    { "key": "enchantment.minecraft.piercing",              "name": "Piercing" },
    { "key": "enchantment.minecraft.power",                 "name": "Power" },
    { "key": "enchantment.minecraft.projectile_protection", "name": "Projectile Protection" },
    { "key": "enchantment.minecraft.protection",            "name": "Protection" },
    { "key": "enchantment.minecraft.punch",                 "name": "Punch" },
    { "key": "enchantment.minecraft.quick_charge",          "name": "Quick Charge" },
    { "key": "enchantment.minecraft.respiration",           "name": "Respiration" },
    { "key": "enchantment.minecraft.riptide",               "name": "Riptide" },
    { "key": "enchantment.minecraft.sharpness",             "name": "Sharpness" },
    { "key": "enchantment.minecraft.silk_touch",            "name": "Silk Touch" },
    { "key": "enchantment.minecraft.smite",                 "name": "Smite" },
    { "key": "enchantment.minecraft.thorns",                "name": "Thorns" },
    { "key": "enchantment.minecraft.unbreaking",            "name": "Unbreaking" },
]




with open("./build/language/enchantments.json", "w+") as json, open("../main/java/org/uwu_snek/shadownight/_generated/EnchantmentOverrideCodes.java", "w+") as java:
    json.write("{\n")
    java.write(
        "package org.uwu_snek.shadownight._generated;\n" +
        "import org.uwu_snek.shadownight.utils.UtilityClass;\n" +
        "public final class EnchantmentOverrideCodes extends UtilityClass {\n"
    )


    for e in overrides:
        json.write(f'    "{ e["key"] }": "{ custom_color }{ e["override"] }",\n')


    json.write("\n")


    for i, e in enumerate(unchanged):
        json.write(f'    "{ e["key"] }": "{ e["name"] }"{"," if i < len(unchanged) - 1 else "" }\n')


    json.write("}")
    java.write("}")

