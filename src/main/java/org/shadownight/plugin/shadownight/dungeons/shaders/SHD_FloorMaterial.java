package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.data.DataBuilderBisected;
import org.shadownight.plugin.shadownight.utils.data.DataBuilderSlab;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise2D;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.Rnd;


public final class SHD_FloorMaterial extends SHD {
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
        Pair.with(1f, new DataBuilderSlab(Material.STONE_BRICK_SLAB).setType(Slab.Type.BOTTOM).setWaterlogged(false).build())
    );
    private static final BlockPattern M_FloorHidden = new BlockPattern(
        Pair.with(1f, Material.STONE_BRICKS.createBlockData())
    );


    private static final BlockPattern M_FloorEdges = new BlockPattern(
        Pair.with(8f, Material.MOSSY_STONE_BRICKS.createBlockData()),
        Pair.with(2f, Material.CRACKED_STONE_BRICKS.createBlockData())
    );
    private static final BlockPattern M_FloorEdgesSlab = new BlockPattern(
        Pair.with(8f, new DataBuilderSlab(Material.MOSSY_STONE_BRICK_SLAB).setType(org.bukkit.block.data.type.Slab.Type.BOTTOM).setWaterlogged(false).build()),
        Pair.with(2f, new DataBuilderSlab(Material.STONE_BRICK_SLAB      ).setType(org.bukkit.block.data.type.Slab.Type.BOTTOM).setWaterlogged(false).build())
    );
    private static final BlockPattern M_Puddle = new BlockPattern(
        Pair.with(8f, new DataBuilderSlab(Material.MOSSY_STONE_BRICK_SLAB).setType(org.bukkit.block.data.type.Slab.Type.BOTTOM).setWaterlogged(true).build()),
        Pair.with(1f, new DataBuilderSlab(Material.MUD_BRICK_SLAB        ).setType(org.bukkit.block.data.type.Slab.Type.BOTTOM).setWaterlogged(true).build()),
        Pair.with(1f, new DataBuilderSlab(Material.STONE_BRICK_SLAB      ).setType(org.bukkit.block.data.type.Slab.Type.BOTTOM).setWaterlogged(true).build()),
        Pair.with(2f, new DataBuilderSlab(Material.ANDESITE_SLAB         ).setType(org.bukkit.block.data.type.Slab.Type.BOTTOM).setWaterlogged(true).build())
    );




    static public final BlockPattern M_Grass = new BlockPattern(
        Pair.with(8f, Material.AIR.createBlockData()),
        Pair.with(4f, Material.SHORT_GRASS.createBlockData()),
        Pair.with(2f, Material.FERN.createBlockData()),
        Pair.with(2f, new DataBuilderBisected(Material.TALL_GRASS).setHalf(Bisected.Half.BOTTOM).build()),
        Pair.with(1f, new DataBuilderBisected(Material.LARGE_FERN).setHalf(Bisected.Half.BOTTOM).build())
    );
    static private final BlockPattern M_Mushroom = new BlockPattern(
        Pair.with(64f, Material.AIR.createBlockData()),
        Pair.with(4f,  Material.BROWN_MUSHROOM.createBlockData()),
        Pair.with(1f,  Material.RED_MUSHROOM.createBlockData())
    );





    private final float[][] wallDist;
    private final int ft;

    /**
     * @param _wallDist The wall distance gradient
     * @param _ft The thickness of the floor
     */
    public SHD_FloorMaterial(final float[][] _wallDist, final int _ft) {
        wallDist = _wallDist;
        ft = _ft;
    }


    @Override
    public void compute(final int x, final int y, final int z) {
        final double noiseSlab   = PerlinNoise2D.compute(x, z, 12);
        final double noisePuddle = PerlinNoise2D.compute(x + 5000, z + 5000, 25);
        final double noiseMoss   = PerlinNoise2D.compute(x, z, 20);

        final boolean isMoss   = noiseMoss   < 0.55 && noiseMoss   > 0.4;
        final boolean isSlab   = noiseSlab   > 0.56 && noiseSlab   < 0.64;
        final boolean isPuddle = noisePuddle > 0.22 && noisePuddle < 0.27;
        final boolean isPuddleBorder = noisePuddle > 0.27 && noisePuddle < 0.4;

        BlockData output;
        if(y < ft - 1) output = M_FloorHidden.get();
        else {
            final float r = rnd.nextFloat();
            if(isPuddleBorder && !isMoss) o.setBiome(x, y, z, Biome.SWAMP);

            if (isMoss) output = noiseMoss < r * 2 ? M_Moss.get() : M_MossFill.get();
            else if (isPuddle)           output = M_Puddle.get();
            else if (wallDist[x][z] < r) output = isSlab && !isPuddleBorder ? M_FloorEdgesSlab.get() : M_FloorEdges.get();
            else                         output = isSlab && !isPuddleBorder ? M_FloorSlab.get() : M_Floor.get();
        }
        o.set(x, y, z, output);
    }
}
