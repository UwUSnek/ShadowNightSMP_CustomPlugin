package org.shadownightsmp.plugin.shadownightsmp.QOL;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.shadownightsmp.plugin.shadownightsmp.ShadowNightSMP;
import org.shadownightsmp.plugin.shadownightsmp.utils.utils;


public class SurvivalFly {
    public static void updateState(Player player) {
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            if (player.hasPermission("group.vip")) {
                if (player.getWorld().getName().equals("Spawn") && !player.getAllowFlight()) {
                    // MULTIVERSE-CORE IS BUGGED. THIS IS A WORKAROUND.
                    // Multiverse resets the flying state when changing the player's gamemode on world change.
                    // The only way to change this is give the player the "mv.bypass.gamemode.<world>" permission, which is not something we can do.
                    // This task is used to re-activate flying 2s after the world change (hopefully after Multiverse turns it off).
                    utils.sendMessage(player, "§7[Vip] Flight will be available in 2s.");
                    Bukkit.getScheduler().runTaskLater(ShadowNightSMP.plugin, () -> { if(player.getWorld().getName().equals("Spawn")) player.setAllowFlight(true); } ,40);
                }
                else if (!player.getWorld().getName().equals("Spawn") && player.getAllowFlight()) {
                    utils.sendMessage(player, "§7[Vip] Flight has been disabled.");
                    player.setAllowFlight(false);
                    player.setFlying(false);
                }
            }
            else {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }
    }
}