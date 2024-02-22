package org.shadownight.plugin.shadownight.qol.info;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.ChatUtils;


public final class CMD_playtime implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        final Player player = (Player)sender;
        if(args.length == 0) {
            ChatUtils.sendMessage(player, "Your playtime is §a" + ChatUtils.sToDuration(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, true) + "§r.");
        }
        else {
            @SuppressWarnings("deprecation") // Using getOfflinePlayer the way it's meant to be used, no need to warn
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if(target.hasPlayedBefore()) {
                ChatUtils.sendMessage(player, target.getName() + "'s playtime is §a" + ChatUtils.sToDuration(target.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, true) + "§.");
            }
            else {
                ChatUtils.sendMessage(player, "The player \"" + target.getName() + "\" doesn't exist or has never played on Shadow Night!");
            }
        }
        return true;
    }
}