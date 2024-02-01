package org.shadownight.plugin.shadownight.QOL.Info;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.utils;


public class CMD_ping implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player)sender;
        utils.sendMessage(player, "Ping: §a" + player.getPing() + "ms");
        return true;
    }
}