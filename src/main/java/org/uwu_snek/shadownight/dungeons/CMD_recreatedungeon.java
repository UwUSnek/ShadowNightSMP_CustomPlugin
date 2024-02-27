package org.uwu_snek.shadownight.dungeons;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public final class CMD_recreatedungeon implements CommandExecutor {
    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        for(Dungeon dungeon : CMD_dungeontest.activeDungeons) {
            if(dungeon.world == ((Player) sender).getWorld()) {
                dungeon.generateDungeon();
                break;
            }
        }
        return true;
    }
}
