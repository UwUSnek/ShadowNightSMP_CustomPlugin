package org.shadownight.plugin.shadownight.qol.tpa;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.ChatUtils;
import org.shadownight.plugin.shadownight.utils.spigot.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Vector;


public final class CMD_tpaccept implements CommandExecutor {

    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if (args.length == 0) {
            return false;
        }

        final Player target = (Player) sender;
        final Player player = org.bukkit.Bukkit.getPlayer(args[0]);
        if (PlayerUtils.playerOfflineCheck(target, player, args[0])) return true;

        @SuppressWarnings("DataFlowIssue") // checked in playerOfflineCheck
        Vector<String> requests_from_player = CMD_tpa.tpa_requests.get(player.getName());
        if (requests_from_player != null && requests_from_player.removeIf(n -> (n.equals(target.getName())))) {
            if (requests_from_player.isEmpty()) CMD_tpa.tpa_requests.remove(player.getName());
            ChatUtils.sendMessage(player, "§aTeleporting you to " + PlayerUtils.getFancyName(target) + "...");
            ChatUtils.sendMessage(target, "§a" + PlayerUtils.getFancyName(player) + " teleported to you!");
            player.teleport(target);
            return true;
        }
        ChatUtils.sendMessage(target, "§c\"" + PlayerUtils.getFancyName(player) + "\" doesn't have pending teleport requests!");
        return true;
    }
}