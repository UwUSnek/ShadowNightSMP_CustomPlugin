package org.shadownightsmp.plugin.shadownightsmp.QOL.Info;


import org.jetbrains.annotations.NotNull;
import org.shadownightsmp.plugin.shadownightsmp.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CMD_discord implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player)sender;
        utils.sendMessage(player, "§9§lDiscord§r: discord.com/invite/VZtg2jYDZ5");
        return true;
    }
}