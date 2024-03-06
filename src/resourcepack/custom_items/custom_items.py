import copy
import os
import json
from shutil import copyfile


#! IMPORTANT: Don't change the order of children of a base item. It will break old ItemStacks
#! IMPORTANT: Only add new elements at the end of the list
items = [
    { "id": "golden_dagger",    "parent": "dagger", "base": "golden_sword" },
    { "id": "golden_spear",     "parent": "spear",  "base": "golden_sword" },

    { "id": "stone_dagger",     "parent": "dagger", "base": "stone_sword" },
    { "id": "stone_spear",      "parent": "spear",  "base": "stone_sword" },

    { "id": "iron_scythe",      "parent": "scythe", "base": "iron_sword" },
    { "id": "iron_dagger",      "parent": "dagger", "base": "iron_sword" },
    { "id": "iron_spear",       "parent": "spear",  "base": "iron_sword" },

    { "id": "diamond_scythe",   "parent": "scythe", "base": "diamond_sword" },
    { "id": "diamond_dagger",   "parent": "dagger", "base": "diamond_sword" },
    { "id": "diamond_spear",    "parent": "spear",  "base": "diamond_sword" },

    { "id": "klaue_scythe",     "parent": "scythe", "base": "netherite_sword" },
    { "id": "netherite_scythe", "parent": "scythe", "base": "netherite_sword" },
    { "id": "netherite_dagger", "parent": "dagger", "base": "netherite_sword" },
    { "id": "netherite_spear",  "parent": "spear",  "base": "netherite_sword" },

    { "id": "hellfire_bow",     "parent": "bow",    "base": "bow" },
]


source_base     = "./custom_items/base"
source_parents  = "./custom_items/parents"
source_textures = "./custom_items/textures"

target_base     = "./build/base_pack/assets/minecraft/models/item";      os.makedirs(target_base,     exist_ok=True)
target_models   = "./build/base_pack/assets/shadow_night/models/item";   os.makedirs(target_models,   exist_ok=True)
target_textures = "./build/base_pack/assets/shadow_night/textures/item"; os.makedirs(target_textures, exist_ok=True)
target_java     = "../main/java/org/uwu_snek/shadownight/_generated";    os.makedirs(target_java,     exist_ok=True)


parents = []
bases = {}
data_zero = 10_000




with open(target_java + "/" + "_custom_item_data.java", "w+") as java_data, open(target_java + "/" + "_custom_item_id.java", "w+") as java_id:
    # Generate java getter
    java_data.write(
        'package org.uwu_snek.shadownight._generated;\n'
        'import org.jetbrains.annotations.NotNull;\n'
        'import org.uwu_snek.shadownight._generated._custom_item_id;\n'
        'import org.javatuples.Pair;\n'
        'import org.bukkit.Material;\n'
        'public final class _custom_item_data extends org.uwu_snek.shadownight.utils.UtilityClass {\n'
        '    public static Pair<Material, Integer> getMaterialAndModel(@NotNull final _custom_item_id id) {\n'
    )
    java_id.write(
        'package org.uwu_snek.shadownight._generated;\n'
        'import org.uwu_snek.shadownight.utils.utils;\n'
        'import java.nio.charset.StandardCharsets;\n'
        'public enum _custom_item_id {\n'
    )


    # For each custom item
    for i in items:
        # Save it in the children list of its base vanilla item
        if i["base"] not in bases:
            bases[i["base"]] = []
        bases[i["base"]] += [ i ]

        # Save parent in the parent list
        if i["parent"] not in parents:
            parents += [ i["parent"] ]




    # For each parent
    for p in parents:
        # Pate its hard coded model
        copyfile(source_parents + "/" + p + ".json", target_models + "/" + p + ".json")




    # For each base vanilla item
    for k, b in enumerate(bases.items()):

        # Save vanilla overrides and output copy
        base = json.load(open(source_base + "/" + b[0] + ".json", "r"))
        base["overrides"] = (base["overrides"] if "overrides" in base else [])
        new_base = copy.deepcopy(base)


        # For each of its children
        for i, c in enumerate(b[1]):

            # Generate custom model data ID and add it to the java plugin hook
            custom_model_data = data_zero + i
            java_data.write(f'        if(id == _custom_item_id.{ c["id"].upper() }) return Pair.with(Material.{ c["base"].upper() }, { custom_model_data });\n')
            java_id.write(f'    { c["id"].upper() }(){ "," if k < len(bases) - 1 or i < len(b[1]) - 1 else ";" }\n')


            # For the base model and each of the vanilla overrides
            for j, override in enumerate([{ "predicate": {} }] + base["overrides"]):
                model_name = c["id"] + ("_" + str(j - 1) if j > 0 else "")

                # Generate the model
                json.dump(
                    {
                        "parent": "shadow_night:item/" + c["parent"],
                        "textures": {
                            "layer0": "shadow_night:item/" + c["parent"] + c["id"]
                        }
                    },
                    open(target_models + "/" + model_name + ".json", "w+"), indent=4
                )

                # Add model override to the vanilla model
                predicate = override["predicate"]
                predicate["custom_model_data"] = custom_model_data
                new_base["overrides"] += [{
                    "predicate": predicate,
                    "model": "shadow_night:item/" + model_name
                }]

                # Paste its hard coded texture
                copyfile(source_textures + "/" + c["parent"] + "/" + model_name + ".png", target_textures + "/" + model_name + ".png")


        # Print new vanilla model
        json.dump(new_base, open(target_base + "/" + b[0] + ".json", "w+"), indent=4)


    java_data.write(
        '        throw new RuntimeException("Invalid _custom_item_id \\"" + id + "\\"");\n'
        '    }\n'
        '}'
    )
    java_id.write(
        '    private final long value;\n'
        '    _custom_item_id() {\n'
        '        this.value = utils.createHash64(this.name().getBytes(StandardCharsets.UTF_8));\n'
        '    }\n'
        '    public long getNumericalValue() {\n'
        '        return value;\n'
        '    }\n'
        '}'
    )
