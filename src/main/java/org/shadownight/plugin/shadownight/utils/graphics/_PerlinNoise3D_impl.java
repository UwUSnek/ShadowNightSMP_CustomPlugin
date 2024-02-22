package org.shadownight.plugin.shadownight.utils.graphics;


import org.joml.Vector3d;
import org.joml.Vector3i;
import org.shadownight.plugin.shadownight.utils.math.Func;



public final class _PerlinNoise3D_impl extends _PerlinNoise_impl {
    public _PerlinNoise3D_impl(){
        resetSeed();
    }


    /**
     * Returns a Perlin noise value for a given (x, y) coordinate.
     * @param x x-coordinate to generate noise value for
     * @param y y-coordinate to generate noise value for
     * @return double value in range [0.0, 1.0] for that point
     */
    public double compute(double x, double y, double z, final int gridSize) {
        x /= gridSize;
        y /= gridSize;
        z /= gridSize;

        // Find the four corners of the unit square
        final Vector3i[] unitSquare = getSquareCoords(x, y, z);
        final double[] dotProds = new double[8];  // stores each gradiant • distance val




        // For each corner of the unit square
        for (int i = 0; i < unitSquare.length; i++) {
            Vector3i corner = unitSquare[i];

            // Calculate distance vector and grad vector based on given point
            Vector3d distVec = new Vector3d(x - corner.x, y - corner.y, z - corner.z);
            Vector3i gradVec = selectGradVector(corner.x, corner.y, corner.z);

            // Take dot product: distance vector • gradient vector
            dotProds[i] = distVec.dot(new Vector3d(gradVec.x, gradVec.y, gradVec.z));
        }



        // Fade x and y towards integral vals to improve naturalness of the noise
        double u = fade(x - (int)x);
        double v = fade(y - (int)y);
        double w = fade(z - (int)z);

        // Bilinear interpolation to get the value for the point (using faded vals)
        double output = Func.linearInt(w,
            Func.linearInt(v,
                Func.linearInt(u, dotProds[0], dotProds[1]),
                Func.linearInt(u, dotProds[2], dotProds[3])
            ),
            Func.linearInt(v,
                Func.linearInt(u, dotProds[4], dotProds[5]),
                Func.linearInt(u, dotProds[6], dotProds[7])
            )
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
    private static Vector3i[] getSquareCoords(final double x, final double y, final double z) {
        int x0, x1, y0, y1, z0, z1;  // (x0,y0) is up-left corner, (x1,y1) is bot-right
        x0 = (int) x;
        y0 = (int) y;
        z0 = (int) z;
        x1 = x0 + 1;
        y1 = y0 + 1;
        z1 = z0 + 1;

        return new Vector3i[]{
            new Vector3i(x0, y0, z0), new Vector3i(x1, y0, z0), new Vector3i(x0, y1, z0), new Vector3i(x1, y1, z0),
            new Vector3i(x0, y0, z1), new Vector3i(x1, y0, z1), new Vector3i(x0, y1, z1), new Vector3i(x1, y1, z1)
        };
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
    public Vector3i selectGradVector(final int x, final int y, final int z) {
        final Vector3i[] validGradientVecs = {
            new Vector3i(1, 1,  1), new Vector3i(-1, 1,  1), new Vector3i(1, -1, 1 ), new Vector3i(-1, -1,  1),
            new Vector3i(1, 1, -1), new Vector3i(-1, 1, -1), new Vector3i(1, -1, -1), new Vector3i(-1, -1, -1)
        };
        return validGradientVecs[hash(x, y, z) & 3]; // same as % 4, but faster
    }


    /***
     * Hashing function based on the function given in
     * which is based on Ken Perlin's original.
     * @param x x-coordinate of point to generate hash for
     * @param y y-coordinate of point to generate hash for
     * @return value in range [0, 255] from hash table
     */
    private int hash(int x, int y, int z) {
        x = x & 255;  // same as % 256, but faster
        y = y & 255;
        z = z & 255;
        return permutation[permutation[permutation[x] + y] + z];
    }
}