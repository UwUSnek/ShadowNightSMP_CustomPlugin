package org.uwu_snek.shadownight.customMobs;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.uwu_snek.shadownight._generated._mob_part_type;

import java.util.ArrayList;




public class Bone {
    protected final static float PI = (float)Math.PI;

    // Bone position and rotation
    protected final Vector3f locPos = new Vector3f(0, 0, 0); // The location of this bone's location relative to its origin
    protected final Vector3f origin = new Vector3f(0, 0, 0); // The location of this bone's origin relative to the root bone
    protected final Quaternionf rotation = new Quaternionf(0, 0, 0, 1);

    // Entity status and update requests
    protected boolean spawned = false;
    protected boolean needsDisplayUpdate = false; public final void requestDisplayUpdate() { needsDisplayUpdate = true; } protected void updateDisplay() { }
    protected boolean needsHitboxUpdate = false;  public final void requestHitboxUpdate()  { needsHitboxUpdate  = true; } protected void updateHitbox()  { }

    public void flushUpdatesSelf(){
        if(needsDisplayUpdate) { updateDisplay(); needsDisplayUpdate = false; }
        if(needsHitboxUpdate)  { updateHitbox();  needsHitboxUpdate  = false; }
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
    protected Vector3f getAbsPos(){ //FIXME cache abs pos to not recalculate it every single time
        return new Vector3f(origin).add(new Vector3f(locPos).rotate(parent.rotation));
    }




    public final void move(final float x, final float y, final float z) {
        move(new Vector3f(x, y, z));
    }
    public void move(final @NotNull Vector3f v) {
        locPos.add(v);
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
        requestDisplayUpdate();
        requestHitboxUpdate();
    }

    public final void moveSelf(final float x, final float y, final float z) {
        moveSelf(new Vector3f(x, y, z));
    }
    public void moveSelf(final @NotNull Vector3f v) {
        locPos.add(v);
        for(Bone c : children) {
            c.locPos.sub(v);
            c.origin.set(getAbsPos());
        }
        requestDisplayUpdate();
        requestHitboxUpdate();
    }

    protected void moveUpdateOrigin(final @NotNull Vector3f o) {
        origin.set(o);
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
        requestDisplayUpdate();
        requestHitboxUpdate();
    }




    //TODO make display updates manual


    public final void rotate(final float angle, final float x, final float y, final float z){
        rotate(new AxisAngle4f(angle, x, y, z));
    }
    public final void rotate(final @NotNull AxisAngle4f r) {
        rotateUnsafe(r.normalize());
    }
    public void rotateUnsafe(final @NotNull AxisAngle4f r) {
        rotation.premul(new Quaternionf(r));
        for(Bone c : children) c.rotateUpdateOrigin(getAbsPos(), new Quaternionf(r));
        requestDisplayUpdate();
    }
    protected void rotateUpdateOrigin(final @NotNull Vector3f o, final @NotNull Quaternionf r){
        rotation.premul(r);
        origin.set(o);
        for(Bone c : children) c.rotateUpdateOrigin(getAbsPos(), r);
        requestDisplayUpdate();
        requestHitboxUpdate();
    }


    public final void rotateLocal(final float angle, final float x, final float y, final float z){
        rotateLocal(new AxisAngle4f(angle, x, y, z));
    }
    public final void rotateLocal(final @NotNull AxisAngle4f r) {
        rotateLocalUnsafe(r.normalize());
    }
    public void rotateLocalUnsafe(final @NotNull AxisAngle4f r) {
        rotation.mul(new Quaternionf(r));
        for(Bone c : children) c.rotateLocalUpdateOrigin(getAbsPos(), new Quaternionf(r));
        requestDisplayUpdate();
    }
    protected void rotateLocalUpdateOrigin(final @NotNull Vector3f o, final @NotNull Quaternionf r){
        rotation.mul(r);
        origin.set(o);
        for(Bone c : children) c.rotateLocalUpdateOrigin(getAbsPos(), r);
        requestDisplayUpdate();
        requestHitboxUpdate();
    }


    public final void setRotation(final float angle, final float x, final float y, final float z){
        setRotation(new AxisAngle4f(angle, x, y, z));
    }
    public final void setRotation(final @NotNull AxisAngle4f r) {
        setRotationUnsafe(r.normalize());
    }
    public void setRotationUnsafe(final @NotNull AxisAngle4f r) {
        final Quaternionf rDiff = new Quaternionf(r).mul(new Quaternionf(rotation).invert());
        rotation.set(r);
        for(Bone c : children) c.rotateUpdateOrigin(getAbsPos(), rDiff);
        requestDisplayUpdate();
    }






    public void mirrorPosX() {
        locPos.x = -locPos.x;
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
        requestHitboxUpdate();
        requestDisplayUpdate();
    }
    public void mirrorPosY() {
        locPos.x = -locPos.x;
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
        requestHitboxUpdate();
        requestDisplayUpdate();
    }
    public void mirrorPosZ() {
        locPos.x = -locPos.x;
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
        requestHitboxUpdate();
        requestDisplayUpdate();
    }
}
