package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.Rnd;




public final class SHD_FloorVegetation extends UtilityClass implements Rnd {
    static private final BlockPattern patternGrass = new BlockPattern(
        Pair.with(2f, Material.AIR),
        Pair.with(2f, Material.SHORT_GRASS),
        Pair.with(1f, Material.FERN)
    );
    static private final BlockPattern patternMushroom = new BlockPattern(
        Pair.with(64f, Material.AIR),
        Pair.with(4f, Material.BROWN_MUSHROOM),
        Pair.with(1f, Material.RED_MUSHROOM)
    );




    private static Material compute(@NotNull final Material supportingBlock, final float wallDistance) {
        if(supportingBlock == Material.MOSS_BLOCK || supportingBlock == Material.GRASS_BLOCK) return patternGrass.get();
        else if(supportingBlock.isOccluding() && wallDistance < 0.5) return patternMushroom.get();
        else return Material.AIR;
    }

    /**
     * Generates the vegetation on the floor.
     * @param buffer The data buffer
     * @param dist The gradient containing the distance of each block from the closest wall
     * @param ft The thickness of the floor
     */
    public static void start(@NotNull final RegionBuffer buffer, final float[][] dist, final int ft) {
        for(int i = 0; i < buffer.x; ++i) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, ft, k) == Material.AIR) buffer.set(i, ft, k, compute(buffer.get(i, ft - 1, k), dist[i][k]));
        }
    }
}
