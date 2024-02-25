package org.shadownight.plugin.shadownight.qol;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent.RespawnReason;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.Scheduler;




public class SpawnInvincibility extends UtilityClass {
    public static void onRespawn(Player player, RespawnReason reason) {
        if (reason == RespawnReason.DEATH) {
            player.setInvulnerable(true);
            Scheduler.delay(() -> player.setInvulnerable(false), 100L); // Logout exploit is managed by event listener in onPlayerQuit
        }
    }
}
