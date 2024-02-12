package org.shadownight.plugin.shadownight.dungeons;


import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;


public class Dungeon {
    //! Prefix to use in dungeon world names. MUST be all lowercase
    private static final String namePrefix = "dungeon_";

    public final World world;
    final Random rnd = new Random();

    public Dungeon() {
        world = Bukkit.createWorld(new WorldCreator(namePrefix + "fewifjwefoio")
            .environment(World.Environment.NORMAL)
            .type(WorldType.NORMAL) //! FLAT creates errors in console. This is a Minecraft Vanilla bug
            .generator("VoidGenerator")
            .generateStructures(false)
            .biomeProvider(new BiomeProvider() {
                @NotNull @Override
                public Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
                    return Biome.PLAINS;
                }
                @NotNull @Override
                public List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
                    return List.of(Biome.PLAINS);
                }
            })
        );

        generateDungeon();
    }


    //    s = 9  (9x9 tiles)
    //    t = 2  (1x2 walls)
    //
    //    ||·········||·········||·········||
    //    ╰──╴s+t╶──╯  ╰──╴s╶──╯╰┴ t
    //        11           9       3

    // FIXME test if this is still broken. it likely is
    private void placeTile(int x, int z, int s, int t) {
        int st = s + t;
        for(int i = x * st + t; i < (x + 1) * st; ++i) for(int j = z * st + t; j < (z + 1) * st; ++j) {
            world.getBlockAt(i, -1, j).setType(Material.DIRT);
        }
    }

    /**
     * Places a wall in the world
     * @param wall The wall to place
     * @param s The size of each tile
     * @param t The thickness of the wall
     */
    private void placeWall(Wall wall, int s, int t) {
        //if(wall.up) switch(wall.type) {
        int st = s + t;
        switch(wall.type) {
            case 'x': for (int i = wall.a.x * st; i < wall.b.x * st + t; ++i) world.getBlockAt(i, 0, wall.a.z * st + st).setType(Material.STONE); break;
            case 'z': for (int i = wall.a.z * st; i < wall.b.z * st + t; ++i) world.getBlockAt(wall.a.x * st + st, 0, i).setType(Material.STONE); break;
        }
    }




    private static class v2i {
        int x, z;
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
        v2i a, b;
        char type;
        public Wall(v2i _a, v2i _b, char _type) {
            a = _a;
            b = _b;
            type = _type;
        }
    }


    public void generateDungeon(){
        int tileSize = 3;  // The size of each tile
        int wallThickness = 1;


        // Initialize tiles
        int x = 21; // The width  of the maze measured in tiles. Must be an odd number
        int z = 41; // The height of the maze measured in tiles. Must be an odd number
        TreeNode[][] tiles = new TreeNode[x][z]; // Defaults to { parent: null }
        for(int i = 0; i < x; ++i) for(int j = 0; j < z; ++j) tiles[i][j] = new TreeNode();

        // TODO remove. this is only for debugging
        for(int i = 0; i < (x + 1) * (tileSize + wallThickness); ++i)
        for(int j = 0; j < (z + 1) * (tileSize + wallThickness); ++j)
        for(int k = -1; k < 2; ++k) world.getBlockAt(i, k, j).setType(Material.AIR);


        // Initialize and randomize walls
        int x1 = x - 1;
        int z1 = z - 1;
        int vNum = x * z1; // Number of x-axis walls   │
        int hNum = x1 * z; // Number of z-axis walls  ───
        ArrayList<Wall> walls = new ArrayList<>(vNum + hNum);
        for(int i = 0; i < x; ++i) for(int j = 0; j < z1; ++j) walls.add(new Wall(new v2i(i, j), new v2i(i + 1, j), 'x'));
        for(int i = 0; i < x1; ++i) for(int j = 0; j < z; ++j) walls.add(new Wall(new v2i(i, j), new v2i(i, j + 1), 'z'));
        //Collections.shuffle(walls);


        // Merge sets with Kruskal's Algorithm
        for (Wall wall : walls) {
            TreeNode aRoot = tiles[wall.a.x][wall.a.z].getRoot();
            TreeNode bRoot = tiles[wall.b.x][wall.b.z].getRoot();
            if (aRoot != bRoot) new TreeNode(aRoot, bRoot);
            else placeWall(wall, tileSize, wallThickness);
        }


        // Draw external walls
        for(int i = 0; i < x * (tileSize + wallThickness) + wallThickness; ++i) {
            world.getBlockAt(i, -1, 0).setType(Material.PURPLE_CONCRETE);
            world.getBlockAt(i, -1, z * (tileSize + wallThickness)).setType(Material.PURPLE_CONCRETE);
        }
        for(int i = 0; i < z * (tileSize + wallThickness) + wallThickness; ++i) {
            world.getBlockAt(0, -1, i).setType(Material.PURPLE_CONCRETE);
            world.getBlockAt(x * (tileSize + wallThickness), -1, i).setType(Material.PURPLE_CONCRETE);
        }


        //for(int i = 0; i < w; ++i) for(int j = 0; j < h; ++j) placeTile(i, j, tileSize, wallThickness);
    }






    /**
     * Deleted any remaining dungeon that wasn't deleted on shutdown for whatever reason
     * This MUST be called first in the .onLoad function
     */
    public static void deleteOldDungeons(){
        List<File> worlds;
        try {
            worlds = Files.list(Paths.get(ShadowNight.plugin.getServer().getWorldContainer().getPath()))
                .map(Path::toFile)
                .filter((File f) -> f.isDirectory() && f.getName().startsWith(namePrefix))
                .toList()
            ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if(!worlds.isEmpty()) {
            utils.log(Level.INFO, "Deleting " + worlds.size() + " old dungeon" + (worlds.size() == 1 ? "" : "s") + "...");
            for (File world : worlds) {
                utils.log(Level.INFO, "Deleted old dungeon " + world.getName());
                try {
                    FileUtils.deleteDirectory(world);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
