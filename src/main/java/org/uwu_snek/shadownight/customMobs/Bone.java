package org.uwu_snek.shadownight.customMobs;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.ArrayList;




public class Bone {
    protected Vector3f locPos = new Vector3f(0, 0, 0); // The location of this bone's location relative to its origin
    protected Vector3f origin = new Vector3f(0, 0, 0); // The location of this bone's origin relative to the root bone
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
        //bone.parent = this;
    }




    /**
     * Calculates the absolute position of this bone (This is relative to the mount's location)
     * @return A copy of the vector representing the absolute position
     */
    protected Vector3f getAbsPos(){
        return new Vector3f(origin).add(locPos);
    }




    public final void move(final int duration, final float x, final float y, final float z) {
        move(duration, new Vector3f(x, y, z));
    }
    public void move(final int duration, final @NotNull Vector3f v) {
        locPos.add(v);
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
    }

    public final void moveSelf(final int duration, final float x, final float y, final float z) {
        moveSelf(duration, new Vector3f(x, y, z));
    }
    public void moveSelf(final int duration, final @NotNull Vector3f v) {
        locPos.add(v);
        for(Bone c : children) c.locPos.sub(v);
    }

    protected void moveUpdateOrigin(final @NotNull Vector3f o) {
        origin.set(o);
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
    }




//TODO make display updates manual
    public final void rotate(final int duration, final float angle, final float x, final float y, final float z){
        rotate(duration, new AxisAngle4f(angle, x, y, z));
    }
    public final void rotate(final int duration, final @NotNull AxisAngle4f r) {
        rotateUnsafe(duration, r.normalize());
    }
    public void rotateUnsafe(final int duration, final @NotNull AxisAngle4f r) {
        for(Bone c : children) c.rotateUpdateOrigin(duration, getAbsPos(), r);
    }
    protected void rotateUpdateOrigin(final int duration, final @NotNull Vector3f o, final @NotNull AxisAngle4f r){
        locPos.rotateAxis(r.angle, r.x, r.y, r.z);
        origin.set(o);
        for(Bone c : children) c.rotateUpdateOrigin(duration, getAbsPos(), r);
    }




    public void mirrorPosX() {
        locPos.x = -locPos.x + 1; //! Add 16 model units (1 block) to account for the model center being at [8,8,8]
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
    }
    public void mirrorPosY() {
        locPos.x = -locPos.x + 1; //! Add 16 model units (1 block) to account for the model center being at [8,8,8]
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
    }
    public void mirrorPosZ() {
        locPos.x = -locPos.x + 1; //! Add 16 model units (1 block) to account for the model center being at [8,8,8]
        for(Bone c : children) c.moveUpdateOrigin(getAbsPos());
    }
}
