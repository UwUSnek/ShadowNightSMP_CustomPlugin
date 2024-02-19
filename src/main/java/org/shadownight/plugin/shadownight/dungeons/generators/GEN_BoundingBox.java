package org.shadownight.plugin.shadownight.dungeons.generators;


import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBufferTemplate;
import org.shadownight.plugin.shadownight.utils.graphics.RegionTemplateData;


public final class GEN_BoundingBox extends UtilityClass {
    /**
     * Generates the floor.
     * @param buffer The data buffer
     * @param t The thickness of the floor to generate
     */
    public static void startFloor(@NotNull final RegionBufferTemplate buffer, final int t){
        //      X                                 Z                                 Thickness
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.z; ++j) for(int k = 0; k < t; ++k) {
            buffer.set(i, k, j, RegionTemplateData.FLOOR);
        }
    }

    /**
     * Generates the ceiling.
     * @param buffer The data buffer
     * @param t The thickness of the ceiling to generate
     * @param wh The height of the walls
     * @param ft The thickness of the floor
     */
    public static void startCeiling(@NotNull final RegionBufferTemplate buffer, final int t, final int wh, final int ft){
        //      X                                 Z                                 Thickness
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.z; ++j) for(int k = wh + ft; k < wh + ft + t; ++k) {
            buffer.set(i, k, j, RegionTemplateData.CEILING);
        }
    }


    /**
     * Generates the outer walls.
     * @param buffer The data buffer
     * @param t The thickness of the walls to generate
     * @param h The height of the walls to generate
     * @param ft The thickness of the floor
     * @param wt The thickness of the walls
     */
    public static void startWalls(@NotNull final RegionBufferTemplate buffer, final int t, final int h, final int ft, final int wt){
        final int x = buffer.x - wt * 2;
        final int z = buffer.z - wt * 2;
        //      X                               Height                                        Thickness
        for(int i = -t; i < x + t; ++i) for(int j = -ft; j < h; ++j) for(int k = 1; k < t + 1; ++k) {
            buffer.setShifted(i, j,        -k, RegionTemplateData.WALL);
            buffer.setShifted(i, j, z + k - 1, RegionTemplateData.WALL);
        }

        //      Z                           Height                                        Thickness
        for(int i = 0; i < z ; ++i) for(int j = -ft; j < h; ++j) for(int k = 1; k < t + 1; ++k)  {
            buffer.setShifted(-k,        j, i, RegionTemplateData.WALL);
            buffer.setShifted(x + k - 1, j, i, RegionTemplateData.WALL);
        }
    }
}