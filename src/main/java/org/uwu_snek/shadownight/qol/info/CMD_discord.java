package org.uwu_snek.shadownight.qol.info;


import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public final class CMD_discord implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        final Player player = (Player)sender;
        ChatUtils.sendMessage(player, "§9§lDiscord§r: discord.com/invite/VZtg2jYDZ5");
        return true;
    }
}