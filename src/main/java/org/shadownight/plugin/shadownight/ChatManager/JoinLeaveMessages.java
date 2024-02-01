package org.shadownight.plugin.shadownight.ChatManager;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class JoinLeaveMessages {
    static public void formatJoin(PlayerJoinEvent event) {
        event.setJoinMessage("§8[§a§l+§8]§7 " + event.getPlayer().getName());
    }

    static public void formatQuit(PlayerQuitEvent event) {
        event.setQuitMessage("§8[§c§l-§8]§7 " + event.getPlayer().getName());
    }
}
