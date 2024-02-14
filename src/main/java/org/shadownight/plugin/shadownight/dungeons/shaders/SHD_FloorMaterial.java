package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Stairs;
import org.javatuples.Pair;
import org.shadownight.plugin.shadownight.utils.GetRandom;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.logging.Level;


public class SHD_FloorMaterial extends SHD_GeneratorShader {
    float[][] wallDistanceGradient;
    public SHD_FloorMaterial(Material _targetMaterial, float[][] _wallDistanceGradient) {
        super(_targetMaterial);
        wallDistanceGradient = _wallDistanceGradient;
    }








    static BlockPattern patternMoss = new BlockPattern(
        Pair.with(2f, Material.MOSS_BLOCK),
        Pair.with(1f, Material.GRASS_BLOCK),
        Pair.with(1f, Material.MOSSY_COBBLESTONE)
    );
    static BlockPattern patternFloorEdges = new BlockPattern(
        Pair.with(8f, Material.MOSSY_STONE_BRICKS),
        Pair.with(2f, Material.CRACKED_STONE_BRICKS)
    );
    static BlockPattern patternFloorEdgesSlab = new BlockPattern(
        Pair.with(8f, Material.MOSSY_STONE_BRICK_SLAB),
        Pair.with(2f, Material.STONE_BRICK_SLAB)
    );


    @Override
    public Material exec(int x, int y, int z) {
        double noiseSlab = PerlinNoise.perlinCalc(x, z, 6);
        double noiseMoss = PerlinNoise.perlinCalc(x, z, 20);
        double wallDist = wallDistanceGradient[x][z];

        boolean isSlab = noiseSlab > 0.7;
        /* //TODO create gradient class
        //TODO BP_Random extends BP_Pattern
        //TODO BP_Gradient extends BP_Pattern
        if(n < 0.1) return Material.BLACK_CONCRETE;
        else if(n < 0.2) return Material.POLISHED_BLACKSTONE_BRICKS;
        else if(n < 0.3) return Material.CRACKED_DEEPSLATE_BRICKS;
        else if(n < 0.4) return Material.DEEPSLATE;
        else if(n < 0.6) return Material.CRACKED_STONE_BRICKS;
        else if(n < 0.7) return Material.ANDESITE;
        else if(n < 0.8) return Material.LIGHT_GRAY_CONCRETE_POWDER;
        else  return Material.SMOOTH_STONE;
        */


        float r = rnd.nextFloat();
        if(noiseMoss < 0.45 && noiseMoss > 0.3 && noiseMoss < r * 2) return patternMoss.get();
        else if(wallDist < r) return isSlab ? patternFloorEdgesSlab.get() : patternFloorEdges.get();
        else                  return isSlab ? Material.STONE_BRICK_SLAB   : GetRandom.block(Material.CRACKED_STONE_BRICKS, Material.STONE_BRICKS);
    }
}
