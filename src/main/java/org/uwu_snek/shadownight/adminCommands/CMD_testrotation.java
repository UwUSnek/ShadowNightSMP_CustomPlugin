package org.uwu_snek.shadownight.adminCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.uwu_snek.shadownight.customItems.ItemManager;
import org.uwu_snek.shadownight.customMobs.CustomMob;
import org.uwu_snek.shadownight.customMobs.implementations.TestMob;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;




public final class CMD_testrotation implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        TestMob.testMob.bones_test.get(args[0]).rotate(20, new AxisAngle4f(Float.parseFloat(args[4]), Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3])));
        return true;
    }
}
