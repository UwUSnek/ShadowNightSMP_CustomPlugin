package org.shadownight.plugin.shadownight.dungeons.generators;

import org.bukkit.Material;
import org.shadownight.plugin.shadownight.dungeons.RegionBuffer;
import org.shadownight.plugin.shadownight.dungeons.shaders.PerlinNoise3D;
import org.shadownight.plugin.shadownight.utils.utils;


public class GEN_WallsDeform {
    public static void start(RegionBuffer buffer, Material material, int floorThickness, int h){
        boolean[][][] tmp = new boolean[buffer.x][buffer.y][buffer.z]; // Defaults to false

        // Create modified copy
        for(int i = 1; i < buffer.x - 1; ++i) for(int j = floorThickness; j < buffer.y; ++j) for(int k = 1; k < buffer.z - 1; ++k) {
            if (buffer.get(i, j, k) == material) {
                int shiftX = (buffer.get(i + 1, j, k) == Material.AIR ? -1 : 0) + (buffer.get(i - 1, j, k) == Material.AIR ? 1 : 0);
                int shiftZ = (buffer.get(i, j, k + 1) == Material.AIR ? -1 : 0) + (buffer.get(i, j, k - 1) == Material.AIR ? 1 : 0);

                if(shiftX != 0 || shiftZ != 0) {
                    double noise1 = PerlinNoise3D.compute(i, j, k, 32); // Base layer noise
                    double noise2 = PerlinNoise3D.compute(i, j, k, 6); // Fine layer noise
                    int scaledNoise = Math.max(0, (int) (Math.sqrt(utils.linearInt(0.5, noise1, noise2) * 16 * ((float) (j - floorThickness) / h)) - 0.5)); // The number of blocks that have to be removed

                    if (shiftX != 0) {
                        int target = i + shiftX * scaledNoise; // The block in the current axis that the wall has to be carved until
                        for (int l = i; l != target; l += shiftX) {
                            tmp[Math.max(0, Math.min(l, buffer.x - 1))][j][k] = true;
                        }
                    }
                    if (shiftZ != 0) {
                        int target = k + shiftZ * scaledNoise; // The block in the current axis that the wall has to be carved until
                        for (int l = k; l != target; l += shiftZ) {
                            tmp[i][j][Math.max(0, Math.min(l, buffer.z - 1))] = true;
                        }
                    }
                }
            }
        }

        // Paste copy back into the original buffer
        for(int i = 0; i < buffer.x; ++i) for(int j = floorThickness; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k) {
            if(tmp[i][j][k]) buffer.set(i, j, k, Material.AIR);
        }
    }
}
