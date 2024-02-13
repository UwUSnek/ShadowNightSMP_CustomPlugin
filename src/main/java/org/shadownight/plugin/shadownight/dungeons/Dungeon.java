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
    private static final String namePrefix = "sn_world_dungeon_";
    public World world = null;


    public Dungeon() {
        if (createWorld()) generateDungeon();
    }


    private boolean createWorld() {
        world = Bukkit.createWorld(new WorldCreator(namePrefix + UUID.randomUUID())
            .environment(World.Environment.NORMAL)
            .type(WorldType.NORMAL) //! FLAT creates errors in console. This is a Minecraft Vanilla bug
            .generator("VoidGenerator")
            .generateStructures(false)
            .biomeProvider(new BiomeProvider() {
                @NotNull
                @Override
                public Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
                    return Biome.PLAINS;
                }

                @NotNull
                @Override
                public List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
                    return List.of(Biome.PLAINS);
                }
            })
        );

        if (world == null) {
            utils.log(Level.SEVERE, "Dungeon world creation failed: createWorld returned null");
            return false;
        }
        else {
            // Griefing and drop gamerules
            world.setGameRule(GameRule.MOB_GRIEFING, false);
            world.setGameRule(GameRule.DO_VINES_SPREAD, false);
            world.setGameRule(GameRule.DO_MOB_LOOT, false);
            world.setGameRule(GameRule.DO_ENTITY_DROPS, false);

            // Spawning gamerules
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
            world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);
            world.setGameRule(GameRule.DO_WARDEN_SPAWNING, false);
            world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);
            world.setGameRule(GameRule.DISABLE_RAIDS, true);

            // Environment gamerules
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_FIRE_TICK, false);
            world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
            world.setGameRule(GameRule.SNOW_ACCUMULATION_HEIGHT, 0);

            // Player gamerules
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
            world.setGameRule(GameRule.DO_INSOMNIA, false);
            world.setGameRule(GameRule.KEEP_INVENTORY, true);
            world.setGameRule(GameRule.PLAYERS_NETHER_PORTAL_DEFAULT_DELAY, 2147483647);
            world.setGameRule(GameRule.PLAYERS_NETHER_PORTAL_CREATIVE_DELAY, 2147483647);
            world.setGameRule(GameRule.PLAYERS_SLEEPING_PERCENTAGE, 101);
        }
        return true;
    }


    public void generateDungeon() {
        long start = System.currentTimeMillis();


        // Inner walls data
        int tileSize = 9;                                     // The size of each tile
        int wallThickness = 8;                                // The thickness of the inner maze walls
        int wallHeight = 5;                                   // The height of the inner maze walls
        int xNum = 11;                                        // The height of the maze expressed in tiles. Must be an odd number
        int zNum = 21;                                        // The width  of the maze expressed in tiles. Must be an odd number
        Material materialWalls = Material.STONE;              // Temporary material used for inner maze walls


        // Outer walls and floor data
        int x = xNum * tileSize + (xNum - 1) * wallThickness; // The height of the dungeon expressed in blocks
        int z = zNum * tileSize + (zNum - 1) * wallThickness; // The width  of the dungeon expressed in blocks
        int floorThickness = 5;                               // The thickness of the floor
        int outerWallsThickness = 5;                          // The thickness of the outer walls
        int outerWallsHeight = 20;                            // The height of the outer walls
        Material materialOuterWalls = Material.BEDROCK;       // Temporary material used for outer walls
        Material materialFloor = Material.DIRT;               // Temporary material used for the floor
        outerWallsThickness = Math.max(outerWallsThickness, wallThickness); // Prevents accidental out of bound exceptions. Negligible performance impact


        // Actually generate the dungeon
        int total_x = x + outerWallsThickness * 2;
        int total_z = z + outerWallsThickness * 2;
        RegionBuffer buffer = new RegionBuffer(total_x, outerWallsHeight + floorThickness, total_z, outerWallsThickness, floorThickness, outerWallsThickness);

        GEN_Walls.start(buffer, materialWalls, tileSize, wallThickness, wallHeight, xNum, zNum);
        GEN_BoundingBox.startFloor(buffer, materialFloor, floorThickness, outerWallsThickness, x, z);
        GEN_BoundingBox.startWalls(buffer, materialOuterWalls, outerWallsThickness, outerWallsHeight, x, z);

        buffer.paste(world, -total_x / 2, 0, -total_z / 2);


        // Log generation time
        long duration = System.currentTimeMillis() - start;
        utils.log(Level.INFO, "Dungeon generated in " + utils.msToDuration(duration, true));
    }


    /**
     * Deleted any remaining dungeon that wasn't deleted on shutdown for whatever reason
     * This MUST be called first in the .onLoad function
     */
    public static void deleteOldDungeons() {
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

        if (!worlds.isEmpty()) {
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
