package org.uwu_snek.shadownight.adminCommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.uwu_snek.shadownight.customMobs.implementations.MOB_Debug;
import org.uwu_snek.shadownight.customMobs.implementations.MOB_OvergrownSpider;




public final class CMD_testtarget implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        MOB_OvergrownSpider.testMob.target((Player)sender);
        return true;
    }
}
