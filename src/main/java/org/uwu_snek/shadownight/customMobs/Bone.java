package org.uwu_snek.shadownight.customMobs;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;




public class Bone {
    protected final Vector3f locPos = new Vector3f(0, 0, 0); // The location of this bone's location relative to its origin
    protected final Vector3f origin = new Vector3f(0, 0, 0); // The location of this bone's origin relative to the root bone
    protected final Quaternionf rotation = new Quaternionf(0, 0, 0, 1);
    protected boolean spawned = false;


    private final ArrayList<Bone> children = new ArrayList<>();
    public ArrayList<Bone> getChildren(){ return children; }
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
    }




    /**
     * Calculates the absolute position of this bone (This is relative to the mount's location)
     * @return A copy of the vector representing the absolute position
     */
    protected Vector3f getAbsPos(){
        return new Vector3f(origin).add(locPos);
    }




    public final void move(final float x, final float y, final float z) {
        move(new Vector3f(x, y, z));
    }
    public void move(final @NotNull Vector3f v) {
        locPos.add(v);
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
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
    }

    protected void moveUpdateOrigin(final @NotNull Vector3f o) {
        origin.set(o);
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
    }




//TODO make display updates manual
    public final void rotate(final float angle, final float x, final float y, final float z){
        rotate(new AxisAngle4f(angle, x, y, z));
    }
    public final void rotate(final @NotNull AxisAngle4f r) {
        rotateUnsafe(r.normalize());
    }


    public final void rotateLocal(final float angle, final float x, final float y, final float z){
        rotateLocal(new AxisAngle4f(angle, x, y, z));
    }
    public final void rotateLocal(final @NotNull AxisAngle4f r) {
        rotateLocalUnsafe(r.normalize());
    }


    public void rotateUnsafe(final @NotNull AxisAngle4f r) {
        rotation.premul(new Quaternionf(r));
        for(Bone c : children) c.rotateUpdateOrigin(getAbsPos(), r);
    }
    protected void rotateUpdateOrigin(final @NotNull Vector3f o, final @NotNull AxisAngle4f r){
        rotation.premul(new Quaternionf(r));
        locPos.rotateAxis(r.angle, r.x, r.y, r.z);
        origin.set(o);
        for(Bone c : children) c.rotateUpdateOrigin(getAbsPos(), r);
    }


    public void rotateLocalUnsafe(final @NotNull AxisAngle4f r) {
        rotation.mul(new Quaternionf(r));
        for(Bone c : children) c.rotateLocalUpdateOrigin(getAbsPos(), r);
    }
    protected void rotateLocalUpdateOrigin(final @NotNull Vector3f o, final @NotNull AxisAngle4f r){
        //locPos.rotateAxis(r.angle, r.x, r.y, r.z); //FIXME use local rotation. This one is world coordinates
        final Quaternionf oldRotation = new Quaternionf(rotation);
        rotation.mul(new Quaternionf(r));
        final AxisAngle4f rotationDiff = new AxisAngle4f(new Quaternionf(oldRotation).mul(new Quaternionf(r).invert()));
        locPos.rotateAxis(rotationDiff.angle, rotationDiff.x, rotationDiff.y, rotationDiff.z); //FIXME use local rotation. This one is world coordinates
        origin.set(o);
        for(Bone c : children) c.rotateLocalUpdateOrigin(getAbsPos(), r);
    }




    public void mirrorPosX() {
        locPos.x = -locPos.x;
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
    }
    public void mirrorPosY() {
        locPos.x = -locPos.x;
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
    }
    public void mirrorPosZ() {
        locPos.x = -locPos.x;
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
    }
}
