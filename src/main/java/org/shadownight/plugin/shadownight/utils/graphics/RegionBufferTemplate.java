package org.shadownight.plugin.shadownight.utils.graphics;


import com.google.common.collect.HashMultimap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.dungeons.shaders.SHD;
import org.shadownight.plugin.shadownight.utils.math.Func;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Level;


/**
 * A simple world region that only contains block types (Material) and data (BlockData)
 * Allows for custom shift on set operations
 */
public final class RegionBufferTemplate {
    private final RegionTemplateData[][][] d;
    public final int x, y, z;
    private final int shift_x, shift_y, shift_z;

    private final AtomicInteger activeTasks = new AtomicInteger(0);


    /**
     * Creates a new RegionBuffer
     * @param _x The length of the buffer
     * @param _y The height of the buffer
     * @param _z The width of the buffer
     * @param _shift_x The shift on the x-axis applied by the setShifted functions
     * @param _shift_y The shift on the y-axis applied by the setShifted functions
     * @param _shift_z The shift on the z-axis applied by the setShifted functions
     */
    public RegionBufferTemplate(final int _x, final int _y, final int _z, final int _shift_x, final int _shift_y, final int _shift_z) {
        x = _x;
        y = _y;
        z = _z;
        shift_x = _shift_x;
        shift_y = _shift_y;
        shift_z = _shift_z;

        d = new RegionTemplateData[x][y][z];
        for(int i = 0; i < x; ++i) for(int j = 0; j < y; ++j) for(int k = 0; k < z; ++k) {
            d[i][j][k] = RegionTemplateData.NULL;
        }
    }






    /**
     * Sets the type of the specified block, shifting the provided coordinates by the amount set when this buffer was created.
     * @param _x The x coordinate of the block
     * @param _y The y coordinate of the block
     * @param _z The z coordinate of the block
     * @param type The type to set
     */
    public void setShifted(final int _x, final int _y, final int _z, @NotNull final RegionTemplateData type) {
        try {
            d[_x + shift_x][_y + shift_y][_z + shift_z] = type;
        }
        catch(ArrayIndexOutOfBoundsException e){
            utils.log(Level.SEVERE, "Template index out of bounds: Received index (" + _x + ", " + _y + ", " + _z + ") with shift (" + shift_x + ", " + shift_y + ", " + shift_z + ")");
        }
    }



    /**
     * Sets the type of the specified block.
     * The top part of bisected blocks is automatically placed.
     * @param _x The x coordinate of the block
     * @param _y The y coordinate of the block
     * @param _z The z coordinate of the block
     * @param data The data to set
     */
    public void set(final int _x, final int _y, final int _z, @NotNull final RegionTemplateData data) {
        try {
            d[_x][_y][_z] = data;
        }
        catch(ArrayIndexOutOfBoundsException e){
            utils.log(Level.SEVERE, "Template index out of bounds: Received index (" + _x + ", " + _y + ", " + _z + ") with shift 0");
        }
    }

    /**
     * Returns the type of the specified block.
     * @param _x The x coordinate of the block
     * @param _y The y coordinate of the block
     * @param _z The z coordinate of the block
     * @return The type of block
     */
    public RegionTemplateData get(final int _x, final int _y, final int _z){
        return d[_x][_y][_z];
    }





    /**
     * Executes a list of material shaders, equally splitting the execution of each shader between a configured amount of threads.
     * All the shaders are executed at the same time and their order is not preserved, but the output uses a dedicated temporary buffer to avoid interferences.
     * This method doesn't wait for the shaders to finish. Pass a runnable to <onComplete> to execute code after.
     * @param threads The number of threads to use
     * @param shaders A list of pairs each containing the material the shader will be called on and the shader to compute
     * @param onComplete The task to run after all the threads have finished computing their shaders.
     *                   This task is ran on the Main Thread and is passed the output buffer as the only parameter
     */
    public void dispatchShaders(final int threads, @NotNull final List<Pair<RegionTemplateData, SHD>> shaders, @NotNull final Consumer<RegionBuffer> onComplete) {
        Bukkit.getScheduler().runTaskAsynchronously(ShadowNight.plugin, () -> {
            // Create temporary buffer
            final RegionBuffer output = new RegionBuffer(x, y, z, shift_x, shift_y, shift_z);
            final int sectionSize = x / threads + 1;

            waitForTasks();
            activeTasks.set(threads);
            for (int i = 0; i < threads; ++i) {
                // Create hashmap
                final HashMultimap<RegionTemplateData, SHD> shaderMap = HashMultimap.create();
                for (Pair<RegionTemplateData, SHD> s : shaders) {
                    s.getValue1().setData(this, output);
                    shaderMap.put(s.getValue0(), s.getValue1());
                }
                // Start shader tasks
                int x0 = Func.clampMax(sectionSize * i, x);
                int x1 = Func.clampMax(sectionSize * (i + 1), x);
                if (x0 != x1) Bukkit.getScheduler().runTaskAsynchronously(ShadowNight.plugin, () -> {
                    computeShaderSection(x0, x1, shaderMap);
                    activeTasks.decrementAndGet();
                });
            }
            waitForTasks();

            /*
            // Paste data from temporary buffer back into this buffer
            for (int i = 0; i < x; ++i) for (int j = 0; j < y; ++j) for (int k = 0; k < z; ++k) {
                d[i][j][k] = output.d[i][j][k];
                b[i][j][k] = output.b[i][j][k];
            }
            */

            // Run callback on main thread
            Bukkit.getScheduler().runTask(ShadowNight.plugin, () -> onComplete.accept(output));
        });
    }


    private void computeShaderSection(final int x0, final int x1, @NotNull final HashMultimap<RegionTemplateData, SHD> shaders) {
        for(int i = x0; i < x1; ++i) for(int j = 0; j < y; ++j) for(int k = 0; k < z; ++k) {
            for(SHD s : shaders.get(d[i][j][k])) {
                s.compute(i, j, k);
            }
        }
    }

    private void waitForTasks(){
        while(activeTasks.get() > 0) {
            try {
                TimeUnit.MILLISECONDS.sleep(1);
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
