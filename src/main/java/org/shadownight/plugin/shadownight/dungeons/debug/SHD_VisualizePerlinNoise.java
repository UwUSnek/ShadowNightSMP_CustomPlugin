package org.shadownight.plugin.shadownight.dungeons.debug;


import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.shadownight.plugin.shadownight.dungeons.shaders.SHD;


public final class SHD_VisualizePerlinNoise extends SHD {
    final int gridSize;
    final int yLevel;

    /**
     * @param _gridSize The normals of each block
     * @param _yLevel The y level in which to draw the perlin noise slice
     */
    public SHD_VisualizePerlinNoise(final int _gridSize, final int _yLevel) {
        gridSize = _gridSize;
        yLevel = _yLevel;
    }

    @Override
    public void compute(final int x, final int y, final int z) {
        if(y == yLevel) {
            o.set(x, yLevel + (int)(perlinNoise3D.compute(x, y, z, gridSize) * ((float)gridSize / 2)), z, Material.WHITE_CONCRETE.createBlockData());
            o.set(x, yLevel, z, Material.WHITE_STAINED_GLASS.createBlockData());
            o.set(x, yLevel + gridSize / 2 + 1, z, Material.WHITE_STAINED_GLASS.createBlockData());
        }
    }
}
