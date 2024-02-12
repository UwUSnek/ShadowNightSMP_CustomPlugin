rm -rf ./build
mkdir ./build
rsync -av --exclude='*.kra' --exclude '*~' ./ShadowNightSmp/ ./build/generated
cd build/generated && zip -r ../output.zip ./*

