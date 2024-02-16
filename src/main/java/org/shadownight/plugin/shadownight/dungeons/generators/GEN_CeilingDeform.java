package org.shadownight.plugin.shadownight.dungeons.generators;

import org.bukkit.Material;
import org.shadownight.plugin.shadownight.dungeons.utils.PerlinNoise2D;
import org.shadownight.plugin.shadownight.dungeons.utils.PerlinNoise3D;
import org.shadownight.plugin.shadownight.dungeons.utils.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.logging.Level;


public class GEN_CeilingDeform {
    public static void start(RegionBuffer buffer, Material material, float[][] wallDist, int floorThickness, int wallHeight) {
        int y = floorThickness + wallHeight - 1; // First y under the flat ceiling
        for(int i = 1; i < buffer.x - 1; ++i) for(int k = 1; k < buffer.z - 1; ++k) {
            // Create noise values
            double noise1 = PerlinNoise2D.compute(i, k, 40);
            double noise2 = PerlinNoise2D.compute(i, k, 2);
            double  mask2 = PerlinNoise2D.compute(i, k, 10);
            mask2 = (mask2 - 0.5 < 0 ? -1d : 1d) * (1 - Math.pow(1 - mask2 - 0.5, 6)); // Increase contrast

            // Smooth out distance gradient
            double avgWallDist = (
                utils.clampMax(wallDist[i - 1][k - 1], 0.85) + utils.clampMax(wallDist[i][k - 1], 0.85) + utils.clampMax(wallDist[i + 1][k - 1], 0.85) +
                utils.clampMax(wallDist[i - 1][k    ], 0.85) + utils.clampMax(wallDist[i][k    ], 0.85) + utils.clampMax(wallDist[i + 1][k    ], 0.85) +
                utils.clampMax(wallDist[i - 1][k + 1], 0.85) + utils.clampMax(wallDist[i][k + 1], 0.85) + utils.clampMax(wallDist[i + 1][k + 1], 0.85)
            ) / 9 + 0.15f;

            // Calculate heights and modify the buffer
            double wallShift = (1 - avgWallDist) * 15;
            double noise = noise1 * 14 + utils.clampMax(Math.pow(noise2, 6) * 25, 15) * avgWallDist * (mask2 + 0.5);
            int h = (int)(wallShift + noise);
            for(int j = y; j > y - h; --j) {
                if(buffer.get(i, j, k) == Material.AIR) buffer.set(i, j, k, material);
            }
        }
    }
}
