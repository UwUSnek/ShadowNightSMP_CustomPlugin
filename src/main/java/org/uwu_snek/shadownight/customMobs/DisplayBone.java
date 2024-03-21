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




    //TODO replace these with a list of hitboxes that can be used to approximate the area when rotating the bone
    private Interaction hitbox = null;
    private final float hitboxWidth;
    private final float hitboxHeight;

    public DisplayBone(final int partCustomModelData, final float hitboxWidth, final float hitboxHeight) {
        this.customModelData = partCustomModelData;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        //rotation.rotateLocalY((float)Math.PI); //! For some reason, Display models are rotated by 180° on the Y-Axis. This resets that
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
        b.locPos.set(locPos);
        b.origin.set(origin);
        b.rotation.set(rotation);
        return b;
    }




    @Override
    public void spawn(final @NotNull Entity mount){
        final Location location = mount.getLocation();
        super.spawn(mount);

        // Initialize display entity
        displayEntity = (ItemDisplay)location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        displayEntity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        displayEntity.setItemStack(ItemUtils.createItemStackDisplay(Material.BONE, customModelData));
        mount.addPassenger(displayEntity);
        updateDisplayTransform(); //TODO make updates manual

        // Initialize hitboxes
        hitbox = (Interaction)location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        hitbox.setInteractionWidth(hitboxWidth);
        hitbox.setInteractionHeight(hitboxHeight);
        displayEntity.addPassenger(hitbox);
        updateHitbox(); //TODO make updates manual
    }







    @Override
    public void move(final @NotNull Vector3f v) {
        super.move(v);
        updateDisplayTransform(); //TODO make updates manual
        updateHitbox(); //TODO make updates manual
    }
    @Override
    public void moveSelf(final @NotNull Vector3f v){
        super.moveSelf(v);
        updateDisplayTransform(); //TODO make updates manual
        updateHitbox(); //TODO make updates manual
    }
    @Override
    public void moveUpdateOrigin(final @NotNull Vector3f o){
        super.moveUpdateOrigin(o);
        updateDisplayTransform(); //TODO make updates manual
        updateHitbox(); //TODO make updates manual
    }




    @Override
    public void rotateUnsafe(final @NotNull AxisAngle4f r) {
        super.rotateUnsafe(r);
        //rotation.premul(new Quaternionf(r));
        updateDisplayTransform(); //TODO make updates manual
    }
    @Override
    public void rotateUpdateOrigin(final @NotNull Vector3f o, final @NotNull AxisAngle4f r){
        super.rotateUpdateOrigin(o, r);
        //rotation.premul(new Quaternionf(r));
        updateDisplayTransform(); //TODO make updates manual
        updateHitbox(); //TODO make updates manual
    }

    @Override
    public void rotateLocalUnsafe(final @NotNull AxisAngle4f r) {
        super.rotateLocalUnsafe(r);
        //rotation.mul(new Quaternionf(r));
        updateDisplayTransform(); //TODO make updates manual
    }
    @Override
    public void rotateLocalUpdateOrigin(final @NotNull Vector3f o, final @NotNull AxisAngle4f r){
        super.rotateLocalUpdateOrigin(o, r);
        //rotation.mul(new Quaternionf(r));
        updateDisplayTransform(); //TODO make updates manual
        updateHitbox(); //TODO make updates manual
    }





    @Override
    public void mirrorPosX() {
        super.mirrorPosX();
        updateDisplayTransform(); //TODO make updates manual
        updateHitbox(); //TODO make updates manual
    }
    @Override
    public void mirrorPosY() {
        super.mirrorPosY();
        updateDisplayTransform(); //TODO make updates manual
        updateHitbox(); //TODO make updates manual
    }
    @Override
    public void mirrorPosZ() {
        super.mirrorPosZ();
        updateDisplayTransform(); //TODO make updates manual
        updateHitbox(); //TODO make updates manual
    }






    private void updateDisplayTransform(){
        if(!spawned) return;
        Transformation t = new Transformation(
            new Vector3f(getAbsPos()).add(0, 0.5f, 0),                 //! Center to in-game block
            new AxisAngle4f(new Quaternionf(rotation).rotateY(PI)), //! For some reason, Display models are rotated by 180° on the Y-Axis. This resets that
            new Vector3f(1),
            new AxisAngle4f(0, 0, 1, 0)
        );
        displayEntity.setInterpolationDuration(10); //TODO automate and add real interpolation
        displayEntity.setTransformation(t);
        displayEntity.setInterpolationDelay(0);
    }


    private void updateHitbox(){
        if(!spawned) return;
        hitbox.teleport(displayEntity.getLocation().add(Vector.fromJOML(getAbsPos())));
    }
}
