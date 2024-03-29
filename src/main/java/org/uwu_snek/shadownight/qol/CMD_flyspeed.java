package org.uwu_snek.shadownight.qol;


import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CMD_flyspeed implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if(args.length == 0) return false;

        float spd;
        try {
            spd = Float.parseFloat(args[0]);
        } catch(NumberFormatException e) {
            return false;
        }

        Player player = (Player)sender;
        if(spd < 0 || spd > 5) {
            ChatUtils.sendMessage(player, "§cFlying speed must be between 0 and 5");
            return true;
        }
        float def = 10.92f;

        player.setFlySpeed(spd / 10);
        ChatUtils.sendMessage(player, "§aFlying speed set to " + spd + " (" + spd * def + "m/s)");
        return true;
    }
}