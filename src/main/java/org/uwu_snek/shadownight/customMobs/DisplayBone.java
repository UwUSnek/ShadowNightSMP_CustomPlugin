package org.uwu_snek.shadownight.customMobs;

import io.papermc.paper.event.player.PrePlayerAttackEntityEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;




public final class DisplayBone extends Bone {
    private ItemDisplay displayEntity;
    private final int customModelData;
    private Transformation transform = new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(1), new AxisAngle4f());

    //TODO replace these with a list of hitboxes that can be used to approximate the area when rotating the bone
    private Interaction hitbox = null;
    private final double hitboxWidth;
    private final double hitboxHeight;

    public DisplayBone(final int customModelData, final double hitboxWidth, final int hitboxHeight) {
        this.customModelData = customModelData;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
    }


    @Override
    public void summon(final @NotNull Location location, final @NotNull Entity mount){
        super.summon(location, mount);

        // Initialize display entity
        displayEntity = (ItemDisplay)location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        displayEntity.setInterpolationDuration(2); //TODO maybe make this dynamic
        displayEntity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        displayEntity.setItemStack(ItemUtils.createItemStackDisplay(Material.BONE, customModelData));
        mount.addPassenger(displayEntity);

        // Initialize hitboxes
        hitbox = (Interaction)location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        displayEntity.addPassenger(hitbox);
    }


    //private void updateDisplay(final @NotNull Vector3f totalTranslation, final @NotNull AxisAngle4f totalRotation){
    //    //TODO update self here
    //    for(Bone b : children) {
    //        totalRotation.
    //        b.updateDisplay(new Vector3f(totalTranslation).add(boneTranslation), ); //TODO
    //    }
    //}


/*
    @Override
    public void move(final @NotNull Vector3f v, final @NotNull Vector3f parentAbsPos) {
        super.move(v);
        transform.getTranslation().add(v);
        updateDisplayTransform();
    }
*/


    @Override
    public void move(final @NotNull Vector3f v){
        super.move(v);
        updateDisplayTransform();
        updateHitbox();
    }


    private void updateDisplayTransform(){
        Transformation t = new Transformation(
            new Vector3f(absPos).add(0, 0.5f, 0),
            transform.getLeftRotation(),
            transform.getScale(),
            transform.getRightRotation()
        );
        displayEntity.setTransformation(t);
        displayEntity.setInterpolationDelay(0);
    }


    private void updateHitbox(){
        //Vector3f absTranslation = transform.getTranslation();
        //hitbox.teleport(displayEntity.getLocation().add(new Vector(absTranslation.x, absTranslation.y, absTranslation.z)));
        hitbox.teleport(displayEntity.getLocation().add(new Vector(absPos.x, absPos.y, absPos.z)));
    }
}
