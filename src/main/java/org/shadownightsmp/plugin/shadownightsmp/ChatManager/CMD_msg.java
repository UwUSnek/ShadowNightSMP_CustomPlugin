package org.shadownightsmp.plugin.shadownightsmp.ChatManager;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.shadownightsmp.plugin.shadownightsmp.utils.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;



public class CMD_msg implements CommandExecutor {
    public static final HashMap<String, String> openDms = new HashMap<>();


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        processCommand((Player) sender, new Vector<>(Arrays.asList(args)));
        return true;
    }


    public static void processCommand(Player player, Vector<String> args) {
        if (args.isEmpty()) {
            openDms.remove(player.getName());
            utils.sendMessage(player, "§aYou returned to the Public chat channel.");
        }
        else {
            Player target = org.bukkit.Bukkit.getPlayer(args.get(0));
            if (!utils.playerOnlineCheck(player, target, args.get(0))) return;
            if (player.equals(target)) {
                utils.sendMessage(player, "§cYou cannot message yourself!");
                return;
            }

            if (args.size() == 1) {
                openDms.put(player.getName(), target.getName());
                utils.sendMessage(player, "§aYou opened a conversation with " + utils.getFancyName(target) + ". §rUse §a/msg§r to return to the Public chat channel.");
            }
            else {
                ChatManager.sendDm(player, target, String.join(" ", args.subList(1, args.size())));
            }
        }
    }
}