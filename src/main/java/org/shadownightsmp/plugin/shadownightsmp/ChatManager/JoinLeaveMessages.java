package org.shadownightsmp.plugin.shadownightsmp.ChatManager;

import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.shadownightsmp.plugin.shadownightsmp.utils.utils;



public class JoinLeaveMessages {
    static public void formatJoin(PlayerJoinEvent event) {
        event.setJoinMessage("§8[§a§l+§8]§7 " + utils.getFancyName(event.getPlayer()));
    }

    static public void formatQuit(PlayerQuitEvent event) {
        event.setQuitMessage("§8[§c§l-§8]§7 " + utils.getFancyName(event.getPlayer()));
    }
}
