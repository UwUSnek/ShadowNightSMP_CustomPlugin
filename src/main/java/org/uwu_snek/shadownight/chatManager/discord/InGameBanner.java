package org.uwu_snek.shadownight.chatManager.discord;


import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public final class InGameBanner {
    /**
     * Starts the banner message loop for the specified player.
     * @param player The player
     */
    public static void startLoop(final @NotNull Player player) {
        Scheduler.loop(
            () -> {
                ChatUtils.newline(player);
                ChatUtils.separator(player);
                player.sendMessage(
                    """
                        §rDo you want to suggest a new feature or report a bug?
                        §9Join the §lDiscord§9 server! §7discord.com/invite/VZtg2jYDZ5
                        §rYou will also find details, rules and news.
                        §dWe hope to see you there!"""
                );
                ChatUtils.separator(player);
                ChatUtils.newline(player);
            },
            200L,       // Wait 10s
            3600 * 20L  // Repeat every 30m
        );
    }
}
