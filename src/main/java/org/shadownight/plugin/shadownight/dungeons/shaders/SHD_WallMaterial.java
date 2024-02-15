package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.shadownight.plugin.shadownight.dungeons.utils.BlockGradient;
import org.shadownight.plugin.shadownight.dungeons.utils.PerlinNoise3D;
import org.shadownight.plugin.shadownight.dungeons.utils.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.Rnd;


public class SHD_WallMaterial extends Rnd {
    static private final BlockGradient patternWall = new BlockGradient(
        Pair.with(6, Material.DEEPSLATE_TILES),
        Pair.with(4, Material.DEEPSLATE_BRICKS),
        Pair.with(2, Material.COBBLED_DEEPSLATE),
        Pair.with(2, Material.DEEPSLATE),
        Pair.with(2, Material.COBBLESTONE),
        Pair.with(2, Material.ANDESITE),
        Pair.with(1, Material.DIORITE)
    );




    private static Material compute(int x, int y, int z, int wallHeight) {
        return patternWall.get((float)y / wallHeight + ((float)PerlinNoise3D.compute(x, y, z, 7) - 0.5f) / 3);
    }

    public static void start(RegionBuffer buffer, Material material, int wallHeight) {
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, j, k) == material) buffer.set(i, j, k, compute(i, j, k, wallHeight));
        }
    }
}
