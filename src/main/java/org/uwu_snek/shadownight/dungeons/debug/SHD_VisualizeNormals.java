package org.uwu_snek.shadownight.dungeons.debug;


import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.uwu_snek.shadownight.dungeons.shaders.SHD;




@SuppressWarnings("unused")
public final class SHD_VisualizeNormals extends SHD {
    final Vector2i[][] normals;

    /**
     * @param _normals The normals of each block
     */
    public SHD_VisualizeNormals(final @NotNull Vector2i @NotNull [] @NotNull [] _normals) {
        normals = _normals;
    }

    @Override
    public void compute(final int x, final int y, final int z) {
        if(y != 7) return;
        final Rotatable data = (Rotatable)Material.PIGLIN_HEAD.createBlockData();
             if(normals[x][z].x ==  1 && normals[x][z].y ==  1)  { data.setRotation(BlockFace.NORTH_WEST); o.set(x, y, z, data); }
        else if(normals[x][z].x == -1 && normals[x][z].y ==  1)  { data.setRotation(BlockFace.NORTH_EAST); o.set(x, y, z, data); }
        else if(normals[x][z].x ==  1 && normals[x][z].y == -1)  { data.setRotation(BlockFace.SOUTH_WEST); o.set(x, y, z, data); }
        else if(normals[x][z].x == -1 && normals[x][z].y == -1)  { data.setRotation(BlockFace.SOUTH_EAST); o.set(x, y, z, data); }
        else if(normals[x][z].x ==  1) { data.setRotation(BlockFace.WEST); o.set(x, y, z, data); }
        else if(normals[x][z].x == -1) { data.setRotation(BlockFace.EAST); o.set(x, y, z, data); }
        else if(normals[x][z].y ==  1) { data.setRotation(BlockFace.NORTH); o.set(x, y, z, data); }
        else if(normals[x][z].y == -1) { data.setRotation(BlockFace.SOUTH); o.set(x, y, z, data); }
    }
}
