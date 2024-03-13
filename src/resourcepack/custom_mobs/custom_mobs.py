import os
import json

data_zero = 20_000

models = [
    { "id" : "test" }
]



source_base     = "./base_pack/assets/minecraft/models/item";         os.makedirs(source_base,     exist_ok=True)
target_base     = "./build/output/assets/minecraft/models/item";      os.makedirs(target_base,     exist_ok=True)
target_java     = "../main/java/org/uwu_snek/shadownight/_generated"; os.makedirs(target_java,     exist_ok=True)




base = json.load(open(source_base + "/" + "bone.json", "r"))




base["overrides"] = []
#for i, model in enumerate(models):
#    base["overrides"] += [{
#        "predicate": {
#            "custom_model_data": data_zero + i
#        },
#        "model": "shadow_night:item/" + model_name
#    }]




json.dump(base, open(target_base + "/" + "bone.json", "w+"), indent=4)