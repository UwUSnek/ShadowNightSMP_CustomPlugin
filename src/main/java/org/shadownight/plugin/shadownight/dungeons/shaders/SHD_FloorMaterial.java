package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fence;
import org.bukkit.block.data.type.Stairs;


public class SHD_FloorMaterial extends SHD_GeneratorShader {
    float[][] wallDistanceGradient;
    public SHD_FloorMaterial(Material _targetMaterial, float[][] _wallDistanceGradient) {
        super(_targetMaterial);
        wallDistanceGradient = _wallDistanceGradient;
    }

    @Override
    public Material exec(int x, int y, int z) {
        //int r = rnd.nextInt(0, 100);
//
        //if     (r < 80) return Material.STONE_BRICKS;
        //else if(r < 90) return Material.MOSSY_STONE_BRICKS;
        //else            return Material.CRACKED_STONE_BRICKS;
        if(wallDistanceGradient[x][z] > 0.5) return Material.GREEN_CONCRETE;
        else return Material.RED_CONCRETE;
    }
}
