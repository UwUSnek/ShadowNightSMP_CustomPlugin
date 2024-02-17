package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.Rnd;




public final class SHD_FloorVegetation extends UtilityClass implements Rnd {
    static private final BlockPattern M_Grass = new BlockPattern(
        Pair.with(2f, Material.AIR.createBlockData()),
        Pair.with(2f, Material.SHORT_GRASS.createBlockData()),
        Pair.with(1f, Material.FERN.createBlockData())
    );
    static private final BlockPattern M_Mushroom = new BlockPattern(
        Pair.with(64f, Material.AIR.createBlockData()),
        Pair.with(4f,  Material.BROWN_MUSHROOM.createBlockData()),
        Pair.with(1f,  Material.RED_MUSHROOM.createBlockData())
    );




    private static BlockData compute(@NotNull final Material supportingBlock, final float wallDistance) {
        if(supportingBlock == Material.MOSS_BLOCK || supportingBlock == Material.GRASS_BLOCK) return M_Grass.get();
        else if(supportingBlock.isOccluding() && wallDistance < 0.5) return M_Mushroom.get();
        else return Material.AIR.createBlockData();
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
