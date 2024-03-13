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




public final class DisplayBone extends Bone {
    private ItemDisplay displayEntity;
    private final int customModelData;
    private Transformation transform = new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(1), new AxisAngle4f());

    public DisplayBone(final int customModelData) {
        this.customModelData = customModelData;
    }


    @Override
    public void summon(final @NotNull Location location){
        super.summon(location);
        displayEntity = (ItemDisplay)location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        displayEntity.setInterpolationDuration(2); //TODO maybe make this dynamic
        displayEntity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        displayEntity.setItemStack(ItemUtils.createItemStackDisplay(Material.BONE, customModelData));
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
    @Override
    public void move(final @NotNull Vector3f v) {
        super.move(v);
        //absPos //TODO
        transform.getTranslation().add(v);
        displayEntity.setTransformation(transform);
        displayEntity.setInterpolationDelay(0);
    }
}
