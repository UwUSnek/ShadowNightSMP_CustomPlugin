package org.shadownight.plugin.shadownight.chatManager;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;



public final class CMD_msg implements CommandExecutor {
    public static final HashMap<String, String> openDms = new HashMap<>();


    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        processCommand((Player) sender, new Vector<>(Arrays.asList(args)));
        return true;
    }


    public static void processCommand(final @NotNull Player player, final @NotNull Vector<String> args) {
        if (args.isEmpty()) {
            openDms.remove(player.getName());
            Chat.sendMessage(player, "§aYou returned to the Public chat channel.");
        }
        else {
            final Player target = org.bukkit.Bukkit.getPlayer(args.get(0));
            if (utils.playerOfflineCheck(player, target, args.get(0))) return;
            if (player.equals(target)) {
                Chat.sendMessage(player, "§cYou cannot message yourself!");
                return;
            }

            if (args.size() == 1) {
                //noinspection DataFlowIssue (checked in playerOfflineCheck)
                openDms.put(player.getName(), target.getName());
                Chat.sendMessage(player, "§aYou opened a conversation with " + utils.getFancyName(target) + ". §rUse §a/msg§r to return to the Public chat channel.");
            }
            else {
                ChatManager.sendDm(player, target, String.join(" ", args.subList(1, args.size())));
            }
        }
    }
}