package org.shadownight.plugin.shadownight.dungeons.generators;


import org.bukkit.Material;
import org.bukkit.World;



/**
 * Generates the outer walls and the floor of a dungeon
 */
public class GEN_BoundingBox {
    public static void startFloor(World world, Material material, int thickness, int wt, int x, int z){
        //      X                          Z                          Thickness
        for(int i = -wt + 1; i < x + wt - 1; ++i) for(int j = -wt + 1; j < z + wt - 1; ++j) for(int k = -thickness; k < 0; ++k) {
            world.getBlockAt(i, k, j).setType(material);
        }
    }


    public static void startWalls(World world, Material material, int t, int height, int x, int z){
        //      X                               Height                          Thickness
        for(int i = -t + 1; i < x + t - 1; ++i) for(int j = 0; j < height; ++j) for(int k = 1; k < t; ++k) {
            world.getBlockAt(i, j,        -k).setType(material);
            world.getBlockAt(i, j, z + k - 1).setType(material);
        }

        //      Z                           Height                          Thickness
        for(int i = 0; i < z ; ++i) for(int j = 0; j < height; ++j) for(int k = 1; k < t; ++k)  {
            world.getBlockAt(-k,        j, i).setType(material);
            world.getBlockAt(x + k - 1, j, i).setType(material);
        }
    }
}
