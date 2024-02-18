package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;

public abstract class SHD {
    public abstract void compute(@NotNull final RegionBuffer i, @NotNull final RegionBuffer o, final int x, final int y, final int z);
}
