package org.shadownight.plugin.shadownight.qol.info;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;


public final class CMD_ping implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, final String[] args) {
        final Player player = (Player)sender;
        Chat.sendMessage(player, "Ping: §a" + player.getPing() + "ms");
        return true;
    }
}