package org.uwu_snek.shadownight.adminCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.uwu_snek.shadownight.customMobs.implementations.MOB_Debug;
import org.uwu_snek.shadownight.customMobs.implementations.MOB_OvergrownSpider;




public final class CMD_testrotation implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        MOB_Debug.testMob.bones_test.get(args[0]).rotate(new AxisAngle4f(Float.parseFloat(args[4]), Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3])));
        //MOB_OvergrownSpider.testMob.bones_test.get(args[0]).rotate(new AxisAngle4f(Float.parseFloat(args[4]), Float.parseFloat(args[1]), Float.parseFloat(args[2]), Float.parseFloat(args[3])));
        return true;
    }
}
