package org.shadownightsmp.plugin.shadownightsmp.Economy;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.shadownightsmp.plugin.shadownightsmp.ShadowNightSMP;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Economy {
    private static File databaseFile;
    public static FileConfiguration database;


    public static final HashMap<UUID, Long> coins = new HashMap<>();


    public static void loadDatabase() {
        databaseFile = new File(ShadowNightSMP.plugin.getDataFolder(), "economy.yml");
        if (!databaseFile.exists()) {
            databaseFile.getParentFile().mkdirs();
            ShadowNightSMP.plugin.saveResource("economy.yml", false);
        }

        database = new YamlConfiguration();
        try {
            database.load(databaseFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }


        for(String key : database.getKeys(false)){
            coins.put(UUID.fromString(key), database.getLong(key));
        }
    }



    public static void addPlayer(Player player){
        UUID uuid = player.getUniqueId();
        if(!database.contains(uuid.toString())){
            database.set(uuid.toString(), 1000);
        }
    }

    public static void saveDatabase(){
        for(Map.Entry<UUID, Long> entry : coins.entrySet()){
            database.set(entry.getKey().toString(), entry.getValue());
        }

        try {
            database.save(databaseFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static long getBalance(Player player) {
        return database.getLong(player.getUniqueId().toString());
    }
    public static void addToBalance(Player player, long n) {
        coins.computeIfPresent(player.getUniqueId(), (key, value) -> value + n);
    }
    public static void removeFromBalance(Player player, long n) {
        coins.computeIfPresent(player.getUniqueId(), (key, value) -> value - n);
    }
}
