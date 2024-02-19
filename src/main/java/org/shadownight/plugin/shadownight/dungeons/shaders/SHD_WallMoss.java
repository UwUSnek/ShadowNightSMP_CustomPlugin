package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Rotatable;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.containers.BlueprintData;


public final class SHD_WallMoss extends SHD {
    static private final BlockPattern M_WallMoss = new BlockPattern(
        Pair.with(1f, Material.MOSS_BLOCK.createBlockData())
    );


    final int wh;
    final int ft;
    final Vector2i[][] normals;

    /**
     * @param _wh The wall height
     * @param _ft The thickness of the floor
     * @param _normals The normals of each block
     */
    public SHD_WallMoss(final int _wh, final int _ft, @NotNull final Vector2i[][] _normals) {
        wh = _wh;
        ft = _ft;
        normals = _normals;
    }

    @Override
    public void compute(final int x, final int y, final int z) {
        final double noise = perlinNoise3D.compute(x, y, z, 31);
        if(y < i.y - 1 && i.get(x, y + 1, z) == BlueprintData.AIR && noise < 0.51 + rnd.nextFloat() * 0.2) o.set(x, y + 1, z, M_WallMoss.get());
    }
}
