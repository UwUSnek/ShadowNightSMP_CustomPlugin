package org.shadownight.plugin.shadownight.utils.containers;


import com.google.common.collect.HashMultimap;
import org.bukkit.Bukkit;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
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
public final class RegionBlueprint {
    private final BlueprintData[][][] d;
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
    public RegionBlueprint(final int _x, final int _y, final int _z, final int _shift_x, final int _shift_y, final int _shift_z) {
        x = _x;
        y = _y;
        z = _z;
        shift_x = _shift_x;
        shift_y = _shift_y;
        shift_z = _shift_z;

        d = new BlueprintData[x][y][z];
        for(int i = 0; i < x; ++i) for(int j = 0; j < y; ++j) for(int k = 0; k < z; ++k) {
            d[i][j][k] = BlueprintData.AIR;
        }
    }






    /**
     * Sets the type of the specified block, shifting the provided coordinates by the amount set when this buffer was created.
     * @param _x The x coordinate of the block
     * @param _y The y coordinate of the block
     * @param _z The z coordinate of the block
     * @param type The type to set
     */
    public void setShifted(final int _x, final int _y, final int _z, @NotNull final BlueprintData type) {
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
    public void set(final int _x, final int _y, final int _z, @NotNull final BlueprintData data) {
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
    public BlueprintData get(final int _x, final int _y, final int _z){
        return d[_x][_y][_z];
    }













    /**
     * Creates a float gradient in which each cell indicates the distance of the block from the closest wall.
     * @param y The Y level to use when performing calculations
     * @param tileSize The size of each tile
     * @param wt The thickness of the walls
     * @param calculateNegative Whether or not negative distances should be calculated for blocks inside walls.
     *                          If false, internal blocks will have a distance value of 0
     */
    public float[][] createWallDistanceGradient(final int y, final int tileSize, final int wt, final boolean calculateNegative) {
        final float[][] gradient = new float[x][z];
        final int halfSize = tileSize / 2;
        final int halfWt = wt / 2;
        for(int i = 0; i < x; ++i) for(int j = 0;  j < z; ++j) {
            int k = 0;
            try {
                while (
                    k < halfSize &&
                    i + k < x - 1 && j + k < z - 1 &&
                    i - k > 0 && j - k > 0 &&
                    d[i + k][y][j    ] != BlueprintData.WALL &&
                    d[i    ][y][j + k] != BlueprintData.WALL &&
                    d[i - k][y][j    ] != BlueprintData.WALL &&
                    d[i    ][y][j - k] != BlueprintData.WALL
                ) ++k;
                if(calculateNegative && k == 0) {
                    while (
                        k < halfWt &&
                        i + k < x - 1 && j + k < z - 1 &&
                        i - k > 0 && j - k > 0 &&
                        d[i + k][y][j    ] == BlueprintData.WALL &&
                        d[i    ][y][j + k] == BlueprintData.WALL &&
                        d[i - k][y][j    ] == BlueprintData.WALL &&
                        d[i    ][y][j - k] == BlueprintData.WALL
                    ) ++k;
                    gradient[i][j] = -(float)k / halfWt;
                }
                else gradient[i][j] = (float)k / halfSize;
            }
            catch (ArrayIndexOutOfBoundsException e) {
                utils.log(Level.SEVERE, "Gradient creation failed: Received index (" + i + ", " + j + ") with k = " + k + " and buffer size (" + x + ", " + z + ")");
            }
        }
        return gradient;
    }



    /**
     * Creates a float gradient in which each cell indicates the distance of the block from the closest wall.
     * @param y The Y level to use when performing calculations
     * @param wt The thickness of the walls
     * @param tileSize The size of each tile
     * @return The generated normals
     *//*
    public Vector2i[][] createWallNormals(final int y, final int wt, final int tileSize) {
        final Vector2i[][] normals = new Vector2i[x][z];
        final int halfSize = tileSize / 2;
        final int halfWt = wt / 2;
        for(int i = 0; i < x; ++i) for(int j = 0;  j < z; ++j) {
            int k = 0;
            try {
                while (
                    k < halfSize &&
                    i + k < x - 1 && j + k < z - 1 &&
                    i - k > 0 && j - k > 0
                ) {
                    if(i + k < x - 1 &&  j + k < z - 1 && d[i + k][y][j + k] == BlueprintData.WALL) { normals[i][j] = new Vector2i(1, 1); break; }
                    if(i + k < x - 1 &&  j - k >     0 && d[i + k][y][j - k] == BlueprintData.WALL) { normals[i][j] = new Vector2i(1, 0); break; }
                    if(i - k >     0 &&  j + k < z - 1 && d[i - k][y][j + k] == BlueprintData.WALL) { normals[i][j] = new Vector2i(0, 0); break; }
                    if(i - k >     0 &&  j - k >     0 && d[i - k][y][j - k] == BlueprintData.WALL) { normals[i][j] = new Vector2i(0, 0); break; }
                    ++k;
                }
                if(k == 0) {
                    while (
                        k < halfWt &&
                        i + k < x - 1 && j + k < z - 1 &&
                        i - k > 0 && j - k > 0 &&
                        d[i + k][y][j + k] == BlueprintData.WALL &&
                        d[i + k][y][j - k] == BlueprintData.WALL &&
                        d[i - k][y][j + k] == BlueprintData.WALL &&
                        d[i - k][y][j - k] == BlueprintData.WALL
                    ) ++k;
                    normals[i][j] = -(float)k / halfWt;
                }
                else normals[i][j] = (float)k / halfSize;
            }
            catch (ArrayIndexOutOfBoundsException e) {
                utils.log(Level.SEVERE, "Gradient creation failed: Received index (" + i + ", " + j + ") with k = " + k + " and buffer size (" + x + ", " + z + ")");
            }
        }
        return normals;
    }

*/








    /**
     * Executes a list of material shaders, equally splitting the execution of each shader between a configured amount of threads.
     * All the shaders are executed at the same time and their order is not preserved, but the output uses a dedicated buffer to avoid interferences.
     * This method doesn't wait for the shaders to finish. Pass a Consumer to <onComplete> to execute code after.
     * @param threads The number of threads to use
     * @param shaders A list of pairs each containing the material the shader will be called on and the shader to compute
     * @param onComplete The task to run after all the threads have finished computing their shaders.
     *                   This task is ran on the Main Thread and is passed the output buffer as the only parameter
     */
    public void dispatchShaders(final int threads, @NotNull final List<Pair<BlueprintData, SHD>> shaders, @NotNull final Consumer<RegionBuffer> onComplete) {
        Bukkit.getScheduler().runTaskAsynchronously(ShadowNight.plugin, () -> {
            // Create temporary buffer
            final RegionBuffer output = new RegionBuffer(x, y, z, shift_x, shift_y, shift_z);
            final int sectionSize = x / threads + 1;

            waitForTasks();
            activeTasks.set(threads);
            for (int i = 0; i < threads; ++i) {
                // Create hashmap
                final HashMultimap<BlueprintData, SHD> shaderMap = HashMultimap.create();
                for (Pair<BlueprintData, SHD> s : shaders) {
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

            // Run callback on main thread
            Bukkit.getScheduler().runTask(ShadowNight.plugin, () -> onComplete.accept(output));
        });
    }


    private void computeShaderSection(final int x0, final int x1, @NotNull final HashMultimap<BlueprintData, SHD> shaders) {
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
