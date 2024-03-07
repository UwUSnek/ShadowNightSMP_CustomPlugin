package org.uwu_snek.shadownight.qol;


import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.Rnd;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;

import java.util.HashMap;




public class CMD_rtp implements CommandExecutor, Rnd {
    private final HashMap<String, Long> prev = new HashMap<>();


    private boolean isRtpBiomeInvalid(final @NotNull Biome biome){
        return
            biome == Biome.OCEAN ||
            biome == Biome.COLD_OCEAN ||
            biome == Biome.FROZEN_OCEAN ||
            biome == Biome.LUKEWARM_OCEAN ||

            biome == Biome.DEEP_OCEAN ||
            biome == Biome.DEEP_COLD_OCEAN ||
            biome == Biome.DEEP_FROZEN_OCEAN ||
            biome == Biome.DEEP_LUKEWARM_OCEAN ||

            biome == Biome.RIVER ||
            biome == Biome.FROZEN_RIVER
        ;
    }




    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        final Player player = (Player)sender;
        final int max = 10000;
        final Long cooldown = 10L * 60 * 1000;


        // Check cooldown
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


        // Send feedback to player
        ChatUtils.sendMessage(player, "§aTeleporting you to a random location...");
        ChatUtils.sendMessage(player, "§7You gained damage immunity for 15s");
        prev.put(player.getName(), System.currentTimeMillis());


        // Find new suitable location and teleport player there
        Location loc;
        do {
            float r = rnd.nextFloat(0, max);
            float a = rnd.nextFloat();
            loc = new Location(Bukkit.getWorld("survival"), r * Math.cos(a), 320, r * Math.sin(a), 0, 90);
        } while(isRtpBiomeInvalid(loc.getBlock().getBiome()));
        player.teleport(loc);


        // Apply blindness and damage immunity
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