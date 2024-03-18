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
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;




public final class DisplayBone extends Bone {
    private ItemDisplay displayEntity = null;
    private final int customModelData;
    private final Quaternionf displayRotation = new Quaternionf(0, 0, 0, 1);




    //TODO replace these with a list of hitboxes that can be used to approximate the area when rotating the bone
    private Interaction hitbox = null;
    private final float hitboxWidth;
    private final float hitboxHeight;

    public DisplayBone(final int partCustomModelData, final float hitboxWidth, final float hitboxHeight) {
        this.customModelData = partCustomModelData;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        displayRotation.rotateLocalY((float)Math.PI); //! For some reason, Display models are rotated by 180° on the Y-Axis. This resets that
    }

    public DisplayBone(final _mob_part_type partId, final float hitboxWidth, final float hitboxHeight) {
        this(partId.getCustomModelData(), hitboxWidth, hitboxHeight);
    }

    /**
     * Lightweight constructor for custom copy operations
     */
    private DisplayBone(final int partCustomModelData, final float hitboxWidth, final float hitboxHeight, final @Nullable Object dummy) {
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.customModelData = partCustomModelData;
    }

    @Override
    public Bone createShallowCopy(){
        final DisplayBone b = new DisplayBone(customModelData, hitboxWidth, hitboxHeight, null);
        b.locPos = locPos;
        b.origin = origin;
        b.displayRotation.set(displayRotation);
        return b;
    }




    @Override
    public void spawn(final @NotNull Entity mount){
        final Location location = mount.getLocation();
        super.spawn(mount);

        // Initialize display entity
        displayEntity = (ItemDisplay)location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        //displayEntity.setInterpolationDuration(4); //TODO maybe make this dynamic
        displayEntity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        displayEntity.setItemStack(ItemUtils.createItemStackDisplay(Material.BONE, customModelData));
        mount.addPassenger(displayEntity);
        updateDisplayTransform();

        // Initialize hitboxes
        hitbox = (Interaction)location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        hitbox.setInteractionWidth(hitboxWidth);
        hitbox.setInteractionHeight(hitboxHeight);
        displayEntity.addPassenger(hitbox);
        updateHitbox();
    }








    @Override
    public void moveSelf(final int duration, final @NotNull Vector3f v){
        super.moveSelf(duration, v);
        updateDisplayTransform();
        updateHitbox();
    }
    @Override
    public void moveUpdateOrigin(final @NotNull Vector3f o){
        super.moveUpdateOrigin(o);
        updateDisplayTransform();
        updateHitbox();
    }




    @Override
    public void rotateUnsafe(final int duration, final @NotNull AxisAngle4f r) {
        super.rotateUnsafe(duration, r);
        displayRotation.premul(new Quaternionf(r));
        updateDisplayTransform();
    }
    @Override
    public void rotateUpdateOrigin(final int duration, final @NotNull Vector3f o, final @NotNull AxisAngle4f r){
        super.rotateUpdateOrigin(duration, o, r);
        displayRotation.premul(new Quaternionf(r));
        updateDisplayTransform();
        updateHitbox();
    }




    private void updateDisplayTransform(){
        if(!spawned) return;
        Transformation t = new Transformation(
            new Vector3f(getAbsPos()).add(0, 0.5f, 0), // Center to in-game block. XZ is inverted by the resource pack generator script
            new AxisAngle4f(displayRotation),
            //new AxisAngle4f(new Quaternionf(0, 0, 0, 1)),
            new Vector3f(1),
            new AxisAngle4f(0, 0, 1, 0)
        );
        displayEntity.setInterpolationDuration(20); //TODO automate and add real interpolation
        displayEntity.setTransformation(t);
        displayEntity.setInterpolationDelay(0);
    }


    private void updateHitbox(){
        if(!spawned) return;
        final Vector3f absPos = getAbsPos();
        hitbox.teleport(displayEntity.getLocation().add(new Vector(absPos.x, absPos.y, absPos.z)));
    }
}
