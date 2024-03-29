package org.uwu_snek.shadownight.economy;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.uwu_snek.shadownight.utils.spigot.PlayerUtils;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;


public final class CMD_trade implements CommandExecutor {
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
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        final Player player = (Player) sender;
        if (args.length == 0) return false;
        else {
            Player target = org.bukkit.Bukkit.getPlayer(args[0]);
            if (PlayerUtils.playerOfflineCheck(player, target, args[0])) return true;
            if (player.equals(target)) {
                ChatUtils.sendMessage(player, "§cYou cannot trade yourself!");
                return true;
            }


            if (tradeRequests.get(player.getName()) != null) {
                ChatUtils.sendMessage(player, "§cYou already have a pending trade request! Please wait for it to expire before opening a new one.");
            }
            else {
                @SuppressWarnings("DataFlowIssue")  //checked in playerOfflineCheck
                TradeRequest requestFromTarget = tradeRequests.get(target.getName());
                if (requestFromTarget != null && requestFromTarget.target.equals(player.getName())) {
                    requestFromTarget.task.cancel();
                    tradeRequests.remove(target.getName());

                    //Create GUIs
                    final TradeGui playerGui = new TradeGui(player, target);
                    final TradeGui targetGui = new TradeGui(target, player);

                    // Open GUI for player
                    playerGui.TradeGuiInit(targetGui);
                    getServer().getPluginManager().registerEvents(playerGui, ShadowNight.plugin);
                    playerGui.openInventory();

                    // Open GUI for target
                    targetGui.TradeGuiInit(playerGui);
                    getServer().getPluginManager().registerEvents(targetGui, ShadowNight.plugin);
                    targetGui.openInventory();
                }
                else {
                    int timeout = 10;
                    tradeRequests.put(
                        player.getName(),
                        new TradeRequest(
                            target.getName(),
                            Scheduler.delay(() -> {
                                if(tradeRequests.get(player.getName()) != null) {
                                    tradeRequests.remove(player.getName());
                                    ChatUtils.sendMessage(player, "§7Your trade request to " + PlayerUtils.getFancyName(target) + "§7 has expired.");
                                    ChatUtils.sendMessage(target, "§7The trade request from " + PlayerUtils.getFancyName(player) + "§7 has expired.");
                                }
                            }, timeout * 20L)
                        )
                    );
                    ChatUtils.sendMessage(player, "§7Sending a trade request to " + PlayerUtils.getFancyName(target) + "§f...");

                    String _command = "/trade " + player.getName();
                    target.sendMessage(
                        Component.text(ChatUtils.serverPrefix + PlayerUtils.getFancyName(player) + "§f sent you a trade request! Use ").append(
                            Component.text("§a" + _command).clickEvent(ClickEvent.runCommand(_command))).append(
                            Component.text("§r to accept ")).append(
                            Component.text("§a(or click here)").clickEvent(ClickEvent.runCommand(_command)))
                    );
                }
            }
        }
        return true;
    }
}