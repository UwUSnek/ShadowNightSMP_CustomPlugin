package org.shadownight.plugin.shadownight.dungeons.generators;


import org.bukkit.Material;
import org.shadownight.plugin.shadownight.dungeons.RegionBuffer;


/**
 * Generates the outer walls and the floor of a dungeon
 */
public class GEN_BoundingBox {
    public static void startFloor(RegionBuffer buffer, Material material, int thickness){
        //      X                                 Z                                 Thickness
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.z; ++j) for(int k = 0; k < thickness; ++k) {
            buffer.setNoShift(i, k, j, material);
        }
    }
    public static void startCeiling(RegionBuffer buffer, Material material, int thickness, int wh){
        //      X                                 Z                                 Thickness
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.z; ++j) for(int k = wh; k < wh + thickness; ++k) {
            buffer.setNoShift(i, k, j, material);
        }
    }


    public static void startWalls(RegionBuffer buffer, Material material, int t, int height, int x, int z){
        //      X                               Height                          Thickness
        for(int i = -t; i < x + t; ++i) for(int j = 0; j < height; ++j) for(int k = 1; k < t + 1; ++k) {
            buffer.set(i, j,        -k, material);
            buffer.set(i, j, z + k - 1, material);
        }

        //      Z                           Height                          Thickness
        for(int i = 0; i < z ; ++i) for(int j = 0; j < height; ++j) for(int k = 1; k < t + 1; ++k)  {
            buffer.set(-k,        j, i, material);
            buffer.set(x + k - 1, j, i, material);
        }
    }
}