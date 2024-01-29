package org.shadownightsmp.plugin.shadownightsmp.QOL.Info;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shadownightsmp.plugin.shadownightsmp.utils.utils;


public class CMD_playtime implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player)sender;
        if(args.length == 0) {
            utils.sendMessage(player, "Your playtime is §a" + utils.sToDuration(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, true) + "§r.");
        }
        else {
            @SuppressWarnings("deprecation") // Using getOfflinePlayer the way it's meant to be used, no need to warn
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if(target.hasPlayedBefore()) {
                utils.sendMessage(player, target.getName() + "'s playtime is §a" + utils.sToDuration(target.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, true) + "§.");
            }
            else {
                utils.sendMessage(player, "The player \"" + target.getName() + "\" doesn't exist or has never played on Shadow Night!");
            }
        }
        return true;
    }
}