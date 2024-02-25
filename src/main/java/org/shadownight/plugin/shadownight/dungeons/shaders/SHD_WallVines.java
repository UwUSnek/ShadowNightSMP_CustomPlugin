package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;


public final class SHD_WallVines extends SHD {
    static private final BlockPattern M_WallVines = new BlockPattern(
        Pair.with(1f, Material.DARK_OAK_LEAVES.createBlockData()),
        Pair.with(1f, Material.OAK_LEAVES.createBlockData())
    );


    @Override
    public void compute(final int x, final int y, final int z) {
        o.set(x, y, z, M_WallVines.get());
    }
}
