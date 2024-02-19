package org.shadownight.plugin.shadownight.dungeons.generators;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise3D;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBufferTemplate;
import org.shadownight.plugin.shadownight.utils.graphics.RegionTemplateData;
import org.shadownight.plugin.shadownight.utils.math.Func;


public final class GEN_WallsDeform extends UtilityClass {
    /**
     * Deforms existing walls.
     * @param buffer The data buffer
     * @param ft The thickness of the floor
     * @param h The height of the walls
     */
    public static void start(@NotNull final RegionBufferTemplate buffer, final int ft, final int h){
        final boolean[][][] tmp = new boolean[buffer.x][buffer.y][buffer.z]; // Defaults to false

        // Create modified copy
        for(int i = 1; i < buffer.x - 1; ++i) for(int j = ft; j < buffer.y; ++j) for(int k = 1; k < buffer.z - 1; ++k) {
            if (buffer.get(i, j, k) == RegionTemplateData.WALL) {
                final int shiftX = (buffer.get(i + 1, j, k) == RegionTemplateData.NULL ? -1 : 0) + (buffer.get(i - 1, j, k) == RegionTemplateData.NULL ? 1 : 0);
                final int shiftZ = (buffer.get(i, j, k + 1) == RegionTemplateData.NULL ? -1 : 0) + (buffer.get(i, j, k - 1) == RegionTemplateData.NULL ? 1 : 0);

                if(shiftX != 0 || shiftZ != 0) {
                    final double noise1 = PerlinNoise3D.compute(i, j, k, 32); // Base layer noise
                    final double noise2 = PerlinNoise3D.compute(i, j, k, 6); // Fine layer noise
                    int scaledNoise = Func.clampMin((int) (Math.sqrt(Func.linearIntSafe(0.5, noise1, noise2) * 16 * ((float) (j - ft) / (h / 2f))) - 0.5), 0); // The number of blocks that have to be removed

                    if (shiftX != 0) {
                        final int target = i + shiftX * scaledNoise; // The block in the current axis that the wall has to be carved until
                        for (int l = i; l != target; l += shiftX) {
                            tmp[Func.clamp(l, 0, buffer.x - 1)][j][k] = true;
                        }
                    }
                    if (shiftZ != 0) {
                        final int target = k + shiftZ * scaledNoise; // The block in the current axis that the wall has to be carved until
                        for (int l = k; l != target; l += shiftZ) {
                            tmp[i][j][Func.clamp(l, 0, buffer.z - 1)] = true;
                        }
                    }
                }
            }
        }

        // Paste copy back into the original buffer
        for(int i = 0; i < buffer.x; ++i) for(int j = ft; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k) {
            if(tmp[i][j][k]) buffer.set(i, j, k, RegionTemplateData.NULL);
        }
    }
}
