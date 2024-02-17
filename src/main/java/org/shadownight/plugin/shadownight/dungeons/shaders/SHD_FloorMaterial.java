package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.data.DataBuilderSlab;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise2D;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.Rnd;


public final class SHD_FloorMaterial extends UtilityClass implements Rnd {
    private static final BlockPattern M_Moss = new BlockPattern(
        Pair.with(2f, Material.MOSS_BLOCK.createBlockData()),
        Pair.with(1f, Material.GRASS_BLOCK.createBlockData()),
        Pair.with(1f, Material.MOSSY_COBBLESTONE.createBlockData())
    );
    private static final BlockPattern M_MossFill = new BlockPattern(
        Pair.with(1f, Material.MOSSY_STONE_BRICKS.createBlockData())
    );


    private static final BlockPattern M_Floor = new BlockPattern(
        Pair.with(8f, Material.STONE_BRICKS.createBlockData()),
        Pair.with(2f, Material.CRACKED_STONE_BRICKS.createBlockData())
    );
    private static final BlockPattern M_FloorSlab = new BlockPattern(
        Pair.with(1f, new DataBuilderSlab(Material.STONE_BRICK_SLAB).setType(Slab.Type.BOTTOM).setWaterlogged(true).build())
    );
    private static final BlockPattern M_FloorHidden = new BlockPattern(
        Pair.with(1f, Material.STONE_BRICKS.createBlockData())
    );


    private static final BlockPattern M_FloorEdges = new BlockPattern(
        Pair.with(8f, Material.MOSSY_STONE_BRICKS.createBlockData()),
        Pair.with(2f, Material.CRACKED_STONE_BRICKS.createBlockData())
    );
    private static final BlockPattern M_FloorEdgesSlab = new BlockPattern(
        Pair.with(8f, new DataBuilderSlab(Material.MOSSY_STONE_BRICK_SLAB).setType(org.bukkit.block.data.type.Slab.Type.BOTTOM).setWaterlogged(true).build()),
        Pair.with(2f, Material.STONE_BRICK_SLAB.createBlockData())
    );



    private static BlockData compute(final int x, final int y, final int z, final float wallDist, final int floorThickness) {
        double noiseSlab = PerlinNoise2D.compute(x, z, 12);
        double noiseMoss = PerlinNoise2D.compute(x, z, 20);
        boolean isSlab = noiseSlab > 0.55 && noiseSlab < 0.65;

        if(y < floorThickness - 1) return M_FloorHidden.get();
        else {
            float r = rnd.nextFloat();
            if (noiseMoss < 0.55 && noiseMoss > 0.4) return noiseMoss < r * 2 ? M_Moss.get() : M_MossFill.get();
            else if (wallDist < r) return isSlab ? M_FloorEdgesSlab.get() : M_FloorEdges.get();
            else                   return isSlab ? M_FloorSlab.get() : M_Floor.get();
        }
    }


    /**
     * Generates the material of the floor.
     * @param buffer The data buffer
     * @param targetMaterial The material that was used for the floor
     * @param dist The gradient containing the distance of each block from the closest wall
     * @param ft The thickness of the floor
     */
    public static void start(@NotNull final RegionBuffer buffer, @NotNull final Material targetMaterial, final float[][] dist, final int ft) {
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < ft; ++j) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, j, k) == targetMaterial) buffer.set(i, j, k, compute(i, j, k, dist[i][k], ft));
        }
    }
}
