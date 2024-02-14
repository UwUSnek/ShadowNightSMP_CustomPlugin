package org.shadownight.plugin.shadownight.dungeons.generators;

import org.bukkit.Material;
import org.shadownight.plugin.shadownight.dungeons.RegionBuffer;
import org.shadownight.plugin.shadownight.dungeons.shaders.PerlinNoise2D;
import org.shadownight.plugin.shadownight.dungeons.shaders.PerlinNoise3D;


public class DeformWalls {
    public static void start(RegionBuffer buffer, Material material, int floorThickness){
        boolean[][][] tmp = new boolean[buffer.x][buffer.y][buffer.z]; // Defaults to false

        // Create modified copy
        for(int i = 0; i < buffer.x; ++i) for(int j = floorThickness; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k) {
            if (buffer.get(i, j, k) == material) {
                double noise = PerlinNoise3D.compute(i, j, k, 5);
                int outputX = Math.max(0, Math.min(i + (int) ((noise - 0.5) * 2 * 4), buffer.x - 1));
                int outputZ = Math.max(0, Math.min(k + (int) ((noise - 0.5) * 2 * 4), buffer.z - 1));
                tmp[outputX][j][outputZ] = true;
            }
        }

        // Paste copy back into the original buffer
        for(int i = 0; i < buffer.x; ++i) for(int j = floorThickness; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k) {
            if(tmp[i][j][k]) buffer.set(i, j, k, material);
        }
    }
}
