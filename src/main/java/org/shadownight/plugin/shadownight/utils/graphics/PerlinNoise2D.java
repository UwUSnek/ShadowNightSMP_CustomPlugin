package org.shadownight.plugin.shadownight.utils.graphics;


import org.joml.Vector2d;
import org.joml.Vector2i;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.math.Func;


public final class PerlinNoise2D  extends UtilityClass implements PerlinNoise {
    /**
     * Returns a Perlin noise value for a given (x, y) coordinate.
     * @param x x-coordinate to generate noise value for
     * @param y y-coordinate to generate noise value for
     * @return double value in range [0.0, 1.0] for that point
     */
    public static double compute(double x, double y, int gridSize) {
        x /= gridSize;
        y /= gridSize;

        // Find the four corners of the unit square
        Vector2i[] unitSquare = getSquareCoords(x, y);
        double[] dotProds = new double[4];  // stores each gradiant • distance val

        // For each corner of the unit square
        for (int i = 0; i < unitSquare.length; i++) {
            Vector2i corner = unitSquare[i];

            // Calculate distance vector and grad vector based on given point
            Vector2d distVec = new Vector2d(x - corner.x, y - corner.y);
            Vector2i gradVec = selectGradVector(corner.x, corner.y);

            // Take dot product: distance vector • gradient vector
            dotProds[i] = distVec.dot(new Vector2d(gradVec.x, gradVec.y));
        }


        // Fade x and y towards integral vals to improve naturalness of the noise
        double u = PerlinNoise.fade(x - (int)x);
        double v = PerlinNoise.fade(y - (int)y);

        // Bilinear interpolation to get the value for the point (using faded vals)
        double output = Func.linearInt(v,
            Func.linearInt(u, dotProds[0], dotProds[1]),
            Func.linearInt(u, dotProds[2], dotProds[3])
        );

        return (output + 1) / 2; // Shift from output range of [-1, 1] to [0, 1]
    }


    /**
     * Determine the coordinates of the 4 corners of a given point's unit square
     * @param x x-coordinate of point to find unit square of
     * @param y y-coordinate of point to find unit square of
     * @return list of coordinate pairs in the format {{x0, y0}, {x1, y0}, {x0, y1}, {x1, y1}}
     *         (that is, {{top lt}, {top rt}, {bot lt}, {bot rt}})
     */
    private static Vector2i[] getSquareCoords(double x, double y) {
        int x0, x1, y0, y1;  // (x0,y0) is up-left corner, (x1,y1) is bot-right
        x0 = (int) x;
        y0 = (int) y;
        x1 = x0 + 1;
        y1 = y0 + 1;

        return new Vector2i[]{new Vector2i(x0, y0), new Vector2i(x1, y0), new Vector2i(x0, y1), new Vector2i(x1, y1)};
    }


    /**
     * For any given (x, y) coordinate, pseudorandomly select a gradient vector
     * using the hash method.
     * @param x integer x-coordinate of grid to generate a gradient vector for
     * @param y integer y-coordinate of grid to generate a gradient vector for
     * @return The gradient vector for that point, in the format {x, y}, where
     *         the vector is measured with its tail at (0, 0) and its head at
     *         (x, y).
     */
    public static Vector2i selectGradVector(int x, int y) {
        final Vector2i[] validGradientVecs = {new Vector2i(1, 1), new Vector2i(-1, 1), new Vector2i(1, -1), new Vector2i(-1, -1)};
        return validGradientVecs[hash(x, y) & 3]; // same as % 4, but faster
    }


    /***
     * Hashing function based on the function given in
     * which is based on Ken Perlin's original.
     * @param x x-coordinate of point to generate hash for
     * @param y y-coordinate of point to generate hash for
     * @return value in range [0, 255] from hash table
     */
    private static int hash(int x, int y) {
        x = x & 255;  // same as % 256, but faster
        y = y & 255;
        return permutation[permutation[x] + y];
    }
}
