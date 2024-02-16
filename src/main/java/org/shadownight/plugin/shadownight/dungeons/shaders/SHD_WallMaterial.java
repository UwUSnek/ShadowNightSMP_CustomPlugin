package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.graphics.BlockGradient;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise3D;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.Rnd;


public final class SHD_WallMaterial extends UtilityClass implements Rnd {
    static private final BlockGradient patternWall = new BlockGradient(
        Pair.with(3, Material.DEEPSLATE_TILES),
        Pair.with(2, Material.DEEPSLATE_BRICKS),
        Pair.with(1, Material.COBBLED_DEEPSLATE),
        Pair.with(1, Material.DEEPSLATE),
        Pair.with(1, new BlockPattern(
            Pair.with(1f, Material.DEEPSLATE),
            Pair.with(1f, Material.COBBLESTONE)
        )),
        Pair.with(1, new BlockPattern(
            Pair.with(1f, Material.COBBLESTONE),
            Pair.with(1f, Material.ANDESITE)
        )),
        Pair.with(1, new BlockPattern(
            Pair.with(1f, Material.ANDESITE),
            Pair.with(1f, Material.DIORITE)
        )),
        Pair.with(3, Material.DIORITE)
    );




    private static Material compute(final int x, final int y, final int z, final int wallHeight, final int floorThickness) {
        return y < floorThickness + 2 ?
            Material.POLISHED_DEEPSLATE :
            patternWall.get((float)y / wallHeight + ((float)PerlinNoise3D.compute(x, y, z, 7) - 0.5f) / 3)
        ;
    }

    /**
     * Generates the material of the walls.
     * @param buffer The data buffer
     * @param material The material that was used for the walls
     * @param h The height of the walls
     * @param ft The thickness of the floor
     */
    public static void start(@NotNull final RegionBuffer buffer, @NotNull final Material material, final int h, final int ft) {
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, j, k) == material) buffer.set(i, j, k, compute(i, j, k, h, ft));
        }
    }
}
