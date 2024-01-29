package org.shadownightsmp.plugin.shadownightsmp.Economy;


import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.shadownightsmp.plugin.shadownightsmp.ShadowNightSMP;
import org.shadownightsmp.plugin.shadownightsmp.utils.utils;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;


public class CMD_trade implements CommandExecutor {
    private static class TradeRequest {
        final String target;
        final BukkitTask task;

        public TradeRequest(String _target, BukkitTask _task) {
            target = _target;
            task = _task;
        }
    }


    private static final HashMap<String, TradeRequest> tradeRequests = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (args.length == 0) return false;
        else {
            Player target = org.bukkit.Bukkit.getPlayer(args[0]);
            if (!utils.playerOnlineCheck(player, target, args[0])) return true;
            if (player.equals(target)) {
                utils.sendMessage(player, "§cYou cannot trade yourself!");
                return true;
            }


            if (tradeRequests.get(player.getName()) != null) {
                utils.sendMessage(player, "§cYou already have a pending trade request! Please wait for it to expire before opening a new one.");
            }
            else {
                TradeRequest requestFromTarget = tradeRequests.get(target.getName());
                if (requestFromTarget != null && requestFromTarget.target.equals(player.getName())) {
                    requestFromTarget.task.cancel();
                    tradeRequests.remove(target.getName());

                    //Create GUIs
                    TradeGui playerGui = new TradeGui(player, target);
                    TradeGui targetGui = new TradeGui(target, player);

                    // Open GUI for player
                    playerGui.TradeGuiInit(targetGui);
                    getServer().getPluginManager().registerEvents(playerGui, ShadowNightSMP.plugin);
                    playerGui.openInventory();

                    // Open GUI for target
                    targetGui.TradeGuiInit(playerGui);
                    getServer().getPluginManager().registerEvents(targetGui, ShadowNightSMP.plugin);
                    targetGui.openInventory();
                }
                else {
                    int timeout = 10;
                    tradeRequests.put(
                        player.getName(),
                        new TradeRequest(
                            target.getName(),
                            Bukkit.getScheduler().runTaskLater(ShadowNightSMP.plugin, () -> {
                                if(tradeRequests.get(player.getName()) != null) {
                                    tradeRequests.remove(player.getName());
                                    utils.sendMessage(player, "§7Your trade request to " + utils.getFancyName(target) + "§7 has expired.");
                                    utils.sendMessage(target, "§7The trade request from " + utils.getFancyName(player) + "§7 has expired.");
                                }
                            }, timeout * 20L)
                        )
                                     );
                    utils.sendMessage(player, "§7Sending a trade request to " + utils.getFancyName(target) + "...");

                    String _command = "/trade " + player.getName();
                    TextComponent c = new TextComponent("§a" + _command);
                    c.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, _command));
                    TextComponent c2 = new TextComponent("§a(or click here)");
                    c2.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, _command));
                    target.spigot().sendMessage(new TextComponent(utils.server_prefix + utils.getFancyName(player) + "§f sent you a trade request! Use "), c, new TextComponent("§r to accept "), c2);

                }
            }
        }
        return true;
    }
}