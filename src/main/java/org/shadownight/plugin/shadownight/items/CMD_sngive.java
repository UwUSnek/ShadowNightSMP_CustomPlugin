package org.shadownight.plugin.shadownight.items;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CMD_sngive implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        long id;
        int amount;
        if(args.length > 0) {
            Player player = (Player) sender;
            try {
                id = Long.parseLong(args[0]);
                amount = args.length > 1 ? Integer.parseInt(args[1]) : 1;

                ItemStack item = ItemManager.getValueFromId(id).createStackCopy();
                item.setAmount(amount);
                player.getWorld().dropItem(player.getLocation(), item);
            }
            catch(NumberFormatException e) {
                player.sendMessage("§cInvalid ID or amount. Usage: /sngive <ID> <amount>");
            }
        }
        return true;
    }
}
