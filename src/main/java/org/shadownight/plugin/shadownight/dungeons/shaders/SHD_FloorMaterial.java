package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.shadownight.plugin.shadownight.dungeons.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.Rnd;


public class SHD_FloorMaterial extends Rnd {
    static BlockPattern patternMoss = new BlockPattern(
        Pair.with(2f, Material.MOSS_BLOCK),
        Pair.with(1f, Material.GRASS_BLOCK),
        Pair.with(1f, Material.MOSSY_COBBLESTONE)
    );
    static BlockPattern patternFloor = new BlockPattern(
        Pair.with(8f, Material.STONE_BRICKS),
        Pair.with(2f, Material.CRACKED_STONE_BRICKS)
    );
    static BlockPattern patternFloorEdges = new BlockPattern(
        Pair.with(8f, Material.MOSSY_STONE_BRICKS),
        Pair.with(2f, Material.CRACKED_STONE_BRICKS)
    );
    static BlockPattern patternFloorEdgesSlab = new BlockPattern(
        Pair.with(8f, Material.MOSSY_STONE_BRICK_SLAB),
        Pair.with(2f, Material.STONE_BRICK_SLAB)
    );



    private static Material compute(int x, int y, int z, float wallDist, int floorThickness) {
        double noiseSlab = PerlinNoise.perlinCalc(x, z, 12);
        double noiseMoss = PerlinNoise.perlinCalc(x, z, 20);
        boolean isSlab = noiseSlab > 0.55 && noiseSlab < 0.65;

        if(y < floorThickness - 1) return Material.STONE_BRICKS;
        else {
            float r = rnd.nextFloat();
            if (noiseMoss < 0.55 && noiseMoss > 0.4 && noiseMoss < r * 2) return patternMoss.get();
            else if (wallDist < r) return isSlab ? patternFloorEdgesSlab.get() : patternFloorEdges.get();
            else                   return isSlab ? Material.STONE_BRICK_SLAB : patternFloor.get();
        }
    }


    public static void start(RegionBuffer buffer, Material targetMaterial, float[][] wallDistanceGradient, int floorThickness) {
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, j, k) == targetMaterial) buffer.set(i, j, k, compute(i, j, k, wallDistanceGradient[i][k], floorThickness));
        }
    }
}
