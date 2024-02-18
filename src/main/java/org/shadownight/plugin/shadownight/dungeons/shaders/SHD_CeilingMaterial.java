package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.graphics.BlockGradient;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise3D;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;


public final class SHD_CeilingMaterial extends SHD {
    static private final BlockGradient M_Ceiling = new BlockGradient(
        Pair.with(4, new BlockPattern(
            Pair.with(0.5f, Material.DEEPSLATE_COAL_ORE.createBlockData()),
            Pair.with(4f,   Material.DEEPSLATE_TILES.createBlockData())
        )),
        Pair.with(1, new BlockPattern(
            Pair.with(0.5f, Material.COAL_ORE.createBlockData()),
            Pair.with(1f,   Material.DEEPSLATE_COAL_ORE.createBlockData()),
            Pair.with(4f,   Material.TUFF.createBlockData()),
            Pair.with(1f,   Material.DIORITE.createBlockData()),
            Pair.with(1f,   Material.POLISHED_DIORITE.createBlockData())
        )),
        Pair.with(4, new BlockPattern(
            Pair.with(0.5f, Material.DEEPSLATE_COAL_ORE.createBlockData()),
            Pair.with(4f,   Material.DEEPSLATE_TILES.createBlockData())
        ))
    );



    @Override
    public void compute(final int x, final int y, final int z) {
        o.set(x, y, z, M_Ceiling.get((float)PerlinNoise3D.compute(x, y, z, 32)));
    }
}
