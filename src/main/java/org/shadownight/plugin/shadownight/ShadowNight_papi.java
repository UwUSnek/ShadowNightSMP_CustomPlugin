package org.shadownight.plugin.shadownight;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ChatManager.CMD_msg;
import org.shadownight.plugin.shadownight.Economy.Economy;
import org.shadownight.plugin.shadownight.utils.utils;

import java.lang.Math;


public class ShadowNight_papi extends PlaceholderExpansion {
    @Override
    public @NotNull String getAuthor() {
        return "UwU_Snek";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "shadownightsmp";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    @SuppressWarnings("deprecation") // This is for getOfflinePlayer bc GriefPrevention doesn't have a placeholder to get the UUID
    public String onRequest(OfflinePlayer offline_player, String placeholder) {
        Player player = offline_player.getPlayer();
        if (placeholder.equalsIgnoreCase("location")) {
            if (player == null) return null;
            String worldName = player.getWorld().getName();
            if (worldName.equals("Spawn")) return "§6Public Spawn point";
            if (worldName.equals("Survival") || worldName.equals("Survival_nether") || worldName.equals("Survival_the_end")) {
            //else {
                String ownerName = PlaceholderAPI.setPlaceholders(player, "%griefprevention_currentclaim_ownername%");
                if (!ownerName.equals("Unclaimed")) {
                    Player owner = Bukkit.getPlayer(ownerName);
                    if(owner == null) return utils.getFancyNameOffline(Bukkit.getOfflinePlayer(ownerName)) + "§r's Claim §7(Offline)";
                    else              return utils.getFancyName       (Bukkit.getPlayer       (ownerName)) + "§r's Claim";
                }
                else switch (worldName) {
                    case "Survival": return "§2Wilderness";
                    case "Survival_nether": return "§4Unmapped region of Hell";
                    case "Survival_the_end": return "§9Indistinct Void patch";
                }
            }

            // Check for dungeons here
            //TODO

            return "§fUnknown location";
        }
        if (placeholder.equalsIgnoreCase("chat_channel")) {
            if (player == null) return null;
            String targetName = CMD_msg.openDms.get(player.getName());
            if (targetName == null) return "Chat channel: §aPublic";
            else return "§dMessaging " + targetName;
        }
        if (placeholder.equalsIgnoreCase("playtime")) {
            return "Your playtime: " + utils.sToDuration(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, true);
        }
        if (placeholder.equalsIgnoreCase("player_balance")) {
            return String.valueOf(Economy.getBalance(player));
        }
        if (placeholder.equalsIgnoreCase("high_level_warning")) {
            Location coords = player.getLocation();
            if(Math.sqrt(Math.pow(coords.getX(), 2) + Math.pow(coords.getZ(), 2)) > 10000) {
                return "§4§lDANGER: You are past the 10k blocks safe zone. Mobs will get stronger as you travel further from spawn.";
            }
            else return "§2§lYou are currently in the safe zone. Mobs will stay the same level throughout it. Good luck.";
        }

        return null; // Placeholder is unknown by the Expansion
    }
}