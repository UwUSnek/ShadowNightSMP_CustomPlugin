package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.containers.RegionBuffer;
import org.shadownight.plugin.shadownight.utils.containers.RegionBlueprint;
import org.shadownight.plugin.shadownight.utils.graphics.*;


/**
 * A 3D Material shader.
 * Subclasses must implement the compute method which will be called on every suitable block in the region.
 */
public abstract class SHD implements Rnd {
    /** The input data buffer.*/
    protected RegionBlueprint i;
    /** The output data buffer.*/
    protected RegionBuffer o;
    /** A 3D perlin noise generator initialized with a random seed that is unique to this shader.*/
    protected _PerlinNoise3D_impl perlinNoise3D;
    /** A 2D perlin noise generator initialized with a random seed that is unique to this shader.*/
    protected _PerlinNoise2D_impl perlinNoise2D;



    public void setData(final @NotNull RegionBlueprint _i, final @NotNull RegionBuffer _o) {
        i = _i;
        o = _o;
        perlinNoise3D = new _PerlinNoise3D_impl();
        perlinNoise2D = new _PerlinNoise2D_impl();
    }

    public abstract void compute(final int x, final int y, final int z);
}
