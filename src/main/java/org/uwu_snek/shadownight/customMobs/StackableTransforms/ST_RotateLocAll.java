package org.uwu_snek.shadownight.customMobs.StackableTransforms;

import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.utils.math.Func;

import java.util.function.Function;




public final class ST_RotateLocAll extends ST {
    private final Quaternionf r;

    /**
     * Rotates the bone and its hierarchy on the given axis using its own local coordinate system.
     * The axis vector is automatically normalized.
     * @param duration   The duration of this transformation expressed in ticks
     * @param easing     The type of easing to use
     * @param x The X value of the axis vector
     * @param y The Y value of the axis vector
     * @param z The Z value of the axis vector
     * @param angle The angle of the rotation around the axis defined by z, y and z
     */
    @SuppressWarnings("unused")
    public ST_RotateLocAll(int duration, @NotNull Function<Double, Double> easing, final float x, final float y, final float z, final float angle){
        this(duration, easing, new Quaternionf().fromAxisAngleRad(x, y, z, angle));
    }

    /**
     * Rotates the bone and its hierarchy on the given axis using its own local coordinate system.
     * @param duration   The duration of this transformation expressed in ticks
     * @param easing     The type of easing to use
     * @param r The rotation
     */
    @SuppressWarnings("unused")
    public ST_RotateLocAll(int duration, @NotNull Function<Double, Double> easing, final @NotNull AxisAngle4f r) {
        this(duration, easing, new Quaternionf(r));
    }

    /**
     * Rotates the bone and its hierarchy on the given axis using its own local coordinate system.
     * @param duration   The duration of this transformation expressed in ticks
     * @param easing     The type of easing to use
     * @param r The rotation
     */
    @SuppressWarnings("unused")
    public ST_RotateLocAll(int duration, @NotNull Function<Double, Double> easing, final @NotNull Quaternionf r) {
        super(duration, easing);
        this.r = r;
    }




    @Override
    protected void updateBoneValues(final @NotNull Bone b) {
        final float stepLen = computeStepLen();
        updateInitial(b, Func.quaternionAngleMul(r, stepLen));
    }


    public static void updateInitial(final @NotNull Bone b, final @NotNull Quaternionf _r) {
        b.rotation.mul(_r);
        for(Bone c : b.children) updateChild(c, _r, b.getAbsPos());
        b.requestDisplayUpdateSelf();
    }
    public static void updateChild(final @NotNull Bone b, final @NotNull Quaternionf _r, final @NotNull Vector3f o){
        b.rotation.mul(_r);
        b.origin.set(o);
        for(Bone c : b.children) updateChild(c, _r, b.getAbsPos());
        b.requestDisplayUpdateSelf();
        b.requestHitboxUpdateSelf();
    }
}
