package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.graphics.BlockGradient;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise3D;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;


public final class SHD_CeilingMaterial extends Rnd {
    static private final BlockPattern patternCeiling = new BlockPattern(
        Pair.with(6f, Material.DEEPSLATE_TILES)
    );




    private static Material compute(int x, int y, int z) {
        return patternCeiling.get();
    }

    public static void start(RegionBuffer buffer, Material material) {
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, j, k) == material) buffer.set(i, j, k, compute(i, j, k));
        }
    }
}
