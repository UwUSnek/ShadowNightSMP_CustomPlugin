package org.shadownight.plugin.shadownight.QOL;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent.RespawnReason;
import org.shadownight.plugin.shadownight.ShadowNight;


public class SpawnInvincibility {
    public static void onRespawn(Player player, RespawnReason reason) {
        player.sendMessage("debug: event triggered");
        if (reason == RespawnReason.DEATH) {
            player.sendMessage("debug: reason: death");
            player.setInvulnerable(true);
            Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () -> player.setInvulnerable(false), 100L); // Logout exploit is managed by event listener in onPlayerQuit
        }
    }
}
