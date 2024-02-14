package org.shadownight.plugin.shadownight.dungeons;


import org.bukkit.Material;
import org.bukkit.World;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.logging.Level;

/**
 * A simple world region that only contains block types (Material)
 * Allows for custom shift on set operations
 */
public class RegionBuffer {
    private final Material[][][] b;
    public final int x;
    public final int y;
    public final int z;
    private final int shift_x;
    private final int shift_y;
    private final int shift_z;


    public RegionBuffer(int _x, int _y, int _z, int _shift_x, int _shift_y, int _shift_z) {
        x = _x;
        y = _y;
        z = _z;
        shift_x = _shift_x;
        shift_y = _shift_y;
        shift_z = _shift_z;

        b = new Material[x][y][z];
        for(int i = 0; i < x; ++i) for(int j = 0; j < y; ++j) for(int k = 0; k < z; ++k) {
            b[i][j][k] = Material.AIR;
        }
    }




    public void set(int _x, int _y, int _z, Material material) {
        try {
            b[_x + shift_x][_y + shift_y][_z + shift_z] = material;
        }
        catch(ArrayIndexOutOfBoundsException e){
            utils.log(Level.SEVERE, "Index out of bounds: Received index (" + _x + ", " + _y + ", " + _z + ") with shift (" + shift_x + ", " + shift_y + ", " + shift_z + ")");
        }
    }
    public void setNoShift(int _x, int _y, int _z, Material material) {
        try {
            b[_x][_y][_z] = material;
        }
        catch(ArrayIndexOutOfBoundsException e){
            utils.log(Level.SEVERE, "Index out of bounds: Received index (" + _x + ", " + _y + ", " + _z + ") with shift 0");
        }
    }

    public Material get(int _x, int _y, int _z){
        return b[_x][_y][_z];
    }

    public void paste(World world, int _x, int _y, int _z) {
        for(int i = 0; i < x; ++i) for(int j = 0; j < y; ++j) for(int k = 0; k < z; ++k) {
            world.getBlockAt(_x + i, _y + j, _z + k).setType(b[i][j][k]);
        }
    }
}
