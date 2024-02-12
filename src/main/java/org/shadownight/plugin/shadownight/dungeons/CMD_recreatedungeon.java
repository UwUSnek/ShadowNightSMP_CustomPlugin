package org.shadownight.plugin.shadownight.dungeons;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Vector;


public class CMD_recreatedungeon implements CommandExecutor {
    private static final Vector<Dungeon> activeDungeons = new Vector<>();


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        for(Dungeon dungeon : CMD_dungeontest.activeDungeons) {
            dungeon.generateDungeon();
        }
        return true;
    }
}
