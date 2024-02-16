package org.shadownight.plugin.shadownight.qol;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.Random;


public class CMD_rtp implements CommandExecutor, Rnd {
    private final HashMap<String, Long> prev = new HashMap<>();



    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, final String[] args) {
        final Player player = (Player)sender;
        final int max = 10000;
        final Long cooldown = 10L * 60 * 1000;

        Long last_time = prev.get(player.getName());
        if(last_time != null) {
            Long time_diff = System.currentTimeMillis() - last_time;
            if (time_diff < cooldown){
                long s_left = (cooldown - time_diff) / 1000;
                Chat.sendMessage(player, "§cThis command is on cooldown! Try again in " + s_left / 60 + "m" + s_left % 60 + "s");
                if(!player.hasPermission("group.mod")) return true;
                Chat.sendMessage(player, "§7Skipping cooldown... (Mod rank or higher)");
            }
        }

        Chat.sendMessage(player, "§aTeleporting you to a random location...");
        Chat.sendMessage(player, "§7You gained damage immunity for 15s");
        prev.put(player.getName(), System.currentTimeMillis());

        float r = rnd.nextFloat(0, max);
        float a = rnd.nextFloat();
        Location loc = new Location(Bukkit.getWorld("Survival"), r * Math.cos(a), 320, r * Math.sin(a), 0, 90);
        player.teleport(loc);

        PotionEffect effect2 = new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, false, false);
        player.addPotionEffect(effect2);
        player.setInvulnerable(true);
        Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () -> { // Logout exploit is managed by event listener in onPlayerQuit
            Chat.sendMessage(player, "§7Your damage immunity ran out");
            player.setInvulnerable(false);
        }, 300L);
        return true;
    }
}