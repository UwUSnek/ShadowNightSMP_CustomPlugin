package org.shadownight.plugin.shadownight.qol.info;


import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;


public final class CMD_vote implements CommandExecutor {
    private static void sendLink(Player player, String msg, String url) {
        @SuppressWarnings("deprecation")
        TextComponent component = new TextComponent(TextComponent.fromLegacyText("§r§l>§r " + msg));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        player.spigot().sendMessage(component);
    }



    @SuppressWarnings("SpellCheckingInspection")
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        Player player = (Player)sender;
        String fake_dot = "․";

        Chat.newline(player);
        Chat.separator(player);
        player.sendMessage("§dClick to vote for the server!");
        sendLink(player, "GTop100" + fake_dot + "com",                 "https://gtop100.com/topsites/Minecraft-Servers/sitedetails/Shadow-Night-SMP-103158");
        sendLink(player, "MinecraftServers" + fake_dot + "org",        "https://minecraftservers.org/vote/658865");
        sendLink(player, "Minestatus" + fake_dot + "net",              "https://minestatus.net/server/shadownight.g-portal.game");
        sendLink(player, "PlanetMinecraft" + fake_dot + "com",         "https://www.planetminecraft.com/server/shadow-night-6171115/vote/");
        sendLink(player, "Minecraft-Server" + fake_dot + "eu",         "https://minecraft-server.eu/server/index/22E1D/Shadow-Night");
        sendLink(player, "Minecraft-Servers" + fake_dot + "de",        "https://minecraft-servers.de/server/shadow-night");
        sendLink(player, "Minecraft-Serverlist" + fake_dot + "net",    "https://www.minecraft-serverlist.net/server/58114");
        Chat.separator(player);
        Chat.newline(player);
        return true;
    }
}