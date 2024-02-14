package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Stairs;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.logging.Level;


public class SHD_FloorMaterial extends SHD_GeneratorShader {
    float[][] wallDistanceGradient;
    public SHD_FloorMaterial(Material _targetMaterial, float[][] _wallDistanceGradient) {
        super(_targetMaterial);
        wallDistanceGradient = _wallDistanceGradient;
    }

    @Override
    public Material exec(int x, int y, int z, int gridSize) {
        double noise = PerlinNoise.perlinCalc((float) x / gridSize, (float) z / gridSize);
        double noise2 = PerlinNoise.perlinCalc((float) x / (gridSize * 2), (float) z / (gridSize * 2));
        double dist = wallDistanceGradient[x][z];
        /*
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
        if(noise2 < 0.6 && 1 - dist > r / 4) return r > 0.2 ? Material.MOSS_BLOCK : Material.GRASS_BLOCK;
        else if(dist < r) {
            if (r < 0.66) return noise < 0.7 ? Material.MOSSY_STONE_BRICKS   : Material.MOSSY_STONE_BRICK_SLAB;
            else          return noise < 0.7 ? Material.CRACKED_STONE_BRICKS : Material.STONE_BRICK_SLAB;
        }
        else return noise < 0.7 ? Material.STONE_BRICKS : Material.STONE_BRICK_SLAB;
    }
}
