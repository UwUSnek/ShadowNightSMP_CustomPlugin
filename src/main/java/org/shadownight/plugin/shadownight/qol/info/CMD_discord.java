package org.shadownight.plugin.shadownight.qol.info;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public final class CMD_discord implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, final String[] args) {
        final Player player = (Player)sender;
        Chat.sendMessage(player, "§9§lDiscord§r: discord.com/invite/VZtg2jYDZ5");
        return true;
    }
}