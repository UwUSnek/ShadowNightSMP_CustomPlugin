package org.uwu_snek.shadownight.qol.info;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;


public final class CMD_vote implements CommandExecutor {
    private static void sendLink(Player player, String msg, String url) {
        player.sendMessage(Component.text("§r§l>§r " + msg).clickEvent(ClickEvent.openUrl(url)));
    }



    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String @NotNull [] args) {
        Player player = (Player)sender;
        String fake_dot = "․";

        ChatUtils.newline(player);
        ChatUtils.separator(player);
        player.sendMessage("§dClick to vote for the server!");
        sendLink(player, "GTop100" + fake_dot + "com",                 "https://gtop100.com/topsites/Minecraft-Servers/sitedetails/Shadow-Night-SMP-103158");
        sendLink(player, "MinecraftServers" + fake_dot + "org",        "https://minecraftservers.org/vote/658865");
        sendLink(player, "Minestatus" + fake_dot + "net",              "https://minestatus.net/server/shadownight.g-portal.game");
        sendLink(player, "PlanetMinecraft" + fake_dot + "com",         "https://www.planetminecraft.com/server/shadow-night-6171115/vote/");
        sendLink(player, "Minecraft-Server" + fake_dot + "eu",         "https://minecraft-server.eu/server/index/22E1D/Shadow-Night");
        sendLink(player, "Minecraft-Servers" + fake_dot + "de",        "https://minecraft-servers.de/server/shadow-night");
        sendLink(player, "Minecraft-Serverlist" + fake_dot + "net",    "https://www.minecraft-serverlist.net/server/58114");
        ChatUtils.separator(player);
        ChatUtils.newline(player);
        return true;
    }
}