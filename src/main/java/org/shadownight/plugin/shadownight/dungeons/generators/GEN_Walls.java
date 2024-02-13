package org.shadownight.plugin.shadownight.dungeons.generators;


import org.bukkit.Material;
import org.bukkit.World;
import org.shadownight.plugin.shadownight.dungeons.RegionBuffer;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Generates the inner maze walls of a dungeon
 */
public class GEN_Walls {
    private static class v2i {
        int x;
        int z;
        public v2i(int _x, int _z) {
            x = _x;
            z = _z;
        }
    }

    private static class TreeNode {
        private TreeNode parent = null;

        public TreeNode() {}
        public TreeNode(TreeNode _a, TreeNode _b) {
            _a.parent = this;
            _b.parent = this;
        }

        public TreeNode getRoot() {
            return parent == null ? this : parent.getRoot();
        }
    }

    private static class Wall {
        //boolean up = true;
        v2i a;
        v2i b;
        char type;
        public Wall(v2i _a, v2i _b, char _type) {
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
     * Places a wall in the world
     * @param wall The wall to place
     * @param s The size of each tile
     * @param t The thickness of the wall
     * @param h The height of the wall
     */
    private static void placeWall(RegionBuffer buffer, Material material, Wall wall, int s, int t, int h) {
        int st = s + t;
        v2i a = wall.a;
        switch(wall.type) {
            //                 Long side                                         Height                            Thickness
            case 'x': for (int i = a.x * st - t; i < a.x * st + st; ++i) for(int j = 0; j < h; ++j) for(int k = 0; k < t; ++k) buffer.set(i, j, a.z * st + s + k, material); break;
            case 'z': for (int i = a.z * st - t; i < a.z * st + st; ++i) for(int j = 0; j < h; ++j) for(int k = 0; k < t; ++k) buffer.set(a.x * st + s + k, j, i, material); break;
        }
    }




    public static void start(RegionBuffer buffer, Material material, int tileSize, int wallThickness, int wallHeight, int x, int z){
        // Initialize tiles
        TreeNode[][] tiles = new TreeNode[x][z]; // Defaults to { parent: null }
        for(int i = 0; i < x; ++i) for(int j = 0; j < z; ++j) tiles[i][j] = new TreeNode();

        // TODO remove. this is only for debugging
        //for(int i = 0; i < (x + 1) * (tileSize + wallThickness); ++i)
            //for(int j = 0; j < (z + 1) * (tileSize + wallThickness); ++j)
                //for(int k = -1; k < wallHeight; ++k) buffer.set(i, k, j, Material.AIR);


        // Initialize and randomize walls
        int x1 = x - 1;
        int z1 = z - 1;
        int vNum = x * z1; // Number of x-axis walls   │
        int hNum = x1 * z; // Number of z-axis walls  ───
        ArrayList<Wall> walls = new ArrayList<>(vNum + hNum);
        for(int i = 0; i < x; ++i) for(int j = 0; j < z1; ++j) walls.add(new Wall(new v2i(i, j), new v2i(i, j + 1), 'x'));
        for(int i = 0; i < x1; ++i) for(int j = 0; j < z; ++j) walls.add(new Wall(new v2i(i, j), new v2i(i + 1, j), 'z'));
        Collections.shuffle(walls);


        // Merge sets with Kruskal's Algorithm
        for (Wall wall : walls) {
            TreeNode aRoot = tiles[wall.a.x][wall.a.z].getRoot();
            TreeNode bRoot = tiles[wall.b.x][wall.b.z].getRoot();
            if (aRoot != bRoot) new TreeNode(aRoot, bRoot);
            else placeWall(buffer, material, wall, tileSize, wallThickness, wallHeight);
        }
    }
}
