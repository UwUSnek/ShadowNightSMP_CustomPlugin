package org.uwu_snek.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.containers.RegionBlueprint;
import org.uwu_snek.shadownight.utils.containers.RegionBuffer;
import org.uwu_snek.shadownight.utils.graphics.BlockGradient;




public final class SHD_OuterWallMaterial extends SHD {
    private static final BlockGradient M_OuterWall = new BlockGradient(
        Pair.with(1, Material.DEEPSLATE.createBlockData()),
        Pair.with(1, Material.OBSIDIAN.createBlockData())
    );



    final int ft;
    final int owt;
    final int wt;
    final SHD_WallMaterial normalWall;

    /**
     * @param _wh The wall height
     * @param _ft The thickness of the floor
     * @param _owt The thickness of the outer walls
     * @param _wt The thickness of the inner walls
     */
    public SHD_OuterWallMaterial(final int _wh, final int _ft, final int _owt, final int _wt) {
        ft = _ft;
        owt = _owt;
        wt = _wt;
        normalWall = new SHD_WallMaterial(_wh, _ft);
    }
    @Override
    public void setData(final @NotNull RegionBlueprint _i, final @NotNull RegionBuffer _o) {
        super.setData(_i, _o);
        normalWall.setData(i, o);
    }



    @Override
    public void compute(final int x, final int y, final int z) {
        BlockData hiddenBlock = SHD_FloorMaterial.M_FloorHidden.get((float)y / ft);
        if(hiddenBlock.getMaterial() == Material.OBSIDIAN) o.set(x, y, z, hiddenBlock);
        else {
            final int rel = getRel(x, z);
            o.set(x, y, z, M_OuterWall.get((float)(rel - wt) / (owt - wt)));
        }
    }
    private int getRel(final int x, final int z) {
        final float xNorm = (float)x / i.x - 0.5f;     // The normalized position on the X-axis
        final float zNorm = (float)z / i.z - 0.5f;     // The normalized position on the Z-axis

        return owt - (
            Math.abs(xNorm) > Math.abs(zNorm) ?
            (xNorm > 0 ? i.x - 1 - x : x) :  // X position relative to the start of the outer wall looking from the spawn point
            (zNorm > 0 ? i.z - 1 - z : z)    // Z position relative to the start of the outer wall looking from the spawn point
        );
    }
}
