package org.shadownightsmp.plugin.shadownightsmp.QOL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent.RespawnReason;
import org.shadownightsmp.plugin.shadownightsmp.ShadowNightSMP;


public class SpawnInvincibility {
    public static void onRespawn(Player player, RespawnReason reason) {
        player.sendMessage("debug: event triggered");
        if (reason == RespawnReason.DEATH) {
            player.sendMessage("debug: reason: death");
            player.setInvulnerable(true);
            Bukkit.getScheduler().runTaskLater(ShadowNightSMP.plugin, () -> player.setInvulnerable(false), 100L); // Logout exploit is managed by event listener in onPlayerQuit
        }
    }
}
