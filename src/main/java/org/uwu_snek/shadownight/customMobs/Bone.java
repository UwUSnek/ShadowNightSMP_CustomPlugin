package org.uwu_snek.shadownight.customMobs;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.customMobs.StackableTransforms.*;
import org.uwu_snek.shadownight.utils.math.Func;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;




public class Bone {
    // Bone position and rotation
    private final LinkedList<ST> activeTransforms = new LinkedList<>();
    public final Vector3f locPos = new Vector3f(0, 0, 0); // The location of this bone's location relative to its origin
    public final Vector3f origin = new Vector3f(0, 0, 0); // The location of this bone's origin relative to the root bone
    public final Quaternionf rotation = new Quaternionf(0, 0, 0, 1);

    // Entity status and update requests
    protected boolean spawned = false;
    protected boolean needsDisplayUpdate = false; protected void updateDisplay() { }
    protected boolean needsHitboxUpdate = false;  protected void updateHitbox()  { }

    public final void requestDisplayUpdateSelf() { needsDisplayUpdate = true; }
    public final void requestHitboxUpdateSelf()  { needsHitboxUpdate  = true; }
    public void flushUpdatesSelf(){
        if(needsDisplayUpdate) { updateDisplay(); needsDisplayUpdate = false; }
        if(needsHitboxUpdate)  { updateHitbox();  needsHitboxUpdate  = false; }
    }

    public void flushUpdates(){ flushUpdatesSelf(); for(Bone c : children) c.flushUpdates(); } //TODO REMOVE


    // Parent and children bones
    protected Bone parent = null;
    public final ArrayList<Bone> children = new ArrayList<>();

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
     * Adds a new transformation at the top of the transformation stack.
     * Transformations are ticked and applied automatically until completed.
     * @param transform The transformation to add
     */
    public final void addTransform(final @NotNull ST transform) {
        activeTransforms.add(transform);
    }

    /**
     * Ticks this bone and all of its children.
     * Stacked transforms are also ticked.
     */
    public final void tick() {
        for(ST t : activeTransforms) t.tick(this);
        activeTransforms.removeIf((t) -> t.getProgress() >= 1);

        flushUpdatesSelf();
        for(Bone b : children) b.tick();
    }




    @SuppressWarnings("unused") public final void instantMoveAll (final float x, final float y, final float z) {
        ST_MoveAll.updateInitial(this, new Vector3f(x, y, z));
    }
    @SuppressWarnings("unused") public final void instantMoveSelf(final float x, final float y, final float z) {
        ST_MoveSelf.updateInitial(this, new Vector3f(x, y, z));
    }
    @SuppressWarnings("unused") public final void instantRotateAbsAll(final float x, final float y, final float z, final float angle) {
        ST_RotateAbsAll.updateInitial(this, new Quaternionf().fromAxisAngleRad(x, y, z, angle));
    }
    @SuppressWarnings("unused") public final void instantRotateLocAll(final float x, final float y, final float z, final float angle) {
        ST_RotateLocAll.updateInitial(this, new Quaternionf().fromAxisAngleRad(x, y, z, angle));
    }
    @SuppressWarnings("unused") public final void instantRotateRelAll(final float x, final float y, final float z, final float angle) {
        ST_RotateRelAll.updateInitial(this, new Quaternionf().fromAxisAngleRad(x, y, z, angle));
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
        for(Bone c : children) ST_RotateAbsAll.updateChild(c, rDiff, getAbsPos());
        requestDisplayUpdateSelf();
    }




    /**
     * Mirrors the X position of the bone using the parent's local coordinate system and the bone's origin as center.
     * This method only affects the position. No bones are ever rotated or truly mirrored.
     * Child bones are moved, but their position relative to the parent stays unchanged.
     */
    @SuppressWarnings("unused")
    public void mirrorPosX() {
        locPos.x = -locPos.x;
        for(Bone c : children) ST_MoveAll.updateChild(c, getAbsPos());
        requestHitboxUpdateSelf();
        requestDisplayUpdateSelf();
    }

    /**
     * Mirrors the Y position of the bone using the parent's local coordinate system and the bone's origin as center.
     * This method only affects the position. No bones are ever rotated or truly mirrored.
     * Child bones are moved, but their position relative to the parent stays unchanged.
     */
    @SuppressWarnings("unused")
    public void mirrorPosY() {
        locPos.x = -locPos.x;
        for(Bone c : children) ST_MoveAll.updateChild(c, getAbsPos());
        requestHitboxUpdateSelf();
        requestDisplayUpdateSelf();
    }

    /**
     * Mirrors the Z position of the bone using the parent's local coordinate system and the bone's origin as center.
     * This method only affects the position. No bones are ever rotated or truly mirrored.
     * Child bones are moved, but their position relative to the parent stays unchanged.
     */
    @SuppressWarnings("unused")
    public void mirrorPosZ() {
        locPos.x = -locPos.x;
        for(Bone c : children) ST_MoveAll.updateChild(c, getAbsPos());
        requestHitboxUpdateSelf();
        requestDisplayUpdateSelf();
    }
}
