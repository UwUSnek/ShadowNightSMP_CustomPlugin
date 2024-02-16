package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.graphics.BlockGradient;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise3D;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;


public final class SHD_CeilingMaterial extends UtilityClass implements Rnd {
    static private final BlockGradient patternCeiling = new BlockGradient(
        Pair.with(4, new BlockPattern(
            Pair.with(0.5f, Material.DEEPSLATE_COAL_ORE),
            Pair.with(4f,   Material.DEEPSLATE_TILES)
        )),
        Pair.with(1, new BlockPattern(
            Pair.with(0.5f, Material.COAL_ORE),
            Pair.with(1f,   Material.DEEPSLATE_COAL_ORE),
            Pair.with(4f,   Material.TUFF),
            Pair.with(1f,   Material.DIORITE),
            Pair.with(1f,   Material.POLISHED_DIORITE)
        )),
        Pair.with(4, new BlockPattern(
            Pair.with(0.5f, Material.DEEPSLATE_COAL_ORE),
            Pair.with(4f,   Material.DEEPSLATE_TILES)
        ))
    );




    private static Material compute(final int x, final int y, final int z) {
        return patternCeiling.get((float)PerlinNoise3D.compute(x, y, z, 32));
    }


    /**
     * Generates the material of the ceiling.
     * @param buffer The data buffer
     * @param material The material that was used for the ceiling
     */
    public static void start(@NotNull final RegionBuffer buffer, @NotNull final Material material) {
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, j, k) == material) buffer.set(i, j, k, compute(i, j, k));
        }
    }
}
