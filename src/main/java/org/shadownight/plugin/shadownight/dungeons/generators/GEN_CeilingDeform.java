package org.shadownight.plugin.shadownight.dungeons.generators;

import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise2D;
import org.shadownight.plugin.shadownight.utils.containers.RegionBlueprint;
import org.shadownight.plugin.shadownight.utils.containers.BlueprintData;
import org.shadownight.plugin.shadownight.utils.math.Func;


public final class GEN_CeilingDeform extends UtilityClass {
    /**
     * Deforms the existing ceiling.
     * @param buffer The data buffer
     * @param dist The gradient containing the distance of each block from the closest wall
     * @param ft The thickness of the floor
     * @param h The height of the walls
     */
    public static void start(final @NotNull RegionBlueprint buffer, final float @NotNull [] @NotNull [] dist, final int ft, final int h) {
        final int y = ft + h - 1; // First y under the flat ceiling
        for(int i = 1; i < buffer.x - 1; ++i) for(int k = 1; k < buffer.z - 1; ++k) {
            // Create noise values
            final double noise1 = PerlinNoise2D.compute(i, k, 40);
            final double noise2 = PerlinNoise2D.compute(i, k, 2);
            double  mask2 = PerlinNoise2D.compute(i, k, 10);
            mask2 = (mask2 - 0.5 < 0 ? -1d : 1d) * (1 - Math.pow(1 - mask2 - 0.5, 6)); // Increase contrast //TODO move this to Func class

            // Smooth out distance gradient
            final double avgWallDist = (
                Func.clampMax(dist[i - 1][k - 1], 0.85) + Func.clampMax(dist[i][k - 1], 0.85) + Func.clampMax(dist[i + 1][k - 1], 0.85) +
                Func.clampMax(dist[i - 1][k    ], 0.85) + Func.clampMax(dist[i][k    ], 0.85) + Func.clampMax(dist[i + 1][k    ], 0.85) +
                Func.clampMax(dist[i - 1][k + 1], 0.85) + Func.clampMax(dist[i][k + 1], 0.85) + Func.clampMax(dist[i + 1][k + 1], 0.85)
            ) / 9 + 0.15f;

            // Calculate heights and modify the buffer
            final double vineHeight = Func.clampMax(Math.pow(noise2, 6) * 25, 15) * avgWallDist * (mask2 + 0.5);
            final double noise = noise1 * 14 + vineHeight;
            final int ch = (int)((1 - avgWallDist) * 15 + noise);
            final double vineY = y - ch + vineHeight;
            for(int j = y; j > y - ch; --j) {
                if(buffer.get(i, j, k) == BlueprintData.AIR) buffer.set(i, j, k, j > vineY ? BlueprintData.CEILING : BlueprintData.CEILING_VINE);
            }
        }
    }
}
