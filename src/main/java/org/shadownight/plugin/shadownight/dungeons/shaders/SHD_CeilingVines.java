package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.data.DataBuilderLeaves;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;


public final class SHD_CeilingVines extends SHD {
    private static final BlockPattern M_Vines = new BlockPattern(
        Pair.with(1f, new DataBuilderLeaves(Material.OAK_LEAVES).setPersistent(true).build())
    );

    @Override
    public void compute(final int x, final int y, final int z) {
        o.set(x, y, z, M_Vines.get());
    }
}