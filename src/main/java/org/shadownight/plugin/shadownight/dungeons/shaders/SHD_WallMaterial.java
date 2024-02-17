package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.data.DataBuilderDeepslate;
import org.shadownight.plugin.shadownight.utils.graphics.BlockGradient;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise3D;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.Rnd;


public final class SHD_WallMaterial extends UtilityClass implements Rnd {
    static private final BlockPattern M_WallBase = new BlockPattern(
        Pair.with(1f, Material.POLISHED_DEEPSLATE.createBlockData())
    );
    static private final BlockGradient M_Wall = new BlockGradient(
        Pair.with(3, Material.DEEPSLATE_TILES.createBlockData()),
        Pair.with(2, Material.DEEPSLATE_BRICKS.createBlockData()),
        Pair.with(1, Material.COBBLED_DEEPSLATE.createBlockData()),
        Pair.with(1, new BlockPattern(
            Pair.with(1f, new DataBuilderDeepslate(Material.DEEPSLATE).setFacing(Axis.X).build()),
            Pair.with(1f, new DataBuilderDeepslate(Material.DEEPSLATE).setFacing(Axis.Z).build())
        )),
        Pair.with(1, new BlockPattern(
            Pair.with(1f, Material.DEEPSLATE.createBlockData()),
            Pair.with(1f, Material.COBBLESTONE.createBlockData())
        )),
        Pair.with(1, new BlockPattern(
            Pair.with(1f, Material.COBBLESTONE.createBlockData()),
            Pair.with(1f, Material.ANDESITE.createBlockData())
        )),
        Pair.with(1, new BlockPattern(
            Pair.with(1f, Material.ANDESITE.createBlockData()),
            Pair.with(1f, Material.DIORITE.createBlockData())
        )),
        Pair.with(3, Material.DIORITE.createBlockData())
    );




    private static BlockData compute(final int x, final int y, final int z, final int wallHeight, final int floorThickness) {
        return y < floorThickness + 2 ?
            M_WallBase.get() :
            M_Wall.get((float)y / wallHeight + ((float)PerlinNoise3D.compute(x, y, z, 7) - 0.5f) / 3)
        ;
    }

    /**
     * Generates the material of the walls.
     * @param buffer The data buffer
     * @param material The material that was used for the walls
     * @param h The height of the walls
     * @param ft The thickness of the floor
     */
    public static void start(@NotNull final RegionBuffer buffer, @NotNull final Material material, final int h, final int ft) {
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, j, k) == material) buffer.set(i, j, k, compute(i, j, k, h, ft));
        }
    }
}
