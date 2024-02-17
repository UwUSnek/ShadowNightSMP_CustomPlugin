package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.data.DataBuilderLeaves;
import org.shadownight.plugin.shadownight.utils.graphics.BlockPattern;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;


public final class SHD_CeilingVines extends UtilityClass implements Rnd {
    private static final BlockPattern M_Vines = new BlockPattern(
        Pair.with(6f, new DataBuilderLeaves(Material.OAK_LEAVES).setPersistent(true).build())
    );




    private static BlockData compute(final int x, final int y, final int z) {
        return M_Vines.get();
    }


    /**
     * Generates the material of the vines hanging from the ceiling.
     * @param buffer The data buffer
     * @param material The material that was used for the vines
     */
    public static void start(@NotNull final RegionBuffer buffer, @NotNull final Material material) {
        for(int i = 0; i < buffer.x; ++i) for(int j = 0; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k){
            if(buffer.get(i, j, k) == material) buffer.set(i, j, k, compute(i, j, k));
        }
    }
}
