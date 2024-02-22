package org.shadownight.plugin.shadownight.qol;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.spigot.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.shadownight.plugin.shadownight.utils.spigot.Scheduler;

import java.util.HashMap;


public class CMD_rtp implements CommandExecutor, Rnd {
    private final HashMap<String, Long> prev = new HashMap<>();



    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        final Player player = (Player)sender;
        final int max = 10000;
        final Long cooldown = 10L * 60 * 1000;

        Long last_time = prev.get(player.getName());
        if(last_time != null) {
            Long time_diff = System.currentTimeMillis() - last_time;
            if (time_diff < cooldown){
                long s_left = (cooldown - time_diff) / 1000;
                ChatUtils.sendMessage(player, "§cThis command is on cooldown! Try again in " + s_left / 60 + "m" + s_left % 60 + "s");
                if(!player.hasPermission("group.mod")) return true;
                ChatUtils.sendMessage(player, "§7Skipping cooldown... (Mod rank or higher)");
            }
        }

        ChatUtils.sendMessage(player, "§aTeleporting you to a random location...");
        ChatUtils.sendMessage(player, "§7You gained damage immunity for 15s");
        prev.put(player.getName(), System.currentTimeMillis());

        float r = rnd.nextFloat(0, max);
        float a = rnd.nextFloat();
        Location loc = new Location(Bukkit.getWorld("Survival"), r * Math.cos(a), 320, r * Math.sin(a), 0, 90);
        player.teleport(loc);

        PotionEffect effect2 = new PotionEffect(PotionEffectType.BLINDNESS, 60, 1, false, false);
        player.addPotionEffect(effect2);
        player.setInvulnerable(true);
        Scheduler.delay(() -> { // Logout exploit is managed by event listener in onPlayerQuit
            ChatUtils.sendMessage(player, "§7Your damage immunity ran out");
            player.setInvulnerable(false);
        }, 300L);
        return true;
    }
}