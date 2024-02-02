package org.shadownight.plugin.shadownight.chatManager;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.shadownight.plugin.shadownight.utils.utils;


public class DiscordBanner {
    // Starts the banner loop for that player
    public static void startLoop(Player player, Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(
            plugin,
            () -> {
                utils.newline(player);
                utils.separator(player);
                player.sendMessage(
                    """
                        §rDo you want to suggest a new feature or report a bug?
                        §9Join the §lDiscord§9 server! §7discord.com/invite/VZtg2jYDZ5
                        §rYou will also find details, rules and news.
                        §dWe hope to see you there!"""
                );
                utils.separator(player);
                utils.newline(player);
            },
            200L,       // Wait 10s
            3600 * 20L  // Repeat every 30m
        );
    }
}
