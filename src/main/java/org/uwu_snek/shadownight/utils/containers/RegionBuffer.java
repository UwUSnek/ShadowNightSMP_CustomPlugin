package org.uwu_snek.shadownight.utils.containers;


import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * A simple world region that only contains block types (Material) and data (BlockData)
 * Allows for custom shift on set operations
 */
public final class RegionBuffer {
    private final BlockData[][][] d;
    private final Biome[][][] b;
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
    public RegionBuffer(final int _x, final int _y, final int _z, final int _shift_x, final int _shift_y, final int _shift_z) {
        x = _x;
        y = _y;
        z = _z;
        shift_x = _shift_x;
        shift_y = _shift_y;
        shift_z = _shift_z;

        d = new BlockData[x][y][z];
        b = new Biome[x][y][z];
        BlockData data = Material.AIR.createBlockData();
        for(int i = 0; i < x; ++i) for(int j = 0; j < y; ++j) for(int k = 0; k < z; ++k) {
            d[i][j][k] = data;
            b[i][j][k] = null;
        }
    }
    public RegionBuffer(final @NotNull RegionBuffer copy) {
        x = copy.x;
        y = copy.y;
        z = copy.z;
        shift_x = copy.shift_x;
        shift_y = copy.shift_y;
        shift_z = copy.shift_z;

        d = new BlockData[x][y][z];
        b = new Biome[x][y][z];
        for(int i = 0; i < x; ++i) for(int j = 0; j < y; ++j) for(int k = 0; k < z; ++k) {
            d[i][j][k] = copy.d[i][j][k];
            b[i][j][k] = copy.b[i][j][k];
        }
    }





    /**
     * Sets the biome of the specified block.
     * @param _x The x coordinate of the block
     * @param _y The y coordinate of the block
     * @param _z The z coordinate of the block
     * @param biome The biome to set
     */
    public void setBiome(final int _x, final int _y, final int _z, final @NotNull Biome biome) {
        b[_x][_y][_z] = biome;
    }





    /**
     * Sets the material and data of the specified block.
     * The top part of bisected blocks is automatically placed.
     * @param _x The x coordinate of the block
     * @param _y The y coordinate of the block
     * @param _z The z coordinate of the block
     * @param data The data to set
     */
    public void set(final int _x, final int _y, final int _z, final @NotNull BlockData data) {
        try {
            d[_x][_y][_z] = data;
            if(data instanceof Bisected && _y + 1 < y - 1) {
                Bisected top = (Bisected)data.clone();
                top.setHalf(Bisected.Half.TOP);
                d[_x][_y + 1][_z] = top;
            }
        }
        catch(ArrayIndexOutOfBoundsException e){
            utils.log(Level.SEVERE, "Index out of bounds: Received index (" + _x + ", " + _y + ", " + _z + ") with shift 0");
            throw new RuntimeException(e);
        }
    }
    /**
     * Sets the material of the specified block using its default data.
     * @param _x The x coordinate of the block
     * @param _y The y coordinate of the block
     * @param _z The z coordinate of the block
     * @param material The material to set
     */
    public void set(final int _x, final int _y, final int _z, final @NotNull Material material) {
        set(_x, _y, _z, material.createBlockData());
    }

    /**
     * Returns the material of the specified block.
     * @param _x The x coordinate of the block
     * @param _y The y coordinate of the block
     * @param _z The z coordinate of the block
     * @return The block material
     */
    public Material get(final int _x, final int _y, final int _z){
        return d[_x][_y][_z].getMaterial();
    }




    /**
     * Pastes the region at the given location, optionally creating a bedrock box around it.
     * @param world The world to paste the region in
     * @param _x The x coordinate of the origin
     * @param _y The y coordinate of the origin
     * @param _z The z coordinate of the origin
     * @param createBox Whether to create the bedrock box. This is always created before pasting the actual region
     */
    public void paste(final @NotNull World world, final int _x, final int _y, final int _z, final boolean createBox) {
        if(createBox) {
            // X-Y vertical plane (bedrock box side)
            for(int i = -1; i < x + 1; ++i) for(int j = -1; j < y + 1; ++j) {
                world.getBlockAt(_x + i, _y + j, _z - 1).setType(Material.BEDROCK, false);
                world.getBlockAt(_x + i, _y + j, _z + x).setType(Material.BEDROCK, false);
            }
            // Z-Y vertical plane (bedrock box side)
            for(int i = 0; i < z; ++i) for(int j = -1; j < y + 1; ++j) {
                world.getBlockAt(_x - 1, _y + j, _z + i).setType(Material.BEDROCK, false);
                world.getBlockAt(_x + x, _y + j, _z + i).setType(Material.BEDROCK, false);
            }
            // X-Z horizontal plane (bedrock box side)
            for(int i = 0; i < x; ++i) for(int j = 0; j < z; ++j) {
                world.getBlockAt(_x + i, _y - 1, _z + j).setType(Material.BEDROCK, false);
                world.getBlockAt(_x + i, _y + y, _z + j).setType(Material.BEDROCK, false);
            }
        }


        // Main region
        // IMPORTANT: This must be placed last as mushrooms require a low skylight level which the bedrock box provides
        //            Placing them first will cause most of them to drop as items
        for(int i = 0; i < x; ++i) for(int j = 0; j < y; ++j) for(int k = 0; k < z; ++k) {
            world.getBlockAt(_x + i, _y + j, _z + k).setBlockData(d[i][j][k]);
            Biome biome = b[i][j][k];
            if(biome != null) world.setBiome(_x + i, _y + j, _z + k, biome);
        }
    }
}
