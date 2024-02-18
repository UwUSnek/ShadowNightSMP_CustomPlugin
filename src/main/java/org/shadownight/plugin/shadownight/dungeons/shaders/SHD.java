package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.graphics.RegionBuffer;


/**
 * A 3D Material shader.
 * Subclasses must implement the compute method which will be called on every suitable block in the region.
 */
public abstract class SHD implements Rnd {
    /**
     * The input data buffer.
     */
    protected RegionBuffer i;
    /**
     * The output data buffer.
     */
    protected RegionBuffer o;



    public final void setBuffers(@NotNull final RegionBuffer _i, @NotNull final RegionBuffer _o) {
        i = _i;
        o = _o;
    }

    public abstract void compute(final int x, final int y, final int z);
}
