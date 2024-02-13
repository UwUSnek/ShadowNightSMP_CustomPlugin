# Create build directory
rm -rf ./build
mkdir ./build


# Zip resource pack ignoring krita files and temporary files
# Don't save extra data in the zip (-X) to avoid unnecessary git changes
rsync -av --exclude='*.kra' --exclude '*~' ./ShadowNightSmp/ ./build/generated
cd build/generated && zip -X -r ../output.zip ./*
