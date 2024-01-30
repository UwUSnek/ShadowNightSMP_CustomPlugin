package org.shadownightsmp.plugin.shadownightsmp.ChatManager;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownightsmp.plugin.shadownightsmp.utils.utils;

import java.util.HashMap;
import java.util.Vector;
import java.util.Arrays;


public class CMD_r implements CommandExecutor {
    public static final HashMap<String, String> lastDmFrom = new HashMap<>();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player) sender;
        String targetName = lastDmFrom.get(player.getName());
        if (targetName == null) utils.sendMessage(player, "§cNobody has messaged you recently!");
        else {
            Player target = Bukkit.getPlayer(targetName);
            if (target == null) utils.sendMessage(player, "§cCannot reply! That player is not online at the moment.");
            else {
                Vector<String> _args = new Vector<>(Arrays.asList(args));
                _args.add(0, targetName);
                CMD_msg.processCommand(player, _args);
            }
        }
        return true;
    }
}