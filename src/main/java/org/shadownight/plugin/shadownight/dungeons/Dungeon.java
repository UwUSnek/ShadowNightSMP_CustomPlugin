package org.shadownight.plugin.shadownight.dungeons;


import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.dungeons.generators.*;
import org.shadownight.plugin.shadownight.dungeons.shaders.*;
import org.shadownight.plugin.shadownight.utils.graphics.PerlinNoise;
import org.shadownight.plugin.shadownight.utils.containers.RegionBlueprint;
import org.shadownight.plugin.shadownight.utils.containers.BlueprintData;
import org.shadownight.plugin.shadownight.utils.math.Func;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.shadownight.plugin.shadownight.utils.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;


public final class Dungeon {
    //! Prefix to use in dungeon world names. MUST be all lowercase
    private static final String namePrefix = "sn_world_dungeon_";
    public World world = null;


    /**
     * Creates a new Dungeon.
     */
    public Dungeon() {
        if (createWorld()) generateDungeon();
    }


    /**
     * Creates the world the dungeon will be in.
     * @return True if the world was generated correctly, false otherwise
     */
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

            // World settings
            world.setTime(13000); // Night
            world.setClearWeatherDuration(2147483647);
            world.setDifficulty(Difficulty.HARD);
            world.setHardcore(false);
            world.setPVP(true);
        }
        return true;
    }




    /**
     * Generates the dungeon structure.
     */
    public void generateDungeon() {
        final long start = System.currentTimeMillis();

        // Inner walls data
        final int tileSize = 13;                                    // The size of each tile
        final int wallThickness = 9;                                // The thickness of the inner maze walls
        final int wallHeight = 60;                                  // The height of the inner maze walls
        final int xNum = 11;                                        // The height of the maze expressed in tiles. Must be an odd number
        final int zNum = 11;                                        // The width  of the maze expressed in tiles. Must be an odd number


        // Outer walls and floor data
        final int x = xNum * tileSize + (xNum - 1) * wallThickness; // The height of the dungeon expressed in blocks
        final int z = zNum * tileSize + (zNum - 1) * wallThickness; // The width  of the dungeon expressed in blocks
        final int floorThickness = 5;                               // The thickness of the floor
        final int ceilingThickness = 5;                             // The thickness of the ceiling
        final int outerWallsThickness = 9;                          // The thickness of the outer walls

        //noinspection ConstantConditions
        if(outerWallsThickness > wallThickness) utils.log(Level.SEVERE, "Outer wall thickness must be >= wall thickness"); // Prevent out of bounds
        //noinspection ConstantConditions
        if(floorThickness < 2) utils.log(Level.SEVERE, "Floor thickness must be at least 2");
        //noinspection ConstantConditions
        if(ceilingThickness < 2) utils.log(Level.SEVERE, "Ceiling thickness must be at least 2");



        // Actually generate the dungeon
        {
            // Calculate total size and create region buffer
            final int total_x = x + outerWallsThickness * 2;
            final int total_y = wallHeight + floorThickness + ceilingThickness;
            final int total_z = z + outerWallsThickness * 2;
            final RegionBlueprint templateBuffer = new RegionBlueprint(total_x, total_y, total_z, outerWallsThickness, floorThickness, outerWallsThickness);

            // Generate blueprint
            PerlinNoise.resetSeed(); GEN_BoundingBox.startFloor  (templateBuffer, floorThickness);
            PerlinNoise.resetSeed(); GEN_BoundingBox.startCeiling(templateBuffer, ceilingThickness, wallHeight, floorThickness);
            PerlinNoise.resetSeed(); GEN_Walls.start             (templateBuffer, tileSize, wallThickness, wallHeight, xNum, zNum, floorThickness); // Must be 2nd in order to generate into the floor
            PerlinNoise.resetSeed(); GEN_BoundingBox.startWalls  (templateBuffer, outerWallsThickness, wallHeight, floorThickness, wallThickness);

            // Calculate normals and base distance gradient using the default walls, then run the wall deform shader
            final float[][] wallDistanceGradient = templateBuffer.createWallDistanceGradient(floorThickness, tileSize, wallThickness, false);
            final Vector2i[][] wallNormals = templateBuffer.createWallNormals(floorThickness, wallThickness, tileSize);
            PerlinNoise.resetSeed(); GEN_WallsDeform.start(templateBuffer, floorThickness, wallHeight);
            PerlinNoise.resetSeed(); GEN_WallVines.start(templateBuffer, wallNormals, floorThickness);
            PerlinNoise.resetSeed(); GEN_WallMoss.start(templateBuffer, floorThickness);

            // Calculate top distance gradient and generate the rest of the blueprint
            final float[][] wallDistanceGradientHigh = templateBuffer.createWallDistanceGradient(floorThickness + wallHeight - 1, tileSize, wallThickness, true);
            PerlinNoise.resetSeed(); GEN_CeilingDeform.start(templateBuffer, wallDistanceGradientHigh, floorThickness, wallHeight);


            // Apply material shaders and paste the structure into the world
            templateBuffer.dispatchShaders(8,
                Arrays.asList(
                    Pair.with(BlueprintData.FLOOR,        new SHD_FloorMaterial(wallDistanceGradient, floorThickness)),
                    Pair.with(BlueprintData.WALL,         new SHD_WallMaterial(wallHeight, floorThickness)),
                    Pair.with(BlueprintData.WALL_MOSS,    new SHD_WallMoss()),
                    Pair.with(BlueprintData.WALL_VINE,    new SHD_WallVines()),
                    Pair.with(BlueprintData.CEILING,      new SHD_CeilingMaterial()),
                    Pair.with(BlueprintData.CEILING_VINE, new SHD_CeilingVines())
                ),
                (outputBuffer) -> {
                    // Log generation time
                    utils.log(Level.INFO, "Dungeon generated in " + Chat.msToDuration(System.currentTimeMillis() - start, true));

                    // Paste and log pasting time
                    final long pasteStart = System.currentTimeMillis();
                    outputBuffer.paste(world, -total_x / 2, 0, -total_z / 2, true);
                    utils.log(Level.INFO, "Dungeon pasted in " + Chat.msToDuration(System.currentTimeMillis() - pasteStart, true));
                }
            );
        }
    }



    /**
     * Deleted any remaining dungeon that wasn't deleted on shutdown for whatever reason
     * This MUST be called first in the .onLoad function
     */
    public static void deleteOldDungeons() {
        final List<File> worlds;
        try(Stream<Path> stream = Files.list(Paths.get(ShadowNight.plugin.getServer().getWorldContainer().getPath()))) {
            worlds = stream
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

