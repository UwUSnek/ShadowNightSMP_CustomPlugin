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







    private void placeTile(int _x, int _z, int tileSize) {
        for(int x = _x * tileSize; x < (_x + 1) * tileSize ; ++x) for(int z = _z * tileSize; z < (_z + 1) * tileSize; ++z) {
            world.getBlockAt(x, 0, z).setType(Material.STONE);
        }
    }
    private void placeWall(Wall wall, int tileSize) {
        if(wall.up) {
            if (wall.type == 'h') for (int i = wall.a.x * tileSize; i < wall.b.x * tileSize + 1; ++i) world.getBlockAt(i, 0, wall.a.y * tileSize).setType(Material.STONE);
            if (wall.type == 'v') for (int i = wall.a.y * tileSize; i < wall.b.y * tileSize + 1; ++i) world.getBlockAt(wall.a.x * tileSize, 0, i).setType(Material.STONE);
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
        boolean up = true;
        Vector2i a;
        Vector2i b;
        char type;
        public Wall(Vector2i _a, Vector2i _b, char _type) {
            a = _a;
            b = _b;
            type = _type;
        }
    }


    private void generateDungeon(){
        int tileSize = 9;  // The size of each tile

        // Initialize tiles
        int w = 21; // The width  of the maze measured in tiles. Must be an odd number
        int h = 21; // The height of the maze measured in tiles. Must be an odd number
        TreeNode[][] tiles = new TreeNode[w][h]; // Defaults to { parent: null }
        for(int i = 0; i < w; ++i) for(int j = 0; j < h; ++j) tiles[i][j] = new TreeNode();


        // Initialize and randomize walls
        int w1 = w - 1;
        int h1 = h - 1;
        int vNum = w1 * h; // Number of vertical   walls   │
        int hNum = h1 * w; // Number of horizontal walls  ───
        ArrayList<Wall> walls = new ArrayList<>(vNum + hNum);
        for(int i = 0; i < w1; ++i) for(int j = 0; j < h;  ++j) walls.add(new Wall(new Vector2i(i, j), new Vector2i(i + 1, j), 'h'));
        for(int i = 0; i < w;  ++i) for(int j = 0; j < h1; ++j) walls.add(new Wall(new Vector2i(i, j), new Vector2i(i, j + 1), 'v'));
        Collections.shuffle(walls);


        // Merge sets with Kruskal's Algorithm
        for (Wall wall : walls) {
            TreeNode a = tiles[wall.a.x][wall.a.y];
            TreeNode b = tiles[wall.b.x][wall.b.y];
            TreeNode aRoot = a.getRoot();
            TreeNode bRoot = b.getRoot();
            if (aRoot != bRoot) {
                new TreeNode(aRoot, bRoot);
                wall.up = false;
            }
            placeWall(wall, tileSize);
        }
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
