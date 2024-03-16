package org.uwu_snek.shadownight.customMobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.utils;

import java.util.logging.Level;




public final class DisplayBone extends Bone {
    public String DEBUG_NAME;
    private ItemDisplay displayEntity;
    private final int customModelData;
    private final Quaternionf displayRotation = new Quaternionf(0, 0, 1, 0);




    //TODO replace these with a list of hitboxes that can be used to approximate the area when rotating the bone
    private Interaction hitbox = null;
    private final float hitboxWidth;
    private final float hitboxHeight;

    public DisplayBone(final _mob_part_type partId, final float hitboxWidth, final float hitboxHeight, final String DEBUG_NAME) {
        this.DEBUG_NAME = DEBUG_NAME;
        this.customModelData = partId.getCustomModelData();
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
    }


    @Override
    public void summon(final @NotNull Entity mount){
        final Location location = mount.getLocation();
        super.summon(mount);

        // Initialize display entity
        displayEntity = (ItemDisplay)location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        //displayEntity.setInterpolationDuration(4); //TODO maybe make this dynamic
        displayEntity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        displayEntity.setItemStack(ItemUtils.createItemStackDisplay(Material.BONE, customModelData));
        mount.addPassenger(displayEntity);

        // Initialize hitboxes
        hitbox = (Interaction)location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        hitbox.setInteractionWidth(hitboxWidth);
        hitbox.setInteractionHeight(hitboxHeight);
        displayEntity.addPassenger(hitbox);
    }





    @Override
    public void move(final int duration, final @NotNull Vector3f v){
        super.move(duration, v);
        displayEntity.setInterpolationDuration(duration);
        updateDisplayTransform();
        updateHitbox();
    }
    @Override
    public void moveUpdateOrigin(final @NotNull Vector3f o){
        super.moveUpdateOrigin(o);
        updateDisplayTransform();
        updateHitbox();
    }



    public String DEBUG_printAngle(Quaternionf a ) {
        //AxisAngle4f g = new AxisAngle4f(a);
        AxisAngle4f g = new AxisAngle4f(a);
        return "(angle: " + g.angle + ", xyz: (" + g.x + ", " + g.y + ", " + g.z + "))";
    }

    public String DEBUG_printAngle(AxisAngle4f g ) {
        return "(angle: " + g.angle + ", xyz: (" + g.x + ", " + g.y + ", " + g.z + "))";
    }

    @Override
    public void rotateUnsafe(final int duration, final @NotNull AxisAngle4f r) {
        super.rotateUnsafe(duration, r);
        utils.log(Level.WARNING, "[" + DEBUG_NAME + "][self] Rotation 1: " + DEBUG_printAngle(displayRotation));
        displayRotation.rotateAxis(r.angle, -r.x, -r.y, r.z); // Invert XZ Plane (-y)
        utils.log(Level.WARNING, "[" + DEBUG_NAME + "][self] Rotation 2: " + DEBUG_printAngle(displayRotation));
        displayEntity.setInterpolationDuration(duration);
        updateDisplayTransform();
        //updateHitbox();
    }
    @Override
    public void rotateUpdateOrigin(final int duration, final @NotNull Vector3f o, final @NotNull AxisAngle4f r){
        super.rotateUpdateOrigin(duration, o, r);
        utils.log(Level.WARNING, "[" + DEBUG_NAME + "][inherited] Rotation 1: " + DEBUG_printAngle(displayRotation));
        displayRotation.rotateAxis(r.angle, -r.x, -r.y, r.z); // Invert XZ Plane (-y)
        utils.log(Level.WARNING, "[" + DEBUG_NAME + "][inherited] Rotation 2: " + DEBUG_printAngle(displayRotation));
        displayEntity.setInterpolationDuration(duration);
        updateDisplayTransform();
        updateHitbox();
    }




    private void updateDisplayTransform(){
        Transformation t = new Transformation(
            new Vector3f(getAbsPos()).add(0, 0.5f, 0), // Center to in-game block. XZ is inverted by the resource pack generator script
            new AxisAngle4f(displayRotation),
            new Vector3f(1, 1, 1),
            new AxisAngle4f(0, 0, 0, 0)
        );
        //TODO remove
        hitbox.teleport(hitbox.getLocation().clone().setDirection(new Vector(new AxisAngle4f(displayRotation).x, new AxisAngle4f(displayRotation).y, new AxisAngle4f(displayRotation).z)));
        displayEntity.setTransformation(t);
        displayEntity.setInterpolationDelay(0);
    }


    private void updateHitbox(){
        final Vector3f absPos = getAbsPos();
        //hitbox.teleport(displayEntity.getLocation().add(new Vector(absPos.x, absPos.y, absPos.z)));
        //TODO remove
        hitbox.teleport(displayEntity.getLocation().add(new Vector(absPos.x, absPos.y, absPos.z)).setDirection(new Vector(new AxisAngle4f(displayRotation).x, new AxisAngle4f(displayRotation).y, new AxisAngle4f(displayRotation).z)));
    }
}
