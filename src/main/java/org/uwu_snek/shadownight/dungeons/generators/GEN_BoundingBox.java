package org.uwu_snek.shadownight.dungeons.generators;


import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.Rnd;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.containers.RegionBlueprint;
import org.uwu_snek.shadownight.utils.containers.BlueprintData;
import org.uwu_snek.shadownight.utils.math.Func;


public final class GEN_BoundingBox extends UtilityClass implements Rnd {
    /**
     * Generates the floor.
     * @param buffer The data buffer
     * @param t The thickness of the floor to generate
     */
    public static void startFloor(final @NotNull RegionBlueprint buffer, final int t){
        //      X                                 Z                                 Thickness
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.z; ++j) for(int k = 0; k < t; ++k) {
            buffer.set(i, k, j, BlueprintData.FLOOR);
        }
    }

    /**
     * Generates the ceiling.
     * @param buffer The data buffer
     * @param t The thickness of the ceiling to generate
     * @param wh The height of the walls
     * @param ft The thickness of the floor
     */
    public static void startCeiling(final @NotNull RegionBlueprint buffer, final int t, final int wh, final int ft){
        //      X                                 Z                                 Thickness
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.z; ++j) for(int k = wh + ft; k < wh + ft + t; ++k) {
            buffer.set(i, k, j, BlueprintData.CEILING);
        }
    }


    /**
     * Generates the outer walls.
     * @param buffer The data buffer
     * @param h The height of the walls to generate
     * @param ft The thickness of the floor
     * @param owt The thickness of the outer walls
     * @param wt The thickness of the inner walls
     */
    public static void startWalls(final @NotNull RegionBlueprint buffer, final int h, final int ft, final int owt, final int wt){
        final int x = buffer.x - owt * 2;
        final int z = buffer.z - owt * 2;
        //      X                               Height                                        Thickness
        for(int i = -owt; i < x + owt; ++i) for(int j = -ft; j < h; ++j) for(int k = 1; k < owt + 1; ++k) {
            final BlueprintData data = k > wt + rnd.nextInt(Func.clampMin(wt, 1)) - (wt / 2) ? BlueprintData.OUTER_WALL : BlueprintData.WALL;
            buffer.setShifted(i, j,        -k, data);
            buffer.setShifted(i, j, z + k - 1, data);
        }

        //      Z                           Height                                        Thickness
        for(int i = 0; i < z ; ++i) for(int j = -ft; j < h; ++j) for(int k = 1; k < owt + 1; ++k)  {
            final BlueprintData data = k > wt + rnd.nextInt(Func.clampMin(wt, 1)) - (wt / 2) ? BlueprintData.OUTER_WALL : BlueprintData.WALL;
            buffer.setShifted(-k,        j, i, data);
            buffer.setShifted(x + k - 1, j, i, data);
        }
    }
}