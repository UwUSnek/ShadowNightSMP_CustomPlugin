package org.shadownight.plugin.shadownight.dungeons.generators;


import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.containers.BlueprintData;
import org.shadownight.plugin.shadownight.utils.containers.RegionBlueprint;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise3D;

import java.util.ArrayList;
import java.util.Collections;


public final class GEN_WallVines extends UtilityClass {


    /**
     * Generates the maze and places all the walls.
     * @param buffer The data buffer
     * @param normals the normals of each block
     * @param wt The thickness of the walls
     * @param h The height of the walls
     * @param ft The thickness of the floor
     */
    public static void start(@NotNull final RegionBlueprint buffer, @NotNull final Vector2i[][] normals, final int wt, final int h, final int ft){
        for(int i = 0; i < buffer.x; ++i) for(int k = 0; k < buffer.z; ++k) {
            for(int j = ft;; ++j) if(buffer.get(i, j, k) == BlueprintData.AIR) {
                final double noise = PerlinNoise3D.compute(i, j, k, 16);
                break;
            }
        }
    }
}
