package org.uwu_snek.shadownight.customMobs.StackableTransforms;

import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.customMobs.Bone;

import java.util.function.Function;




public abstract class ST {
    protected Function<Double, Double> easing;
    protected double progress;
    protected double tickAmount;
    protected double lastFraction = 0;

    /**
     * Creates a new stackable transformation.
     * @param duration   The duration of this transformation expressed in ticks
     * @param easing     The type of easing to use
     */
    public ST(final int duration, final @NotNull Function<Double, Double> easing) {
        this.tickAmount = 20d / duration;
        this.easing = easing;
    }

    /**
     * Advances this transformation by 1 tick and updates the values of the Bone <b> and its children.
     * @param b The bone to apply this transformation's step to.
     *          Every step of a specific transformation object must be applied to the same bone.
     *          Doing otherwise will likely cause visual glitches and inconsistencies in the hitbox position.
     */
    public final void tick(final @NotNull Bone b){
        progress += tickAmount;
        updateBoneValues(b);
    }

    /**
     * Calculates the amount of progress the animation will display until the next tick is reached.
     * The amount depends on the Easing Function used, the transformation duration and the current progress.
     * @return The calculated step length
     */
    protected final float computeStepLen(){
        final double curFraction = easing.apply(progress);
        final float r = (float)(curFraction - lastFraction);
        lastFraction = curFraction;
        return r;
    }

    protected abstract void updateBoneValues(final @NotNull Bone b);
}
