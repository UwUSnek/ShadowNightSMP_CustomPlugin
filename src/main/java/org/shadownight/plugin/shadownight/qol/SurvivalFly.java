package org.shadownight.plugin.shadownight.qol;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.shadownight.plugin.shadownight.utils.spigot.Scheduler;




public class SurvivalFly extends UtilityClass {
    /**
     * Updates the allowFlight value of the player <player> based on their rank and current world.
     * @param player The player to check
     */
    public static void updateState(final @NotNull Player player) {
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            if (player.hasPermission("group.vip")) {
                if (player.getWorld().getName().equals("Spawn") && !player.getAllowFlight()) {
                    // MULTIVERSE-CORE IS BUGGED. THIS IS A WORKAROUND.
                    // Multiverse resets the flying state when changing the player's gamemode on world change.
                    // The only way to change this is give the player the "mv.bypass.gamemode.<world>" permission, which is not something we can do.
                    // This task is used to re-activate flying 2s after the world change (hopefully after Multiverse turns it off).
                    Chat.sendMessage(player, "§7[Vip] Flight will be available in 2s.");
                    Scheduler.delay(() -> { if(player.getWorld().getName().equals("Spawn")) player.setAllowFlight(true); } , 40);
                }
                else if (!player.getWorld().getName().equals("Spawn") && player.getAllowFlight()) {
                    Chat.sendMessage(player, "§7[Vip] Flight has been disabled.");
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