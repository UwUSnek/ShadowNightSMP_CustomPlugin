import os


target_java = "../main/java/org/uwu_snek/shadownight/_generated"; os.makedirs(target_java, exist_ok=True)
db = "./current"
version = 0


# Create file if it doesn't exist
if os.path.exists(db):
    with open(db, "r") as f:
        version = int(f.read()) + 1

# If it does, read the last version
with open(db, "w+") as f:
    f.write(str(version))



# Write java hook
with open(target_java + "/_build_counter.java", "w+") as java:
    java.write(
        "package org.uwu_snek.shadownight._generated;\n"
        "public final class _build_counter extends org.uwu_snek.shadownight.utils.UtilityClass {\n"
        "    public static int getCurrentValue() {\n"
        "        return " + str(version) + ";\n"
        "    }\n"
        "}"
    )
