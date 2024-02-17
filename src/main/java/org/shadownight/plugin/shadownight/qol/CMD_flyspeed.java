package org.shadownight.plugin.shadownight.qol;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CMD_flyspeed implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, final String[] args) {
        if(args.length == 0) return false;

        float spd;
        try {
            spd = Float.parseFloat(args[0]);
        } catch(NumberFormatException e) {
            return false;
        }

        Player player = (Player)sender;
        if(spd < 0 || spd > 5) {
            Chat.sendMessage(player, "§cFlying speed must be between 0 and 5");
            return true;
        }
        float def = 10.92f;

        player.setFlySpeed(spd / 10);
        Chat.sendMessage(player, "§aFlying speed set to " + spd + " (" + spd * def + "m/s)");
        return true;
    }
}