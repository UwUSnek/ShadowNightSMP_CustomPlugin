package org.shadownight.plugin.shadownight.dungeons;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Vector;


public final class CMD_dungeontest implements CommandExecutor {
    public static final Vector<Dungeon> activeDungeons = new Vector<>();


    @Override
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, final String[] args) {
        final Player player = (Player) sender;
        final Dungeon newDungeon = new Dungeon();

        activeDungeons.add(newDungeon);
        player.teleport(new org.bukkit.util.Vector(0, 10, 0).toLocation(newDungeon.world));
        player.setWalkSpeed(1f); //TODO Reset on leave

        return true;
    }
}
