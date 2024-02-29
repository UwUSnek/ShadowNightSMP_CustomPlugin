package org.uwu_snek.shadownight.qol.tpa;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.uwu_snek.shadownight.utils.spigot.PlayerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Vector;


public final class CMD_tpa implements CommandExecutor {
    public static final HashMap<String, Vector<String>> tpa_requests = new HashMap<>();


    public static void removeTargetFromAllRequesters(@NotNull final String targetName){
        for(Vector<String> targets : tpa_requests.values()) {
            targets.removeIf(s -> (s.equals(targetName)));
        }
    }



    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String @NotNull [] args) {
        if(args.length == 0) {
            return false;
        }

        final Player player = (Player)sender;
        final Player target = org.bukkit.Bukkit.getPlayer(args[0]);

        if(PlayerUtils.playerOfflineCheck(player, target, args[0])) return true;
        if(player.equals(target)) {
            ChatUtils.sendMessage(player, "§cYou cannot teleport to yourself!");
            return true;
        }

        Vector<String> requests_from_player = tpa_requests.get(player.getName());
        //noinspection DataFlowIssue (checked in playerOfflineCheck)
        if(requests_from_player != null && requests_from_player.contains(target.getName())){
            ChatUtils.sendMessage(player, "§cYou already sent a request to " + PlayerUtils.getFancyName(target) + "§c! Please wait for them to accept");
            return true;
        }

        ChatUtils.sendMessage(player, "§7Sending a teleport request to " + PlayerUtils.getFancyName(target) + "...");

        String _command = "/tpaccept " + player.getName();
        target.sendMessage(
            Component.text(ChatUtils.serverPrefix + PlayerUtils.getFancyName(player) + "§f is asking you to teleport to your location! Use ").append(
            Component.text("§a" + _command).clickEvent(ClickEvent.runCommand(_command))).append(
            Component.text("§r to accept ")).append(
            Component.text("§a(or click here)").clickEvent(ClickEvent.runCommand(_command)))
        );
        if(requests_from_player == null) tpa_requests.put(player.getName(), new Vector<>());
        tpa_requests.get(player.getName()).add(target.getName());
        return true;
    }
}