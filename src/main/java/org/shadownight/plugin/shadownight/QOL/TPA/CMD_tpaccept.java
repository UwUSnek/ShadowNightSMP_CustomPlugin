package org.shadownight.plugin.shadownight.QOL.TPA;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Vector;


public class CMD_tpaccept implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            return false;
        }

        Player target = (Player) sender;
        Player player = org.bukkit.Bukkit.getPlayer(args[0]);
        if (utils.playerOfflineCheck(target, player, args[0])) return true;

        Vector<String> requests_from_player = CMD_tpa.tpa_requests.get(player.getName());
        if (requests_from_player != null && requests_from_player.removeIf(n -> (n.equals(target.getName())))) {
            if (requests_from_player.isEmpty()) CMD_tpa.tpa_requests.remove(player.getName());
            utils.sendMessage(player, "§aTeleporting you to " + utils.getFancyName(target) + "...");
            utils.sendMessage(target, "§a" + utils.getFancyName(player) + " teleported to you!");
            player.teleport(target);
            return true;
        }
        utils.sendMessage(target, "§c\"" + utils.getFancyName(player) + "\" doesn't have pending teleport requests!");
        return true;
    }
}