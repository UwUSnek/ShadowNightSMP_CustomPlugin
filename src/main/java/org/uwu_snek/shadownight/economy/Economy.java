package org.uwu_snek.shadownight.economy;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight.utils.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public final class Economy extends UtilityClass {
    private static File databaseFile;
    public static FileConfiguration databaseObject;

    public static final HashMap<UUID, Long> database = new HashMap<>();


    /**
     * Loads the database from the configuration file.
     */
    public static void loadDatabase() {
        databaseFile = new File(ShadowNight.plugin.getDataFolder(), "economy.yml");
        if (!databaseFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            databaseFile.getParentFile().mkdirs();
            ShadowNight.plugin.saveResource("economy.yml", false);
        }

        databaseObject = new YamlConfiguration();
        try {
            databaseObject.load(databaseFile);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }


        for(String key : databaseObject.getKeys(false)){
            database.put(UUID.fromString(key), databaseObject.getLong(key));
        }
    }


    /**
     * Adds a new player to the database.
     * If the player already exists, the old data will be overridden.
     * @param player The new player
     */
    public static void addPlayer(final @NotNull Player player){
        final UUID uuid = player.getUniqueId();
        if(!databaseObject.contains(uuid.toString())){
            database.put(uuid, 1000L);
        }
    }

    /**
     * Saves the database to the configuration file.
     */
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




    /**
     * Returns the amount of coins of a player.
     * @param player The player
     * @return The amount of coins
     */
    public static long getBalance(final @NotNull Player player) {
        return database.get(player.getUniqueId());
    }

    /**
     * Returns the amount of coins of a player.
     * @param offlinePlayer The player
     * @return The amount of coins
     */
    public static long getBalance(final @NotNull OfflinePlayer offlinePlayer) {
        return database.get(offlinePlayer.getUniqueId());
    }


    /**
     * Gives <n> coins to a player.
     * @param player The target player
     * @param n The amount of coins to add
     */
    public static void addToBalance(Player player, long n) {
        database.computeIfPresent(player.getUniqueId(), (key, value) -> value + n);
    }

    /**
     * Removes <n> coins from a player.
     * @param player The target player
     * @param n The amount of coins to remove
     */
    public static void removeFromBalance(Player player, long n) {
        database.computeIfPresent(player.getUniqueId(), (key, value) -> value - n);
    }
}
