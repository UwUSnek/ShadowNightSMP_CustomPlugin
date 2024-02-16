package org.shadownight.plugin.shadownight.dungeons.generators;


import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;


public final class GEN_BoundingBox extends UtilityClass {
    /**
     * Generates the floor.
     * @param buffer The data buffer
     * @param material The material to use for the floor
     * @param t The thickness of the floor to generate
     */
    public static void startFloor(@NotNull final RegionBuffer buffer, @NotNull final Material material, final int t){
        //      X                                 Z                                 Thickness
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.z; ++j) for(int k = 0; k < t; ++k) {
            buffer.set(i, k, j, material);
        }
    }

    /**
     * Generates the ceiling.
     * @param buffer The data buffer
     * @param material The material to use for the ceiling
     * @param t The thickness of the ceiling to generate
     * @param wh The height of the walls
     * @param ft The thickness of the floor
     */
    public static void startCeiling(@NotNull final RegionBuffer buffer, @NotNull final Material material, final int t, final int wh, final int ft){
        //      X                                 Z                                 Thickness
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.z; ++j) for(int k = wh + ft; k < wh + ft + t; ++k) {
            buffer.set(i, k, j, material);
        }
    }


    /**
     * Generates the outer walls.
     * @param buffer The data buffer
     * @param material The material to use for the outer walls
     * @param t The thickness of the walls to generate
     * @param h The height of the walls to generate
     * @param x The x size of the buffer //FIXME
     * @param z The z size of the buffer //FIXME
     * @param ft The thickness of the floor
     */
    public static void startWalls(@NotNull final RegionBuffer buffer, @NotNull final Material material, final int t, final int h, final int x, final int z, final int ft){
        //      X                               Height                                        Thickness
        for(int i = -t; i < x + t; ++i) for(int j = -ft; j < h; ++j) for(int k = 1; k < t + 1; ++k) {
            buffer.setShifted(i, j,        -k, material);
            buffer.setShifted(i, j, z + k - 1, material);
        }

        //      Z                           Height                                        Thickness
        for(int i = 0; i < z ; ++i) for(int j = -ft; j < h; ++j) for(int k = 1; k < t + 1; ++k)  {
            buffer.setShifted(-k,        j, i, material);
            buffer.setShifted(x + k - 1, j, i, material);
        }
    }
}