# Create build directory
rm -rf ./build
mkdir ./build


# Zip texturepack ignoring krita files and temporary files
rsync -av --exclude='*.kra' --exclude '*~' ./ShadowNightSmp/ ./build/generated
cd build/generated && zip --no-extra -r ../output.zip ./*
