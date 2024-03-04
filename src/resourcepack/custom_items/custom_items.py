import copy
import os
import json
from shutil import copyfile


#IMPORTANT: Don't change the order of the items. It will break old ItemStack s
#IMPORTANT: Only add new elements at the end of the list
items = [
    { "id": "iron_scythe",      "parent": "scythe", "base": "iron_sword" },
    { "id": "diamond_scythe",   "parent": "scythe", "base": "diamond_sword" },
    { "id": "netherite_scythe", "parent": "scythe", "base": "netherite_sword" },
    { "id": "klaue_scythe",     "parent": "scythe", "base": "netherite_sword" },
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




with open(target_java + "/" + "_custom_model_ids.java", "w+") as java:
    # Generate java getter
    java.write(
        'package org.uwu_snek.shadownight._generated;\n'
        'import org.jetbrains.annotations.NotNull;\n'
        'import org.uwu_snek.shadownight.items.CustomItemId;\n'
        'import org.javatuples.Pair;\n'
        'import org.bukkit.Material;\n'
        'public final class _custom_model_ids extends org.uwu_snek.shadownight.utils.UtilityClass {\n'
        '    public static Pair<Material, Integer> getMaterialAndModel(@NotNull final CustomItemId id) {\n'
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
    for b, children in bases.items():

        # Save vanilla overrides and output copy
        base = json.load(open(source_base + "/" + b + ".json", "r"))
        base["overrides"] = (base["overrides"] if "overrides" in base else [])
        new_base = copy.deepcopy(base)


        # For each of its children
        for i, c in enumerate(children):

            # Generate custom model data ID and add it to the java plugin hook
            custom_model_data = data_zero + i
            java.write(f'        if(id == CustomItemId.{ c["id"].upper() }) return Pair.with(Material.{ c["base"].upper() }, { custom_model_data });\n')


            # For the base model and each of the vanilla overrides
            for j, override in enumerate([{ "predicate": {} }] + base["overrides"]):
                model_name = c["id"] + ("_" + str(j - 1) if j > 0 else "")

                # Generate the model
                json.dump(
                {
                        "parent": "shadow_night:item/" + c["parent"],
                        "textures": {
                            "layer0": "shadow_night:item/" + c["id"]
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
                copyfile(source_textures + "/" + model_name + ".png", target_textures + "/" + model_name + ".png")


        # Print new vanilla model
        json.dump(new_base, open(target_base + "/" + b + ".json", "w+"), indent=4)


    java.write(
        '        throw new RuntimeException("Invalid CustomItemId \\"" + id + "\\"");\n'
        '    }\n'
        '}'
    )
