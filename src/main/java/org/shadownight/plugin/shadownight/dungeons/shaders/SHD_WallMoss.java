package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.containers.BlueprintData;


public final class SHD_WallMoss extends SHD {
    static private final BlockPattern M_WallMoss = new BlockPattern(
        Pair.with(1f, Material.MOSS_BLOCK.createBlockData())
    );



    final int wh;
    final int ft;

    /**
     * @param _wh The wall height
     * @param _ft The thickness of the floor
     */
    public SHD_WallMoss(final int _wh, final int _ft) {
        wh = _wh;
        ft = _ft;
    }

    @Override
    public void compute(final int x, final int y, final int z) {
        if(y < i.y - 1 && i.get(x, y + 1, z) == BlueprintData.AIR) o.set(x, y + 1, z, M_WallMoss.get());
    }
}
