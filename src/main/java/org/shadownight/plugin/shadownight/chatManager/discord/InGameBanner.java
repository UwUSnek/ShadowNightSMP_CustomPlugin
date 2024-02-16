package org.shadownight.plugin.shadownight.chatManager.discord;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;


public final class InGameBanner {
    /**
     * Starts the banner message loop for the specified player.
     * @param player The player
     * @param plugin The plugin instance
     */
    public static void startLoop(@NotNull final Player player, @NotNull final Plugin plugin) {
        Bukkit.getScheduler().runTaskTimer(
            plugin,
            () -> {
                Chat.newline(player);
                Chat.separator(player);
                player.sendMessage(
                    """
                        §rDo you want to suggest a new feature or report a bug?
                        §9Join the §lDiscord§9 server! §7discord.com/invite/VZtg2jYDZ5
                        §rYou will also find details, rules and news.
                        §dWe hope to see you there!"""
                );
                Chat.separator(player);
                Chat.newline(player);
            },
            200L,       // Wait 10s
            3600 * 20L  // Repeat every 30m
        );
    }
}
