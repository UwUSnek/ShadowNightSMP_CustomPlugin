package org.uwu_snek.shadownight.qol;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.chatManager.ChatManager;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;




public class CMD_show implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        final Player player = (Player)sender;
        ItemStack item = player.getInventory().getItemInMainHand();
        if(item.getType() != Material.AIR) {
            ChatManager.sendPublicMessage(player, Component.text("§7 is holding: §f").append(ItemUtils.getFancyItemName(item)).append(Component.text("§7 [hover]")).hoverEvent(item));
        }
        return true;
    }
}
