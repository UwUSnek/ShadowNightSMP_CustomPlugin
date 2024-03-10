package org.uwu_snek.shadownight.adminCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;




public final class CMD_sngiveall implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        if(args.length > 0) {
            final Player player = (Player)sender;
            final String pattern = args[0];

            for(ItemManager manager : ItemManager.values()) {
                if(ChatUtils.stripColor(manager.getInstance().getDisplayName()).matches(pattern)) {
                    ItemStack item = manager.getInstance().createDefaultItemStack();
                    player.getInventory().setItem(player.getInventory().firstEmpty(), item);
                }
            }
            return true;
        }
        return false;
    }
}
