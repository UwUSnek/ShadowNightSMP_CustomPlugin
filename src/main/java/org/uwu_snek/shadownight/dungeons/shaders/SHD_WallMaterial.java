package org.uwu_snek.shadownight.dungeons.shaders;


import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.javatuples.Pair;
import org.uwu_snek.shadownight.utils.blockdata.DataBuilderOrientable;
import org.uwu_snek.shadownight.utils.graphics.BlockGradient;
import org.uwu_snek.shadownight.utils.graphics.BlockPattern;


public final class SHD_WallMaterial extends SHD {
    static private final BlockPattern M_WallBase = new BlockPattern(
        Pair.with(1f, Material.POLISHED_DEEPSLATE.createBlockData())
    );
    static private final BlockGradient M_Wall = new BlockGradient(
        Pair.with(3, new BlockPattern(
            Pair.with(1.65f, Material.DEEPSLATE_TILES.createBlockData()),
            Pair.with(1.35f, Material.CRACKED_DEEPSLATE_TILES.createBlockData()),
            Pair.with(0.65f, Material.DEEPSLATE_BRICKS.createBlockData()),
            Pair.with(0.35f, Material.CRACKED_DEEPSLATE_BRICKS.createBlockData())
        )),
        Pair.with(2, new BlockPattern(
            Pair.with(0.65f, Material.DEEPSLATE_TILES.createBlockData()),
            Pair.with(0.35f, Material.CRACKED_DEEPSLATE_TILES.createBlockData()),
            Pair.with(1.65f, Material.DEEPSLATE_BRICKS.createBlockData()),
            Pair.with(1.35f, Material.CRACKED_DEEPSLATE_BRICKS.createBlockData())
        )),
        Pair.with(1, Material.COBBLED_DEEPSLATE.createBlockData()),
        Pair.with(1, new BlockPattern(
            Pair.with(1.5f, new DataBuilderOrientable(Material.DEEPSLATE).setFacing(Axis.X).build()),
            Pair.with(1.5f, new DataBuilderOrientable(Material.DEEPSLATE).setFacing(Axis.Z).build()),
            Pair.with(0.5f, Material.DEEPSLATE.createBlockData()),
            Pair.with(0.5f, Material.COBBLESTONE.createBlockData())
        )),
        Pair.with(1, new BlockPattern(
            Pair.with(0.5f, new DataBuilderOrientable(Material.DEEPSLATE).setFacing(Axis.X).build()),
            Pair.with(0.5f, new DataBuilderOrientable(Material.DEEPSLATE).setFacing(Axis.Z).build()),
            Pair.with(1.0f, Material.DEEPSLATE.createBlockData()),
            Pair.with(1.0f, Material.COBBLESTONE.createBlockData()),
            Pair.with(0.5f, Material.COBBLESTONE.createBlockData()),
            Pair.with(0.5f, Material.ANDESITE.createBlockData())
        )),
        Pair.with(1, new BlockPattern(
            Pair.with(1.5f, Material.COBBLESTONE.createBlockData()),
            Pair.with(1.5f, Material.ANDESITE.createBlockData()),
            Pair.with(0.5f, Material.ANDESITE.createBlockData()),
            Pair.with(0.5f, Material.DIORITE.createBlockData())
        )),
        Pair.with(1, new BlockPattern(
            Pair.with(1f, Material.ANDESITE.createBlockData()),
            Pair.with(1f, Material.DIORITE.createBlockData())
        )),
        Pair.with(3, Material.DIORITE.createBlockData())
    );




    final int wh;
    final int ft;

    /**
     * @param _wh The wall height
     * @param _ft The thickness of the floor
     */
    public SHD_WallMaterial(final int _wh, final int _ft) {
        wh = _wh;
        ft = _ft;
    }

    @Override
    public void compute(final int x, final int y, final int z) {
        BlockData hiddenBlock = SHD_FloorMaterial.M_FloorHidden.get((float)y / ft);
        if(hiddenBlock.getMaterial() == Material.OBSIDIAN) o.set(x, y, z, hiddenBlock);
        else o.set(x, y, z,
            y < ft + 2 ?
            M_WallBase.get() :
            M_Wall.get((float)(y - ft) / wh + ((float)perlinNoise3D.compute(x, y, z, 7) - 0.5f) / 3)
        );
    }
}
