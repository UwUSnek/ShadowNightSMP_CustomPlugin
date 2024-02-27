package org.uwu_snek.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.uwu_snek.shadownight.utils.blockdata.DataBuilderLeaves;
import org.uwu_snek.shadownight.utils.graphics.BlockPattern;


public final class SHD_CeilingVines extends SHD {
    private static final BlockPattern M_Vines = new BlockPattern(
        Pair.with(1f, new DataBuilderLeaves(Material.MANGROVE_LEAVES).setPersistent(true).build())
    );

    @Override
    public void compute(final int x, final int y, final int z) {
        o.set(x, y, z, M_Vines.get());
    }
}
