package org.shadownight.plugin.shadownight.utils.math;


import org.shadownight.plugin.shadownight.utils.UtilityClass;



public final class Func extends UtilityClass {
    /**
     * Checks if a double value is within a certain threshold from a target value.
     * This is used to avoid precision related problems when comparing double values.
     * @param n The value to check
     * @param target The target value
     * @param threshold The threshold to use
     * @return True if the value is withing the threshold, false otherwise
     */
    @SuppressWarnings("unused")
    public static boolean doubleEquals(final double n, final double target, final double threshold) {
        return !(n < target - threshold || n > target + threshold);
    }
    /**
     * Checks if a float value is within a certain threshold from a target value.
     * This is used to avoid precision related problems when comparing float values.
     * @param n The value to check
     * @param target The target value
     * @param threshold The threshold to use
     * @return True if the value is withing the threshold, false otherwise
     */
    @SuppressWarnings("unused")
    public static boolean floatEquals(final float n, final float target, final float threshold) {
        return !(n < target - threshold || n > target + threshold);
    }



    /**
     * Calculates the linear interpretation between the values <a> and <b>.
     * If you cannot make sure that <b> is always lower than <a>, use the linearIntSafe function.
     * @param progress The value to use to calculate the interpolation [0-1]
     * @param a The first value [< b]
     * @param b The second value [> a]
     * @return Linearly interpolated value between a and b
     */
    @SuppressWarnings("unused")
    public static double linearInt(final double progress, final double a, final double b) {
        return a + (progress * (b - a));
    }

    /**
     * Calculates the linear interpretation between the values <a> and <b>.
     * If <b> is always lower than <a>, using the linearInt function will likely be faster.
     * @param progress The value to use to calculate the interpolation [0-1]
     * @param a The first value
     * @param b The second value
     * @return Linearly interpolated value between a and b
     */
    @SuppressWarnings("unused")
    public static double linearIntSafe(final double progress, double a, double b) {
        if(a > b) { double tmp = a; a = b; b = tmp; }
        return a + (progress * (b - a));
    }





    /**
     * Equivalent to Math.max(n, min)
     * @param n The value to clamp
     * @param min The minimum value that can be returned
     * @return The clamped value
     */
    @SuppressWarnings("unused")
    public static double clampMin(final double n, final double min) {
        return Math.max(min, n);
    }
    /**
     * Equivalent to Math.max(n, min)
     * @param n The value to clamp
     * @param min The minimum value that can be returned
     * @return The clamped value
     */
    @SuppressWarnings("unused")
    public static int clampMin(final int n, final int min) {
        return Math.max(min, n);
    }



    /**
     * Equivalent to Math.min(n, max)
     * @param n The value to clamp
     * @param max The maximum value that can be returned
     * @return The clamped value
     */
    @SuppressWarnings("unused")
    public static double clampMax(final double n, final double max) {
        return Math.min(n, max);
    }
    /**
     * Equivalent to Math.min(n, max)
     * @param n The value to clamp
     * @param max The maximum value that can be returned
     * @return The clamped value
     */
    @SuppressWarnings("unused")
    public static int clampMax(final int n, final int max) {
        return Math.min(n, max);
    }



    /**
     * Clamps a value between <min> and <max>
     * @param n The value to clamp
     * @param min The minimum value that can be returned
     * @param max The maximum value that can be returned
     * @return The clamped value
     */
    @SuppressWarnings("unused")
    public static double clamp(final double n, final double min, final double max) {
        return Math.max(min, Math.min(n, max));
    }
    /**
     * Clamps a value between <min> and <max>
     * @param n The value to clamp
     * @param min The minimum value that can be returned
     * @param max The maximum value that can be returned
     * @return The clamped value
     */
    @SuppressWarnings("unused")
    public static int clamp(final int n, final int min, final int max) {
        return Math.max(min, Math.min(n, max));
    }
}
