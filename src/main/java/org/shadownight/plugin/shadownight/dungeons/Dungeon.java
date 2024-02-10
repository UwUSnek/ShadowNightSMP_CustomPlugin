package org.shadownight.plugin.shadownight.dungeons;


import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Dungeon {
    public final World world;
    Random rnd = new Random();

    public Dungeon() {
        world = Bukkit.createWorld(new WorldCreator("test_dungeon")
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


        world.getBlockAt(world.getSpawnLocation()).setType(Material.RESPAWN_ANCHOR);
        Bukkit.getScheduler().runTaskTimer(ShadowNight.plugin, () -> {
            world.getBlockAt(new Location(world, rnd.nextInt(0, 50), rnd.nextInt(0, 50), rnd.nextInt(0, 50))).setType(Material.RESPAWN_ANCHOR);
        },
        1L, 1L);
    }
}
