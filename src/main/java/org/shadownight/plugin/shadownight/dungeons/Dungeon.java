package org.shadownight.plugin.shadownight.dungeons;


import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.stream.Collectors;


public class Dungeon {
    //! Prefix to use in dungeon world names. MUST be all lowercase
    private static final String namePrefix = "dungeon_";

    public final World world;
    final Random rnd = new Random();

    public Dungeon() {
        world = Bukkit.createWorld(new WorldCreator(namePrefix + "fewifjwefoio")
            .environment(World.Environment.NORMAL)
            .type(WorldType.FLAT)
            .generator("VoidGenerator")
            .generateStructures(false)
            .biomeProvider(new BiomeProvider() {
                @NotNull @Override
                public Biome getBiome(@NotNull WorldInfo worldInfo, int i, int i1, int i2) {
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

    private void generateDungeon(){
        int tileSize = 3;  // The size of each tile
        int length = 51;   // The length of each side measured in tiles. Must be an odd number
        boolean[][] tiles = new boolean[length][length];

        for(int x = 0; x < length; ++x) for(int z = 0; z < length; ++z) {
            tiles[x][z] = rnd.nextBoolean();
        }
        for(int x = 0; x < length; ++x) for(int z = 0; z < length; ++z) {
            if(tiles[x][z]) placeTile(x, z, tileSize);
            utils.log(Level.WARNING, "Placed " + x + ", " + z);
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
