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


    @Override
    public void compute(final int x, final int y, final int z) {
        o.set(x, y, z, M_WallMoss.get());
        if(i.get(x, y + 1, z) == BlueprintData.AIR) o.set(x, y + 1, z, SHD_FloorMaterial.M_Grass.get());
    }
}
