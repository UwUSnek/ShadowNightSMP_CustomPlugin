package org.uwu_snek.shadownight.customMobs;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.utils.math.Func;

import java.util.ArrayList;




public class Bone {
    // Bone position and rotation
    public final Vector3f locPos = new Vector3f(0, 0, 0); // The location of this bone's location relative to its origin
    public final Vector3f origin = new Vector3f(0, 0, 0); // The location of this bone's origin relative to the root bone
    protected final Quaternionf rotation = new Quaternionf(0, 0, 0, 1);

    // Entity status and update requests
    protected boolean spawned = false;
    protected boolean needsDisplayUpdate = false; protected void updateDisplay() { }
    protected boolean needsHitboxUpdate = false;  protected void updateHitbox()  { }


    public final void requestDisplayUpdateSelf() {
        needsDisplayUpdate = true;
    }
    public final void requestHitboxUpdateSelf()  {
        needsHitboxUpdate  = true;
    }
    public void flushUpdatesSelf(){
        if(needsDisplayUpdate) { updateDisplay(); needsDisplayUpdate = false; }
        if(needsHitboxUpdate)  { updateHitbox();  needsHitboxUpdate  = false; }
    }


    public final void requestDisplayUpdate() {
        requestDisplayUpdateSelf();
        for(Bone c : children) c.requestDisplayUpdate();
    }
    public final void requestHitboxUpdate()  {
        requestHitboxUpdateSelf();
        for(Bone c : children) c.requestHitboxUpdate();
    }
    public void flushUpdates(){
        flushUpdatesSelf();
        for(Bone c : children) c.flushUpdates();
    }


    // Parent and children bones
    protected Bone parent = null;
    protected final ArrayList<Bone> children = new ArrayList<>();

    /**
     * Returns the children of this bone.
     * @return The children as a list of bones
     */
    public @NotNull ArrayList<@NotNull Bone> getChildren(){
        return children;
    }

    /**
     * Returns the child that uses the part type <type>. This function is always O(1).
     * If multiple children have this type, one of them will be returned, but it is not known which one.
     * If no child has this type, the function call will have undefined behaviour.
     * @param type The type of part to get
     * @return The child bone that uses that part
     */
    public @NotNull Bone getChild(final @NotNull _mob_part_type type){
        return children.get(type.getChildIndex());
    }



    public Bone(){ }


    /**
     * Creates a full copy of this bone which includes its entire hierarchy and all of its child displays and hitboxes.
     * The parent of this bone is not preserved, but the ones of child bones are set to the respective copied parent.
     * Copied bones are not spawned automatically on creation.
     */
    public Bone createDeepCopy(){
        Bone b = createShallowCopy();
        for(Bone c : children) b.addChild(c.createDeepCopy());
        return b;
    }

    /**
     * Creates a copy of this bone which doesn't include any of its children, but contains a copy of its direct displays and hitbox.
     * The parent is also not preserved.
     * Copied bones are not spawned automatically on creation.
     */
    public Bone createShallowCopy(){
        final Bone b = new Bone();
        b.locPos.set(locPos);
        b.origin.set(origin);
        return b;
    }




    /**
     * Spawns any tangible and/or visible bone in this bone's hierarchy in the world
     * @param mount The mount entity used to support the bones
     */
    public void spawn(final @NotNull Entity mount){
        spawned = true;
        for(Bone b : children) b.spawn(mount);
    }

    /**
     * Adds a child to this bone
     * @param bone The child to add
     */
    public void addChild(@NotNull Bone bone) {
        children.add(bone);
        bone.parent = this;
    }




    /**
     * Calculates the absolute position of this bone (This is relative to the mount's location)
     * @return A copy of the vector representing the absolute position
     */
    public Vector3f getAbsPos(){ //FIXME cache abs pos to not recalculate it every single time
        return new Vector3f(origin).add(new Vector3f(locPos).rotate(parent.rotation));
    }









    /**
     * Moves the bone by the given amount using the parent's coordinate system, without moving any of its children.
     * @param x The amount of movement on the X-Axis
     * @param y The amount of movement on the Y-Axis
     * @param z The amount of movement on the Z-Axis
     */
    public final void moveSelf(final float x, final float y, final float z) {
        moveSelf(new Vector3f(x, y, z));
    }
    public void moveSelf(final @NotNull Vector3f v) {
        locPos.add(v);
        for(Bone c : children) {
            c.locPos.sub(v);
            c.origin.set(getAbsPos());
        }
        requestDisplayUpdateSelf();
        requestHitboxUpdateSelf();
    }








    /**
     * Rotates the bone and its hierarchy on the given axis using the world's coordinate system.
     * The axis vector is automatically normalized.
     * @param angle The angle
     * @param x The x value of the axis vector
     * @param y The y value of the axis vector
     * @param z The z value of the axis vector
     */
    public final void rotateAbsolute(final float angle, final float x, final float y, final float z){
        rotateAbsolute(new AxisAngle4f(angle, x, y, z));
    }
    public final void rotateAbsolute(final @NotNull AxisAngle4f r) {
        rotateAbsoluteUnsafe(r.normalize());
    }
    public void rotateAbsoluteUnsafe(final @NotNull AxisAngle4f r) {
        final Quaternionf qr = new Quaternionf(r);
        rotation.premul(qr);
        for(Bone c : children) c.rotateAbsoluteUpdateOrigin(getAbsPos(), qr);
        requestDisplayUpdateSelf();
    }
    protected void rotateAbsoluteUpdateOrigin(final @NotNull Vector3f o, final @NotNull Quaternionf r){
        rotation.premul(r);
        origin.set(o);
        for(Bone c : children) c.rotateAbsoluteUpdateOrigin(getAbsPos(), r);
        requestDisplayUpdateSelf();
        requestHitboxUpdateSelf();
    }



    /**
     * Rotates the bone and its hierarchy on the given axis using the parent's local coordinate system.
     * The axis vector is automatically normalized.
     * @param angle The angle
     * @param x The x value of the axis vector
     * @param y The y value of the axis vector
     * @param z The z value of the axis vector
     */
    public final void rotateRelative(final float angle, final float x, final float y, final float z){
        rotateRelative(new AxisAngle4f(angle, x, y, z));
    }
    public final void rotateRelative(final @NotNull AxisAngle4f r) {
        rotateRelativeUnsafe(r.normalize());
    }
    public void rotateRelativeUnsafe(final @NotNull AxisAngle4f r) { //FIXME override in root if this accesses the parent bone
        final Quaternionf qr = new Quaternionf(r);
        locPos.rotate(qr);
        rotation.premul(qr);
        for(Bone c : children) c.rotateAbsoluteUpdateOrigin(getAbsPos(), qr);
        requestDisplayUpdateSelf();
        requestHitboxUpdateSelf();
    }




    /**
     * Rotates the bone and its hierarchy on the given axis using its own local coordinate system.
     * The axis vector is automatically normalized.
     * @param angle The angle
     * @param x The x value of the axis vector
     * @param y The y value of the axis vector
     * @param z The z value of the axis vector
     */
    public final void rotateLocal(final float angle, final float x, final float y, final float z){
        rotateLocal(new AxisAngle4f(angle, x, y, z));
    }
    public final void rotateLocal(final @NotNull AxisAngle4f r) {
        rotateLocalUnsafe(r.normalize());
    }
    public void rotateLocalUnsafe(final @NotNull AxisAngle4f r) {
        final Quaternionf qr = new Quaternionf(r);
        rotation.mul(qr);
        for(Bone c : children) c.rotateLocalUpdateOrigin(getAbsPos(), qr);
        requestDisplayUpdateSelf();
    }
    protected void rotateLocalUpdateOrigin(final @NotNull Vector3f o, final @NotNull Quaternionf r){
        rotation.mul(r);
        origin.set(o);
        for(Bone c : children) c.rotateLocalUpdateOrigin(getAbsPos(), r);
        requestDisplayUpdateSelf();
        requestHitboxUpdateSelf();
    }




    /**
     * Sets the rotation of the bone and its hierarchy to the given angle and axis using its own local coordinate system.
     * The axis vector is automatically normalized.
     * @param angle The angle
     * @param x The x value of the axis vector
     * @param y The y value of the axis vector
     * @param z The z value of the axis vector
     */
    public final void setRotation(final float angle, final float x, final float y, final float z){
        setRotation(new AxisAngle4f(angle, x, y, z));
    }
    public final void setRotation(final @NotNull AxisAngle4f r) {
        setRotationUnsafe(r.normalize());
    }
    public void setRotationUnsafe(final @NotNull AxisAngle4f r) {
        final Quaternionf rDiff = new Quaternionf(r).mul(new Quaternionf(rotation).invert());
        rotation.set(r);
        for(Bone c : children) c.rotateAbsoluteUpdateOrigin(getAbsPos(), rDiff);
        requestDisplayUpdateSelf();
    }








    /**
     * Mirrors the X position of the bone using the parent's local coordinate system and the bone's origin as center.
     * This method only affects the position. No bones are ever rotated or truly mirrored.
     * Child bones are moved, but their position relative to the parent stays unchanged.
     */
    public void mirrorPosX() {
        locPos.x = -locPos.x;
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
        requestHitboxUpdateSelf();
        requestDisplayUpdateSelf();
    }

    /**
     * Mirrors the Y position of the bone using the parent's local coordinate system and the bone's origin as center.
     * This method only affects the position. No bones are ever rotated or truly mirrored.
     * Child bones are moved, but their position relative to the parent stays unchanged.
     */
    public void mirrorPosY() {
        locPos.x = -locPos.x;
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
        requestHitboxUpdateSelf();
        requestDisplayUpdateSelf();
    }

    /**
     * Mirrors the Z position of the bone using the parent's local coordinate system and the bone's origin as center.
     * This method only affects the position. No bones are ever rotated or truly mirrored.
     * Child bones are moved, but their position relative to the parent stays unchanged.
     */
    public void mirrorPosZ() {
        locPos.x = -locPos.x;
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
        requestHitboxUpdateSelf();
        requestDisplayUpdateSelf();
    }
}
