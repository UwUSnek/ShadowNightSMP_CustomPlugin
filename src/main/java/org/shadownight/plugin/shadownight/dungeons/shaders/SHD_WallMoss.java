package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.data.DataBuilderOrientable;
import org.shadownight.plugin.shadownight.utils.graphics.BlockGradient;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise3D;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;


public final class SHD_WallMoss extends UtilityClass implements Rnd {
    static private final BlockPattern M_WallMoss = new BlockPattern(
        Pair.with(1f, Material.MOSS_BLOCK.createBlockData())
    );




    private static BlockData compute(@NotNull final RegionBuffer buffer, final int x, final int z) {
        return M_WallMoss.get();
    }

    /**
     * Generates the material of the walls.
     * @param buffer The data buffer
     * @param material The material that was used for the walls
     * @param dist The distance gradient taken at the lowest exposed wall height
     * @param h The height of the walls
     * @param ft The thickness of the floor
     */
    public static void start(@NotNull final RegionBuffer buffer, @NotNull final Material material, final float[][] dist, final int h, final int ft) {
        for(int i = 0; i < buffer.x; ++i) for(int k = 0; k < buffer.z; ++k){
            if(dist[i][k] < 0.1) for(int j = ft; j < ft + h; ++j) {
                if(buffer.get(i, j, k) != material) {
                    buffer.set(i, j, k, compute(buffer, i, k));
                    break;
                }
            }
        }
    }
}
