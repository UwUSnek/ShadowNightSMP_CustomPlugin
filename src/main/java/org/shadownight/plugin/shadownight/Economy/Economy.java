package org.shadownight.plugin.shadownight.Economy;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.shadownight.plugin.shadownight.ShadowNight;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class Economy {
    private static File databaseFile;
    public static FileConfiguration databaseObject;


    public static final HashMap<UUID, Long> database = new HashMap<>();


    public static void loadDatabase() {
        databaseFile = new File(ShadowNight.plugin.getDataFolder(), "economy.yml");
        if (!databaseFile.exists()) {
            databaseFile.getParentFile().mkdirs();
            ShadowNight.plugin.saveResource("economy.yml", false);
        }

        databaseObject = new YamlConfiguration();
        try {
            databaseObject.load(databaseFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }


        for(String key : databaseObject.getKeys(false)){
            database.put(UUID.fromString(key), databaseObject.getLong(key));
        }
    }



    public static void addPlayer(Player player){
        UUID uuid = player.getUniqueId();
        if(!databaseObject.contains(uuid.toString())){
            database.put(uuid, 1000L);
        }
    }

    public static void saveDatabase(){
        for(Map.Entry<UUID, Long> entry : database.entrySet()){
            databaseObject.set(entry.getKey().toString(), entry.getValue());
        }

        try {
            databaseObject.save(databaseFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public static long getBalance(Player player) {
        return database.get(player.getUniqueId());
    }
    public static void addToBalance(Player player, long n) {
        database.computeIfPresent(player.getUniqueId(), (key, value) -> value + n);
    }
    public static void removeFromBalance(Player player, long n) {
        database.computeIfPresent(player.getUniqueId(), (key, value) -> value - n);
    }
}
