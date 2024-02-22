package org.shadownight.plugin.shadownight.items;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class CMD_sngive implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        long id;
        int amount;
        if(args.length > 0) {
            final Player player = (Player) sender;
            try {
                id = Long.parseLong(args[0]);
                amount = args.length > 1 ? Integer.parseInt(args[1]) : 1;

                ItemStack item = ItemManager.getValueFromId(id).createDefaultItemStack();
                item.setAmount(amount);
                player.getInventory().setItem(player.getInventory().firstEmpty(), item);
            }
            catch(NumberFormatException e) {
                player.sendMessage("§cInvalid ID or amount. Usage: /sngive <ID> <amount>");
            }
        }
        return true;
    }
}
