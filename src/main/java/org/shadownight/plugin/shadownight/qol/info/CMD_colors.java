package org.shadownight.plugin.shadownight.qol.info;


import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public final class CMD_colors implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, final String[] args) {
        final Player player = (Player) sender;
        Chat.sendMessage(
            player,
            """
                Formatting codes:
                    §00 §11 §22 §33 §44 §55 §66 §77 §88 §99 §aa §bb §cc §dd §ee §ff
                    §r§kk§r(k) §ll§r §mm§r §nn§r §oo§r r(reset)"""
            );
        return true;
    }
}