import json
import numbers
import shutil
import re
from resourcepack import utils







data_zero = 20_000

source_base     = utils.mkdirs("./custom_mobs/base")
target_base     = utils.mkdirs(utils.output_mc_namespace + "/models/item")

model_source    = "./custom_mobs/models"
texture_source  = "./custom_mobs/textures"

model_target    = utils.mkdirs(utils.output_sn_namespace + "/models/item/mob")
texture_target  = utils.mkdirs(utils.output_sn_namespace + "/textures/item/mob")







base = json.load(open(source_base + "/" + "bone.json", "r"))
model_files = utils.list_files_recursive(model_source)






# Translates a vector <v> by the amount that would bring <origin> to the coordinates [8, 8, 8]
def center_vector(v, origin):
    return [
        v[0] - (origin[0] - 8),
        v[1] - (origin[1] - 8),
        v[2] - (origin[2] - 8)
    ]

#def rotate_vector_180(v):
#    return [
#        v[0]
#        v[1]
#        v[2]
#    ]

def check_element_coordinate_pos(v):
    return (
        -16 <= v[0] <= 32 and
        -16 <= v[1] <= 32 and
        -16 <= v[2] <= 32
    )




def get_all_group_elements(full_model, child):
    if isinstance(child, numbers.Number):
        return [full_model["elements"][child]]
    else:
        r = []
        for c in child["children"]:
            r += get_all_group_elements(full_model, c)
        return r




data_i = 0
def generate_part_model(full_model, full_model_rel_path: str, part, java_parts, java_preset_data: dict):
    global data_i
    part_model = {
        "credit": utils.credit,
        #"texture_size": full_model, #TODO check if this is needed
        "textures": full_model["textures"],
        "elements": [],
    }




    # Find origin and extrapolate data
    DEBUG_pos = f'part "{ part["name"] }" of model { full_model_rel_path }'
    origin_i = -1
    for group_child in part["children"]:
        if isinstance(group_child, numbers.Number):
            element = full_model["elements"][group_child]
            if "name" in element and element["name"].startswith("@"):
                assert origin_i < 0, f'Multiple origins found in { DEBUG_pos }'
                origin_i = group_child
                continue
    assert origin_i > -1, f'Missing origin in { DEBUG_pos }'
    origin = full_model["elements"][origin_i]

    assert origin["from"] == origin["to"], f'Origin "{ origin["name"] }" of { DEBUG_pos } is not of size 0'
    origin_data = {
        "parent": origin["name"][1:],
        "pos": origin["from"]
    }




    # Add part elements and shift them to center their origin to [8, 8, 8]. Ignore 0-Size elements
    for group_child in part["children"]:
        part_model["elements"] += [ e for e in get_all_group_elements(full_model, group_child) if e["from"] != e["to"] ]

    for i, element in enumerate(part_model["elements"]):
        element["from"] = center_vector(element["from"], origin_data["pos"])
        element["to"  ] = center_vector(element["to"], origin_data["pos"])
        if "rotation" in element:
            element["rotation"]["origin"] = center_vector(element["rotation"]["origin"], origin_data["pos"])

        assert\
            check_element_coordinate_pos(element["from"]) and check_element_coordinate_pos(element["to"]), \
            f'Repositioning element n.{ i } "{ element["name"] if "name" in element else "*unnamed*" }" would cause it to be outside of the allowed area.'\
            f'\n{ DEBUG_pos.capitalize() }\n    New "from": { element["from"] }\n    New "to":   { element["to"] }'




    # Paste part model
    full_part_name = "".join(full_model_rel_path.split(".")[:-1]) + "/" + part["name"]
    json.dump(part_model, open(utils.mkdirs(model_target + "/" + full_part_name + ".json"), "w+"), indent=4)




    # Add model as Bone variant
    base["overrides"] += [{
        "predicate": {
            "custom_model_data": data_zero + data_i
        },
        "model": "shadow_night:item/mob/" + full_part_name
    }]




    # Set java hook
    sanitized_full_part_name = re.sub(r"[/-]", "_", full_part_name)
    java_parts.write(f'    { sanitized_full_part_name.upper() }({ data_zero + data_i }),\n')
    data_i += 1


    # Create java preset data
    java_preset_data["members"    ] += f'    protected Bone { part["name"] } = new DisplayBone(_mob_part_type.{ sanitized_full_part_name.upper() }, 0.5f, 0.5f);\n'  #TODO fix hitboxes
    java_preset_data["connections"] += f'        { origin_data["parent"] }.addChild({ part["name"] });\n'
    java_preset_data["adjustments"] += (
        f'        { part["name"] }.moveSelf(20, '   #TODO fix duration
        f'{  (origin_data["pos"][0] - 8) / 16 }f, '  #! Divide by 16 as ItemDisplay translations use block-sized units. Minecraft JSON Model units are 16 times smaller
        f'{ -(origin_data["pos"][1] - 8) / 16 }f, '  #! Invert Y-Axis translations because reasons
        f'{  (origin_data["pos"][2] - 8) / 16 }f);\n'
    )











def generate_mob(model_file, java_parts):
    model = json.load(open(model_file["path"], "r"))

    sanitized_model_name = re.sub(r"[/-]", "_", "".join(model_file["rel_path"].split(".")[:-1]))
    java_class_name = "_mob_preset_" + sanitized_model_name
    with open(utils.mkdirs(utils.target_java + "/_mob_presets/" + java_class_name + ".java"), "w+") as java_preset:


        # Copy textures and fix their path
        for key, texture in model["textures"].items():
            rel_texture_path = texture.split(":")[-1]  # The path of the texture starting from the textures/item directory without the extension
            model["textures"][key] = "shadow_night:item/mob/" + rel_texture_path

            shutil.copyfile(texture_source + "/" + rel_texture_path + ".png", utils.mkdirs(texture_target + "/" + rel_texture_path + ".png"))


        # Separate parts and generate respective models and hooks
        java_preset_data = { "members": "", "connections": "", "adjustments": "" }
        model_path = model_source + "/" + model_file["rel_path"]
        assert "groups" in model and len(model["groups"]) > 0, "Model " + model_path + " has no groups"
        for j, part in enumerate(model["groups"]):
            assert not isinstance(part, numbers.Number), f'Element n.{ str(j) } of model { model_path } is not grouped'
            assert "name" in part, f'Group n.{ str(j) } of model { model_path } has no name.'
            assert re.match(r'[a-zA-Z0-9_]*', part["name"]), f'Group name "{ part["name"] }" is not valid.'
            generate_part_model(model, model_file["rel_path"], part, java_parts, java_preset_data)


        # Create java preset
        java_preset.write(
            f'// ' + utils.credit + '\n\n\n\n'
            f'package org.uwu_snek.shadownight._generated._mob_presets;\n'
            f'import org.uwu_snek.shadownight.customMobs.MOB;\n'
            f'import org.uwu_snek.shadownight._generated._mob_part_type;\n'
            f'import org.uwu_snek.shadownight.customMobs.Bone;\n'
            f'import org.uwu_snek.shadownight.customMobs.DisplayBone;\n'
            f'public abstract class { java_class_name } extends MOB {{\n'
            f'{ java_preset_data["members"] }\n'
            f'    public { java_class_name }() {{\n'
            f'        super();\n'
            f'{ java_preset_data["connections"] }'
            f'{ java_preset_data["adjustments"] }'
            f'    }}\n'
            f'}}\n'
        )










def main():
    base["overrides"] = []
    java_class_name = "_mob_part_type"
    with open(utils.target_java + "/" + java_class_name + ".java", "w+") as java_parts:
        java_parts.write(
            f'// ' + utils.credit + '\n\n\n\n'
            f'package org.uwu_snek.shadownight._generated;\n'
            f'public enum { java_class_name } {{\n'
        )


        for i, model_file in enumerate(model_files):
            generate_mob(model_file, java_parts)


        java_parts.write(
            f'    ;\n'
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
    base["credit"] = utils.credit
    json.dump(base, open(target_base + "/" + "bone.json", "w+"), indent=4)


main()