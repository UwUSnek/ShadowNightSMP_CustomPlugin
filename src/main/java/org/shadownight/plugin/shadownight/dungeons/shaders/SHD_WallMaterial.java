package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.shadownight.plugin.shadownight.dungeons.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.Rnd;


public class SHD_WallMaterial extends Rnd {
    static private final BlockGradient patternWall = new BlockGradient(
        Material.DEEPSLATE_TILES,
        Material.COBBLED_DEEPSLATE,
        Material.COBBLESTONE,
        Material.ANDESITE
    );




    private static Material compute(int x, int y, int z, int wallHeight, int floorThickness) {
        return patternWall.get((float)(y - floorThickness) / wallHeight);
    }

    public static void start(RegionBuffer buffer, Material material, int wallHeight, int floorThickness) {
        for(int i = 0; i < buffer.x; ++i) for(int j = floorThickness; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, j, k) == material) buffer.set(i, j, k, compute(i, j, k, wallHeight, floorThickness));
        }
    }
}
