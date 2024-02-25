package org.shadownight.plugin.shadownight;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.chatManager.CMD_msg;
import org.shadownight.plugin.shadownight.economy.Economy;
import org.shadownight.plugin.shadownight.utils.spigot.ChatUtils;
import org.shadownight.plugin.shadownight.utils.spigot.PlayerUtils;

import java.lang.Math;


public final class ShadowNight_papi extends PlaceholderExpansion {
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
    public String onRequest(final @NotNull OfflinePlayer offlinePlayer, final @NotNull String placeholder) {
        final Player player = offlinePlayer.getPlayer();
        switch (placeholder.toLowerCase()) {
            case "location": {
                if (player == null) return null;
                final String worldName = player.getWorld().getName();
                if (worldName.equals("Spawn")) return "§6Public Spawn point";
                if (worldName.equals("Survival") || worldName.equals("Survival_nether") || worldName.equals("Survival_the_end")) {
                    //else {
                    final String ownerName = PlaceholderAPI.setPlaceholders(player, "%griefprevention_currentclaim_ownername%");
                    if (!ownerName.equals("Unclaimed")) {
                        Player owner = Bukkit.getPlayer(ownerName);
                        if (owner == null) return PlayerUtils.getFancyNameOffline(Bukkit.getOfflinePlayer(ownerName)) + "§r's Claim §7(Offline)";
                        else return PlayerUtils.getFancyName(Bukkit.getPlayer(ownerName)) + "§r's Claim";
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
            case "chat_channel": {
                if (player == null) return null;
                final String targetName = CMD_msg.openDms.get(player.getName());
                if (targetName == null) return "Chat channel: §aPublic";
                else return "§dMessaging " + targetName;
            }
            case "playtime": {
                return "Your playtime: " + ChatUtils.sToDuration(offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, true);
            }
            case "player_balance": {
                return String.valueOf(Economy.getBalance(offlinePlayer));
            }
            case "high_level_warning": {
                if (player == null) return null;
                Location coords = player.getLocation();
                if (Math.sqrt(Math.pow(coords.getX(), 2) + Math.pow(coords.getZ(), 2)) > 10000) {
                    return "§4§lDANGER: You are past the 10k blocks safe zone. Mobs will get stronger as you travel further from spawn.";
                }
                else return "§2§lYou are currently in the safe zone. Mobs will stay the same level throughout it. Good luck.";
            }
        }

        return null; // Placeholder is unknown by the Expansion
    }
}