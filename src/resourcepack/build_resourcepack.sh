#!/bin/bash
set -e


# Create build directory
mkdir -p ./build
rm -rf ./build/*
mkdir ./build/language


# Copy resource pack ignoring krita files and temporary files
rsync -q -av --exclude='*.kra' --exclude '*~' ./base_pack/ ./build/base_pack
mkdir ./build/base_pack/assets/minecraft/lang




# Generate language file
python3 ./language/enchantments.py
python3 ./language/language.py

# Generate models and textures
python3 ./custom_items/custom_items.py




# Zip pack
# Don't save extra data in the zip (-X) to avoid unnecessary git changes
cd build/base_pack && zip -q -X -r ../output.zip ./*
cd ../../../../ && rm ./venv -rf



