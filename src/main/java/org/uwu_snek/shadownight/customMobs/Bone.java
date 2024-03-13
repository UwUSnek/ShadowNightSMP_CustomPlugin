package org.uwu_snek.shadownight.customMobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.ArrayList;




public final class Bone {
    private ItemDisplay displayEntity;
    private final int customModelData;

    //private Vector3f totalBoneTranslation;
    //private AxisAngle4f totalBoneRotation;
    private Transformation boneTransform = new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(), new Vector3f(1, 1, 1), new AxisAngle4f(0, 0, 0, 0));
    private Bone parent = null;
    private final ArrayList<Bone> children = new ArrayList<>();




    public Bone(final int customModelData){
        this(customModelData, new Vector3f(0, 0, 0), new AxisAngle4f(0, 0, 0, 0));
    }

    public Bone(final int customModelData, Vector3f boneTranslation){
        this(customModelData, boneTranslation, new AxisAngle4f(0, 0, 0, 0));
    }

    public Bone(final int customModelData, AxisAngle4f boneRotation){
        this(customModelData, new Vector3f(0, 0, 0), boneRotation);
    }

    public Bone(final int customModelData, Vector3f boneTranslation, AxisAngle4f boneRotation){
        this.customModelData = customModelData;
        //displayTransform = new Transformation(new Vector3f(0, 0, 0), new AxisAngle4f(), new Vector3f(1, 1, 1), new AxisAngle4f());
    }

    public void summon(final @NotNull Location location){
        displayEntity = (ItemDisplay)location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        displayEntity.setInterpolationDelay(2); //TODO maybe make this dynamic
        displayEntity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        displayEntity.setItemStack(ItemUtils.createItemStackDisplay(Material.BONE, customModelData));
        for(Bone b : children) b.summon(location);
    }




    public void addChild(@NotNull Bone bone) {
        children.add(bone);
        bone.parent = this;
    }

    //private void updateDisplay(final @NotNull Vector3f totalTranslation, final @NotNull AxisAngle4f totalRotation){
    //    //TODO update self here
    //    for(Bone b : children) {
    //        totalRotation.
    //        b.updateDisplay(new Vector3f(totalTranslation).add(boneTranslation), ); //TODO
    //    }
    //}



    //public void a(){
    //     = new Transformation();
    //    modelEntity.setTransformation(t);
    //}
    public void move(final float x, final float y, final float z) {

    }
}
