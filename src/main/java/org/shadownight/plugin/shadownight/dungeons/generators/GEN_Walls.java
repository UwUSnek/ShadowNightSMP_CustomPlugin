package org.shadownight.plugin.shadownight.dungeons.generators;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.containers.RegionBlueprint;
import org.shadownight.plugin.shadownight.utils.containers.BlueprintData;

import java.util.ArrayList;
import java.util.Collections;


public final class GEN_Walls extends UtilityClass {
    private static final class v2i {
        final int x;
        final int z;
        public v2i(final int _x, final int _z) {
            x = _x;
            z = _z;
        }
    }

    private static final class TreeNode {
        private TreeNode parent = null;

        public TreeNode() {}
        public TreeNode(@NotNull final TreeNode _a, @NotNull final TreeNode _b) {
            _a.parent = this;
            _b.parent = this;
        }

        public TreeNode getRoot() {
            return parent == null ? this : parent.getRoot();
        }
    }

    private static final class Wall {
        //boolean up = true;
        final v2i a;
        final v2i b;
        final char type;
        public Wall(@NotNull final v2i _a, @NotNull final v2i _b, final char _type) {
            a = _a;
            b = _b;
            type = _type;
        }
    }




    /*
        s = 9  (9x9 tiles)
        t = 2  (9x2 walls)                             X
                                                       ╎
        ||·········||·········||·········||            │
        ╰──╴s+t╶──╯  ╰──╴s╶──╯╰┴ t                     └───--- Z
            11           9       3
    */


    /**
     * Places a wall in the world.
     * @param wall The wall to place
     * @param s The size of each tile
     * @param t The thickness of the wall
     * @param h The height of the wall
     */
    private static void placeWall(@NotNull final RegionBlueprint buffer, @NotNull final Wall wall, final int s, final int t, final int h, final int floorThickness) {
        final int st = s + t;
        final v2i a = wall.a;
        switch(wall.type) {
            //                 Long side                                         Height                            Thickness
            case 'x': for (int i = a.x * st - t; i < a.x * st + st; ++i) for(int j = -floorThickness; j < h; ++j) for(int k = 0; k < t; ++k) {
                buffer.setShifted(i, j, a.z * st + s + k, BlueprintData.WALL);
            }
            break;
            case 'z': for (int i = a.z * st - t; i < a.z * st + st; ++i) for(int j = -floorThickness; j < h; ++j) for(int k = 0; k < t; ++k) {
                buffer.setShifted(a.x * st + s + k, j, i, BlueprintData.WALL);
            }
            break;
        }
    }


    /**
     * Generates the maze and places all the walls.
     * @param buffer The data buffer
     * @param tileSize The size of each tile
     * @param wt The thickness of the walls
     * @param h The height of the walls
     * @param x The x size of the buffer //FIXME
     * @param z The z size of the buffer //TODO
     * @param ft The thickness of the floor
     */
    public static void start(@NotNull final RegionBlueprint buffer, final int tileSize, final int wt, final int h, final int x, final int z, final int ft){
        // Initialize tiles
        final TreeNode[][] tiles = new TreeNode[x][z]; // Defaults to { parent: null }
        for(int i = 0; i < x; ++i) for(int j = 0; j < z; ++j) tiles[i][j] = new TreeNode();


        // Initialize and randomize walls
        final int x1 = x - 1;
        final int z1 = z - 1;
        final int vNum = x * z1; // Number of x-axis walls   │
        final int hNum = x1 * z; // Number of z-axis walls  ───
        final ArrayList<Wall> walls = new ArrayList<>(vNum + hNum);
        for(int i = 0; i < x; ++i) for(int j = 0; j < z1; ++j) walls.add(new Wall(new v2i(i, j), new v2i(i, j + 1), 'x'));
        for(int i = 0; i < x1; ++i) for(int j = 0; j < z; ++j) walls.add(new Wall(new v2i(i, j), new v2i(i + 1, j), 'z'));
        Collections.shuffle(walls);


        // Merge sets with Kruskal's Algorithm
        for (Wall wall : walls) {
            final TreeNode aRoot = tiles[wall.a.x][wall.a.z].getRoot();
            final TreeNode bRoot = tiles[wall.b.x][wall.b.z].getRoot();
            if (aRoot != bRoot) new TreeNode(aRoot, bRoot);
            else placeWall(buffer, wall, tileSize, wt, h, ft);
        }
    }
}
