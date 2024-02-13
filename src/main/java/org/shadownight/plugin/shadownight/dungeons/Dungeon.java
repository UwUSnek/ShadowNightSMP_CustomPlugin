package org.shadownight.plugin.shadownight.dungeons;


import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.dungeons.generators.GEN_BoundingBox;
import org.shadownight.plugin.shadownight.dungeons.generators.GEN_Walls;
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

    /*
        s = 9  (9x9 tiles)
        t = 2  (9x2 walls)                             X
                                                       ╎
        ||·········||·········||·········||            │
        ╰──╴s+t╶──╯  ╰──╴s╶──╯╰┴ t                     └───--- Z
            11           9       3
    */

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
        v2i a = wall.a;
        switch(wall.type) {
            case 'x': for (int i = a.x * st; i < a.x * st + st + t; ++i) world.getBlockAt(i, 0, a.z * st + st).setType(Material.STONE); break;
            case 'z': for (int i = a.z * st; i < a.z * st + st + t; ++i) world.getBlockAt(a.x * st + st, 0, i).setType(Material.STONE); break;
        }
    }




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


    public void generateDungeon(){
        long start = System.currentTimeMillis();


        // Inner walls
        int tileSize = 3;                                     // The size of each tile
        int wallThickness = 1;                                // The thickness of the inner maze walls //TODO implement this
        int wallHeight = 5;                                   // The height of the inner maze walls
        int xNum = 21;                                        // The height of the maze expressed in tiles. Must be an odd number
        int zNum = 41;                                        // The width  of the maze expressed in tiles. Must be an odd number
        GEN_Walls.start(world, Material.STONE, tileSize, wallThickness, wallHeight, xNum, zNum);


        // Outer walls and floor
        int x = xNum * tileSize + (xNum - 1) * wallThickness; // The height of the dungeon expressed in blocks
        int z = zNum * tileSize + (zNum - 1) * wallThickness; // The width  of the dungeon expressed in blocks
        int floorThickness = 5;                               // The thickness of the floor
        int outerWallsThickness = 5;                          // The thickness of the outer walls
        int outerWallsHeight = 20;                            // The height of the outer walls
        GEN_BoundingBox.startFloor(world, Material.DIRT, floorThickness, outerWallsThickness, x, z);
        GEN_BoundingBox.startWalls(world, Material.BEDROCK, outerWallsThickness, outerWallsHeight, x, z);


        // Log generation time
        long duration = System.currentTimeMillis() - start;
        utils.log(Level.INFO, "Dungeon generated in " + utils.msToDuration(duration, true));
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
