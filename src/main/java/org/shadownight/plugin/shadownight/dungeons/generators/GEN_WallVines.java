package org.shadownight.plugin.shadownight.dungeons.generators;


import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.containers.BlueprintData;
import org.shadownight.plugin.shadownight.utils.containers.RegionBlueprint;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise3D;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;


public final class GEN_WallVines extends UtilityClass implements Rnd {


    /**
     * Generates the maze and places all the walls.
     * @param buffer The data buffer
     * @param normals the normals of each block
     * @param ft The thickness of the floor
     */
    public static void start(@NotNull final RegionBlueprint buffer, @NotNull final Vector2i[][] normals, final int ft){
        final double vineChance = 0.46f;
        // Find suitable block column
        for(int i = 0; i < buffer.x; ++i) for(int k = 0; k < buffer.z; ++k) if(buffer.get(i, ft, k) == BlueprintData.WALL) {

            // Find starting Y level
            for(int j = ft + 1; j < buffer.y; ++j) if(buffer.get(i, j, k) == BlueprintData.AIR) {
                final double noise = PerlinNoise3D.compute(i, j, k, 16);

                // Loop down n blocks from the starting position and move the target block on the XZ plane based on the normals when encountering a non-air block
                if(noise < vineChance) for(
                    int i2 = i, j2 = j, k2 = k;                                             // Setup target block coordinates
                    i2 >= 0 && k2 >= 0 && i2 < buffer.x && k2 < buffer.z && j2 >= 0 &&      // Don't overflow the buffer
                    j2 > j - rnd.nextInt(0, (int)((noise + (1 - vineChance))* 20));         // Shorter vines near the edges, max 20 blocks
                    --j2                                                                    // Move down 1 block
                ) {
                    if(buffer.get(i2, j2, k2) != BlueprintData.AIR) {
                        final Vector2i normal = normals[i2][k2];
                        i2 += normal.x;                                       // Follow normals
                        k2 += normal.y;                                       // Follow normals
                        if(rnd.nextFloat() > 0.7) ++j2;                       // Randomize folds to make them look more natural
                        if(buffer.get(i2, j2, k2) != BlueprintData.AIR) ++j2; // Don't poke the floor
                    }
                    buffer.set(i2, j2, k2, BlueprintData.WALL_VINE);
                }
                break;
            }
        }
    }
}
