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
import org.uwu_snek.shadownight._generated._custom_mobs;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.utils;

import java.util.logging.Level;




public final class DisplayBone extends Bone {
    private ItemDisplay displayEntity;
    private final int customModelData;
    //private Transformation transform = new Transformation(new Vector3f(), new AxisAngle4f(), new Vector3f(1), new AxisAngle4f());

    //TODO replace these with a list of hitboxes that can be used to approximate the area when rotating the bone
    private Interaction hitbox = null;
    private final float hitboxWidth;
    private final float hitboxHeight;

    public DisplayBone(final _custom_mobs partId, final float hitboxWidth, final float hitboxHeight) {
        this.customModelData = partId.getCustomModelData();
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
    }


    @Override
    public void summon(final @NotNull Location location, final @NotNull Entity mount){
        super.summon(location, mount);

        // Initialize display entity (Make each of them face North to align the model)
        // Make display face South to negate the inverted XZ Plane caused by the mount facing North
        //displayEntity = (ItemDisplay)location.getWorld().spawnEntity(location.clone().setDirection(new Vector(0, 0, 1)), EntityType.ITEM_DISPLAY);
        displayEntity = (ItemDisplay)location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        displayEntity.setInterpolationDuration(2); //TODO maybe make this dynamic
        displayEntity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        displayEntity.setItemStack(ItemUtils.createItemStackDisplay(Material.BONE, customModelData));
        mount.addPassenger(displayEntity);

        // Initialize hitboxes
        hitbox = (Interaction)location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        hitbox.setInteractionWidth(hitboxWidth);
        hitbox.setInteractionHeight(hitboxHeight);
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
            //new Vector3f(absPos).add(0, 0.5f, 0).mul(-1, 1, -1), // Display render has inverted XZ Plane
            new Vector3f(absPos).add(0, 0.5f, 0),
            //new AxisAngle4f((float)Math.PI, 0, 1, 0),
            new AxisAngle4f(0, 0, 1, 0),
            new Vector3f(1, 1, 1),
            new AxisAngle4f(0, 0, 0, 0)
        );
        displayEntity.setTransformation(t);
        displayEntity.setInterpolationDelay(0);

        utils.log(Level.WARNING, absPos.toString());
    }


    private void updateHitbox(){
        //Vector3f absTranslation = transform.getTranslation();
        //hitbox.teleport(displayEntity.getLocation().add(new Vector(absTranslation.x, absTranslation.y, absTranslation.z)));
        hitbox.teleport(displayEntity.getLocation().add(new Vector(absPos.x, absPos.y, absPos.z)));
    }
}
