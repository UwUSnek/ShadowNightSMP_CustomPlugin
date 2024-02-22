package org.shadownight.plugin.shadownight.chatManager;


import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.ChatUtils;

import java.util.HashMap;
import java.util.Vector;
import java.util.Arrays;


public final class CMD_r implements CommandExecutor {
    public static final HashMap<String, String> lastDmFrom = new HashMap<>();


    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        final Player player = (Player) sender;
        final String targetName = lastDmFrom.get(player.getName());
        if (targetName == null) ChatUtils.sendMessage(player, "§cNobody has messaged you recently!");
        else {
            Player target = Bukkit.getPlayer(targetName);
            if (target == null) ChatUtils.sendMessage(player, "§cCannot reply! That player is not online at the moment.");
            else {
                Vector<String> _args = new Vector<>(Arrays.asList(args));
                _args.add(0, targetName);
                CMD_msg.processCommand(player, _args);
            }
        }
        return true;
    }
}