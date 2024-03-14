import os
import json
import shutil




data_zero = 20_000
credit = "Generated by UwU_Snek's silly little Python script"
output_mc_namespace = "./build/output/assets/minecraft"
output_sn_namespace = "./build/output/assets/shadow_night"
target_java         = "../main/java/org/uwu_snek/shadownight/_generated"



source_base     = "./custom_mobs/base";                 os.makedirs(source_base, exist_ok=True)
target_base     = output_mc_namespace + "/models/item"; os.makedirs(target_base, exist_ok=True)

model_source = "./custom_mobs/models"
texture_source = "./custom_mobs/textures"

model_target   = output_sn_namespace + "/models/item/mob";   os.makedirs(model_target,   exist_ok=True)
texture_target = output_sn_namespace + "/textures/item/mob"; os.makedirs(texture_target, exist_ok=True)



base = json.load(open(source_base + "/" + "bone.json", "r"))
model_files = [{ "path": model_source + "/" + f, "base_name": f } for f in os.listdir(model_source) if os.path.isfile(os.path.join(model_source, f))]




base["overrides"] = []
java_class_name = "_custom_mobs"
with open(target_java + "/" + java_class_name + ".java", "w+") as java:
    java.write(
        f'package org.uwu_snek.shadownight._generated;\n'
        f'public enum { java_class_name } {{\n'
    )


    for i, model_file in enumerate(model_files):
        model = json.load(open(model_file["path"], "r"))


        # Invert XZ Plane
        for element in model["elements"]:
            element["from"][0] = -element["from"][0] + 16
            element["from"][2] = -element["from"][2] + 16
            element["to"  ][0] = -element["to"  ][0] + 16
            element["to"  ][2] = -element["to"  ][2] + 16


        # Copy textures and fix their path
        for key, texture in model["textures"].items():
            rel_texture_path = texture.split(":")[-1]  # The path of the texture starting from the textures/item directory without the extension
            model["textures"][key] = "shadow_night:item/mob/" + rel_texture_path
            shutil.copyfile(texture_source + "/" + rel_texture_path + ".png", texture_target + "/" + rel_texture_path + ".png")


        # Set credit and paste part model
        model["credit"] = credit
        json.dump(model, open(model_target + "/" + model_file["base_name"], "w+"), indent=4)


        # Generate mob part name
        part_name = "".join(model_file["base_name"].split(".")[:-1])

        # Add model as Bone variant
        base["overrides"] += [{
            "predicate": {
                "custom_model_data": data_zero + i
            },
            "model": "shadow_night:item/mob/" + part_name
        }]

        # Set java hook
        java.write(f'    { part_name.upper() }({ data_zero + i }){ "," if i < len(model_files) - 1 else ";" }\n')




    java.write(
        f'    private final int modelData;\n'
        f'    { java_class_name }(final int _modelData) {{\n'
        f'        this.modelData = _modelData;\n'
        f'    }}\n'
        f'    public int getCustomModelData() {{\n'
        f'        return modelData;\n'
        f'    }}\n'
        f'}}'
    )




# Set credit and paste base model
base["credit"] = credit
json.dump(base, open(target_base + "/" + "bone.json", "w+"), indent=4)