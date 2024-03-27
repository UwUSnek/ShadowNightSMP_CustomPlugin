package org.uwu_snek.shadownight.customMobs.StackableTransforms;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import org.uwu_snek.shadownight.customMobs.Bone;

import java.util.function.Function;





public final class ST_MoveAll extends ST {
    private final Vector3f v;

    /**
     * Moves the bone and its hierarchy by the given amount using the parent's coordinate system.
     * @param duration   The duration of this transformation expressed in ticks
     * @param easing     The type of easing to use
     * @param x The distance on the X-Axis to move the bone for
     * @param y The distance on the Y-Axis to move the bone for
     * @param z The distance on the Z-Axis to move the bone for
     */
    @SuppressWarnings("unused")
    public ST_MoveAll(int duration, @NotNull Function<Double, Double> easing, final float x, final float y, final float z) {
        this(duration, easing, new Vector3f(x, y, z));
    }

    /**
     * Moves the bone and its hierarchy by the given amount using the parent's coordinate system.
     * @param duration   The duration of this transformation expressed in ticks
     * @param easing     The type of easing to use
     * @param v The distance to move the bone for
     */
    @SuppressWarnings("unused")
    public ST_MoveAll(int duration, @NotNull Function<Double, Double> easing, final @NotNull Vector3f v) {
        super(duration, easing);
        this.v = v;
    }




    @Override
    protected void updateBoneValues(final @NotNull Bone b) {
        final float stepLen = computeStepLen();
        updateInitial(b, new Vector3f(v).mul(stepLen));
    }


    public static void updateInitial(final @NotNull Bone b, final @NotNull Vector3f _v){
        b.locPos.add(_v);
        for(Bone c : b.children) updateChild( c, b.getAbsPos());
        b.requestDisplayUpdateSelf();
        b.requestHitboxUpdateSelf();
    }
    public static void updateChild(final @NotNull Bone b, final @NotNull Vector3f o) {
        b.origin.set(o);
        for(Bone c : b.children) updateChild(c, b.getAbsPos());
        b.requestDisplayUpdateSelf();
        b.requestHitboxUpdateSelf();
    }
}
