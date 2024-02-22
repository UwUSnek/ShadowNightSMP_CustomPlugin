package org.shadownight.plugin.shadownight.dungeons.generators;


import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.containers.BlueprintData;
import org.shadownight.plugin.shadownight.utils.containers.RegionBlueprint;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise3D;


public final class GEN_WallMoss extends UtilityClass implements Rnd {


    /**
     * Generates the maze and places all the walls.
     * @param buffer The data buffer
     * @param ft The thickness of the floor
     */
    public static void start(final @NotNull RegionBlueprint buffer, final int ft){
        // Find suitable block column
        for(int i = 0; i < buffer.x; ++i) for(int k = 0; k < buffer.z; ++k) if(buffer.get(i, ft, k) == BlueprintData.WALL) {

            // Find starting Y level
            for(int j = ft + 1; j + 1 < buffer.y; ++j) {
                BlueprintData data = buffer.get(i, j + 1, k);
                if(data == BlueprintData.AIR || data == BlueprintData.WALL_VINE) {
                    final double noise = PerlinNoise3D.compute(i, j, k, 31);
                    if(noise < 0.51 + rnd.nextFloat() * 0.2) buffer.set(i, j, k, BlueprintData.WALL_MOSS);
                    break;
                }
            }
        }
    }
}
