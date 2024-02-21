package org.shadownight.plugin.shadownight.dungeons.generators;


import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.containers.RegionBlueprint;
import org.shadownight.plugin.shadownight.utils.containers.BlueprintData;

import java.util.ArrayList;
import java.util.Collections;


public final class GEN_Walls extends UtilityClass {
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
        final Vector2i a;
        final Vector2i b;
        final char type;
        public Wall(@NotNull final Vector2i _a, @NotNull final Vector2i _b, final char _type) {
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
        final Vector2i a = wall.a;
        switch(wall.type) {
            //                 Long side                                         Height                            Thickness
            case 'x': for (int i = a.x * st - t; i < a.x * st + st; ++i) for(int j = -floorThickness; j < h; ++j) for(int k = 0; k < t; ++k) {
                buffer.setShifted(i, j, a.y * st + s + k, BlueprintData.WALL);
            }
            break;
            case 'z': for (int i = a.y * st - t; i < a.y * st + st; ++i) for(int j = -floorThickness; j < h; ++j) for(int k = 0; k < t; ++k) {
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
     * @param xNum The number of tiles in the X-axis
     * @param zNum The number of tiles in the Z-axis
     * @param ft The thickness of the floor
     */
    public static void start(@NotNull final RegionBlueprint buffer, final int tileSize, final int wt, final int h, final int xNum, final int zNum, final int ft){
        // Initialize tiles
        final TreeNode[][] tiles = new TreeNode[xNum][zNum]; // Defaults to { parent: null }
        for(int i = 0; i < xNum; ++i) for(int j = 0; j < zNum; ++j) tiles[i][j] = new TreeNode();


        // Initialize and randomize walls
        final int x1 = xNum - 1;
        final int z1 = zNum - 1;
        final int vNum = xNum * z1; // Number of x-axis walls   │
        final int hNum = x1 * zNum; // Number of z-axis walls  ───
        final ArrayList<Wall> walls = new ArrayList<>(vNum + hNum);
        for(int i = 0; i < xNum; ++i) for(int j = 0; j < z1; ++j) walls.add(new Wall(new Vector2i(i, j), new Vector2i(i, j + 1), 'x'));
        for(int i = 0; i < x1; ++i) for(int j = 0; j < zNum; ++j) walls.add(new Wall(new Vector2i(i, j), new Vector2i(i + 1, j), 'z'));
        Collections.shuffle(walls);


        // Merge sets with Kruskal's Algorithm
        for (Wall wall : walls) {
            final TreeNode aRoot = tiles[wall.a.x][wall.a.y].getRoot();
            final TreeNode bRoot = tiles[wall.b.x][wall.b.y].getRoot();
            if (aRoot != bRoot) new TreeNode(aRoot, bRoot);
            else placeWall(buffer, wall, tileSize, wt, h, ft);
        }
    }
}
