package org.shadownight.plugin.shadownight.qol.info;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;


public final class CMD_playtime implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player)sender;
        if(args.length == 0) {
            Chat.sendMessage(player, "Your playtime is §a" + Chat.sToDuration(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, true) + "§r.");
        }
        else {
            @SuppressWarnings("deprecation") // Using getOfflinePlayer the way it's meant to be used, no need to warn
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if(target.hasPlayedBefore()) {
                Chat.sendMessage(player, target.getName() + "'s playtime is §a" + Chat.sToDuration(target.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, true) + "§.");
            }
            else {
                Chat.sendMessage(player, "The player \"" + target.getName() + "\" doesn't exist or has never played on Shadow Night!");
            }
        }
        return true;
    }
}