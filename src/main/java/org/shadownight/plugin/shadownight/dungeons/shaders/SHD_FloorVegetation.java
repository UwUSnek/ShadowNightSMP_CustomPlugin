package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.shadownight.plugin.shadownight.dungeons.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.Rnd;




public class SHD_FloorVegetation extends Rnd {
    static private BlockPattern patternGrass = new BlockPattern(
        Pair.with(2f, Material.AIR),
        Pair.with(2f, Material.SHORT_GRASS),
        Pair.with(1f, Material.FERN)
    );
    static private BlockPattern patternMushroom = new BlockPattern(
        Pair.with(32f, Material.AIR),
        Pair.with(4f, Material.BROWN_MUSHROOM),
        Pair.with(1f, Material.RED_MUSHROOM)
    );




    private static Material compute(Material supportingBlock, float wallDistance) {
        if(supportingBlock == Material.MOSS_BLOCK || supportingBlock == Material.GRASS_BLOCK) return patternGrass.get();
        else if(supportingBlock.isOccluding() && wallDistance < 0.5) return patternMushroom.get();
        else return Material.AIR;
    }

    public static void start(RegionBuffer buffer, float[][] wallDistanceGradient, int floorThickness) {
        for(int i = 0; i < buffer.x; ++i) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, floorThickness, k) == Material.AIR) buffer.setNoShift(i, floorThickness, k, compute(buffer.get(i, floorThickness - 1, k), wallDistanceGradient[i][k]));
        }
    }
}
