package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;


public final class SHD_CeilingVines extends Rnd {
    static private final BlockPattern patternVines = new BlockPattern(
        Pair.with(6f, Material.OAK_LEAVES)
    );




    private static Material compute(int x, int y, int z) {
        return patternVines.get();
    }

    public static void start(RegionBuffer buffer, Material material) {
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, j, k) == material) buffer.set(i, j, k, compute(i, j, k));
        }
    }
}
