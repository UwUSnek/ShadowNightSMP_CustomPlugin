package org.uwu_snek.shadownight.qol.info;


import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public final class CMD_help implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        final Player player = (Player)sender;
        ChatUtils.newline(player);
        ChatUtils.sendMessage(player, "Useful commands:\n");
        ChatUtils.newline(player);
        player.sendMessage(
            """
                §d§lInfo:§r
                §d  §a/help §7| View this message
                §d  §a/vote §7| Vote for the server
                §d  §a/playtime §7| Show a player's total playtime
                §d  §a/ping §7| Show your ping
                §d  §a/colors §7| List Color and Formatting codes
                §d  §a/discord §7| Join the Shadow Night Community Discord server
            """
        );
        ChatUtils.newline(player);
        player.sendMessage(
            """
                §d§lMovement:§r
                §d  §a/home §7| Warp to your home
                §d  §7/sethome | Create a new home
                §d  §7/delhome | Delete an existing home
                §d  §a/rtp §7| Teleport to a random location in the Survival world
                §d  §a/tpa §7| Teleport to another player
                §d  §7/tpaccept | Accept another player's teleport request
                §d  §a/warp §7| Warp to a predefined location
                §d  §7/warps | List available warp locations
            """
        );
        ChatUtils.newline(player);
        player.sendMessage(
            """
                §d§lMenus:§r
                §d  §a/backpack §7| Open your backpack
                §d  §a/skills §7| Open the Skills Menu
                §d  §a/trade §7| Trade with another player
            """
        );
        ChatUtils.newline(player);
        player.sendMessage(
            """
                §d§lChat:§r
                §d  §a/msg §7| Message a specific player or return to Public chat
                §d  §a/r §7| Message the last player that sent you a message
            """
        );
        ChatUtils.newline(player);
        return true;
    }
}