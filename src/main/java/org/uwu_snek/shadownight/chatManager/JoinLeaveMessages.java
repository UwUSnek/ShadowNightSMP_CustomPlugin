package org.uwu_snek.shadownight.chatManager;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.chatManager.discord.BotManager;
import org.uwu_snek.shadownight.utils.UtilityClass;


public final class JoinLeaveMessages extends UtilityClass {
    /**
     * Modifies the join event message.
     * @param event The join event
     */
    public static void formatJoin(final @NotNull PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("§8[§a§l+§8]§7 " + player.getName());
        BotManager.sendBridgeMessage(player, "☑️ " + player.getName() + " joined the game" + (player.hasPlayedBefore() ? "" : " for the first time!"));
    }

    /**
     * Modifies the quit event message.
     * @param event The quit event
     */
    public static void formatQuit(final @NotNull PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage("§8[§c§l-§8]§7 " + player.getName());
        BotManager.sendBridgeMessage(player, "🇽 " + player.getName() + " left the game");
    }
}
