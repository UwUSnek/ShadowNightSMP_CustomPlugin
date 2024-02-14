package org.shadownight.plugin.shadownight.dungeons.generators;

import org.bukkit.Material;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.shadownight.plugin.shadownight.dungeons.RegionBuffer;
import org.shadownight.plugin.shadownight.dungeons.shaders.PerlinNoise3D;


public class DeformWalls {
    public static void start(RegionBuffer buffer, Material material, int floorThickness){
        boolean[][][] tmp = new boolean[buffer.x][buffer.y][buffer.z]; // Defaults to false

        // Create modified copy
        for(int i = 1; i < buffer.x - 1; ++i) for(int j = floorThickness; j < buffer.y; ++j) for(int k = 1; k < buffer.z - 1; ++k) {
            if (buffer.get(i, j, k) == material) {
                //boolean pz = ;
                //boolean nz = ;
                //boolean px = buffer.get(i + 1, j, k) == Material.AIR;
                //boolean nx = buffer.get(i - 1, j, k) == Material.AIR;
                int shiftX = (buffer.get(i + 1, j, k) == Material.AIR ? 1 : 0) + (buffer.get(i - 1, j, k) == Material.AIR ? -1 : 0);
                int shiftZ = (buffer.get(i, j, k + 1) == Material.AIR ? 1 : 0) + (buffer.get(i, j, k - 1) == Material.AIR ? -1 : 0);
                if(shiftX + shiftZ != 0) {
                    //TODO maybe use negative noise to remove from a larger wall?
                    double noise = PerlinNoise3D.compute(i, j, k, 8) * 0.99;
                    tmp
                        [Math.max(0, Math.min(i + shiftX * (int)(noise * 4), buffer.x - 1))]
                        [j]
                        [Math.max(0, Math.min(k + shiftZ * (int)(noise * 4), buffer.z - 1))]
                        = true
                    ;
                }


                /*
                double noise1 = PerlinNoise3D.compute(i,        j, k,        8);
                double noise2 = PerlinNoise3D.compute(i + 9999, j, k + 9999, 8);
                double noise3 = PerlinNoise3D.compute(i,        j, k,        8);
                double noise4 = PerlinNoise3D.compute(i + 9999, j, k + 9999, 8);

                int outputX      = Math.max(0, Math.min(i + (int) (noise1 * 2), buffer.x - 1));
                int outputZ      = Math.max(0, Math.min(k + (int) (noise2 * 2), buffer.z - 1));
                int outputHalfX  = Math.max(0, Math.min(i + (int) (noise1    ), buffer.x - 1));
                int outputHalfZ  = Math.max(0, Math.min(k + (int) (noise2    ), buffer.z - 1));

                int outputX2     = Math.max(0, Math.min(i + (int) (-noise3 * 2), buffer.x - 1));
                int outputZ2     = Math.max(0, Math.min(k + (int) (-noise4 * 2), buffer.z - 1));
                int outputHalfX2 = Math.max(0, Math.min(i + (int) (-noise3    ), buffer.x - 1));
                int outputHalfZ2 = Math.max(0, Math.min(k + (int) (-noise4    ), buffer.z - 1));

                tmp[i][j][k] = true;

                tmp[outputX     ][j][outputZ     ] = true;
                tmp[outputHalfX ][j][outputHalfZ ] = true;

                tmp[outputX2    ][j][outputZ2    ] = true;
                tmp[outputHalfX2][j][outputHalfZ2] = true;
                */
            }
        }

        // Paste copy back into the original buffer
        for(int i = 0; i < buffer.x; ++i) for(int j = floorThickness; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k) {
            if(tmp[i][j][k]) buffer.set(i, j, k, material);
        }
    }
}
