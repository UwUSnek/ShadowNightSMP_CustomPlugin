package org.shadownight.plugin.shadownight.chatManager;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.shadownight.plugin.shadownight.chatManager.discord.BotManager;


public class JoinLeaveMessages {
    static public void formatJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("§8[§a§l+§8]§7 " + player.getName());
        BotManager.sendBridgeMessage(player, "☑️ " + player.getName() + " joined the game" + (player.hasPlayedBefore() ? "" : " for the first time!"));
    }

    static public void formatQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage("§8[§c§l-§8]§7 " + player.getName());
        BotManager.sendBridgeMessage(player, "🇽 " + player.getName() + " left the game");
    }
}
