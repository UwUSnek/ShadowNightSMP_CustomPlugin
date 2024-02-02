package org.shadownight.plugin.shadownight.qol.tpa;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.utils;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.chat.TextComponent;

import java.util.HashMap;
import java.util.Vector;


public class CMD_tpa implements CommandExecutor {
    public static final HashMap<String, Vector<String>> tpa_requests = new HashMap<>();


    public static void removeTargetFromAllRequesters(String targetName){
        for(Vector<String> targets : tpa_requests.values()) {
            targets.removeIf(s -> (s.equals(targetName)));
        }
    }



    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length == 0) {
            return false;
        }

        Player player = (Player)sender;
        Player target = org.bukkit.Bukkit.getPlayer(args[0]);

        if(utils.playerOfflineCheck(player, target, args[0])) return true;
        if(player.equals(target)) {
            utils.sendMessage(player, "§cYou cannot teleport to yourself!");
            return true;
        }

        Vector<String> requests_from_player = tpa_requests.get(player.getName());
        //noinspection DataFlowIssue (checked in playerOfflineCheck)
        if(requests_from_player != null && requests_from_player.contains(target.getName())){
            utils.sendMessage(player, "§cYou already sent a request to " + utils.getFancyName(target) + "§c! Please wait for them to accept");
            return true;
        }

        utils.sendMessage(player, "§7Sending a teleport request to " + utils.getFancyName(target) + "...");

        String _command = "/tpaccept " + player.getName();
        TextComponent c = new TextComponent("§a" + _command);
        c.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, _command));
        TextComponent c2 = new TextComponent("§a(or click here)");
        c2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, _command));

        //noinspection DataFlowIssue (checked in playerOfflineCheck)
        target.spigot().sendMessage(new TextComponent(utils.serverPrefix + utils.getFancyName(player) + "§f is asking you to teleport to your location! Use "), c, new TextComponent("§r to accept "), c2);
        if(requests_from_player == null) tpa_requests.put(player.getName(), new Vector<>());
        tpa_requests.get(player.getName()).add(target.getName());
        return true;
    }
}