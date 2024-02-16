package org.shadownight.plugin.shadownight.qol.tpa;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.shadownight.plugin.shadownight.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Vector;


public final class CMD_tpaccept implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, final String[] args) {
        if (args.length == 0) {
            return false;
        }

        final Player target = (Player) sender;
        final Player player = org.bukkit.Bukkit.getPlayer(args[0]);
        if (utils.playerOfflineCheck(target, player, args[0])) return true;

        @SuppressWarnings("DataFlowIssue") // checked in playerOfflineCheck
        Vector<String> requests_from_player = CMD_tpa.tpa_requests.get(player.getName());
        if (requests_from_player != null && requests_from_player.removeIf(n -> (n.equals(target.getName())))) {
            if (requests_from_player.isEmpty()) CMD_tpa.tpa_requests.remove(player.getName());
            Chat.sendMessage(player, "§aTeleporting you to " + utils.getFancyName(target) + "...");
            Chat.sendMessage(target, "§a" + utils.getFancyName(player) + " teleported to you!");
            player.teleport(target);
            return true;
        }
        Chat.sendMessage(target, "§c\"" + utils.getFancyName(player) + "\" doesn't have pending teleport requests!");
        return true;
    }
}