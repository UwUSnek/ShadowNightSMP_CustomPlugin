package org.uwu_snek.shadownight.dungeons.generators;


import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.uwu_snek.shadownight.utils.Rnd;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.containers.BlueprintData;
import org.uwu_snek.shadownight.utils.containers.RegionBlueprint;
import org.uwu_snek.shadownight.utils.graphics.PerlinNoise3D;




public final class GEN_WallVines extends UtilityClass implements Rnd {


    /**
     * Generates the maze and places all the walls.
     * @param buffer The data buffer
     * @param normals the normals of each block
     * @param ft The thickness of the floor
     */
    public static void start(final @NotNull RegionBlueprint buffer, final @NotNull Vector2i @NotNull [] @NotNull [] normals, final int ft){
        final double vineChance = 0.46f;
        // Find suitable block column
        for(int i = 0; i < buffer.x; ++i) for(int k = 0; k < buffer.z; ++k) if(buffer.get(i, ft, k) == BlueprintData.WALL) {

            // Find starting Y level
            for(int j = ft + 1; j < buffer.y; ++j) if(buffer.get(i, j, k) == BlueprintData.AIR) {
                final double noise = PerlinNoise3D.compute(i, j, k, 16);

                // Loop down n blocks from the starting position and move the target block on the XZ plane based on the normals when encountering a non-air block
                if(noise < vineChance) for(
                    int i2 = i, j2 = j, k2 = k;                                             // Setup target block coordinates
                    j2 > j - rnd.nextInt(0, (int)((noise + (1 - vineChance))* 20));         // Shorter vines near the edges, max 20 blocks
                    --j2                                                                    // Move down 1 block
                ) {
                    if(j2 < 0) break;                                                           // [Prevent overflow from last for iteration]
                    if(buffer.get(i2, j2, k2) != BlueprintData.AIR) {                           // If block is not air, [Move XZ based on normals]
                        final Vector2i normal = normals[i2][k2];                                    // Choose a random direction if normal is diagonal
                        if(normal.x != 0 && normal.y != 0) if (rnd.nextBoolean()) normal.x = 0; else normal.y = 0;
                        i2 += normal.x;                                                             // Follow normals
                        k2 += normal.y;                                                             // Follow normals
                        if(rnd.nextFloat() > 0.7) ++j2;                                             // Randomize folds to make them look more natural
                        if(i2 < 0 || j2 < 0 || k2 < 0 || i2 >= buffer.x || j2 >= buffer.y || k2 >= buffer.z) break; // [Prevent overflow from repositioning and y adjustments]
                        if(buffer.get(i2, j2, k2) != BlueprintData.AIR) ++j2;                       // Don't poke the floor
                    }
                    if(j2 >= buffer.y) break; // [Prevent overflow from floor anti-poke y adjustments]
                    buffer.set(i2, j2, k2, BlueprintData.WALL_VINE);
                }
                break;
            }
        }
    }
}
