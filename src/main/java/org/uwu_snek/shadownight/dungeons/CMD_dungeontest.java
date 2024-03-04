package org.uwu_snek.shadownight.dungeons;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Vector;


public final class CMD_dungeontest implements CommandExecutor {
    public static final Vector<Dungeon> activeDungeons = new Vector<>();


    @Override
    public boolean onCommand(final @NotNull CommandSender sender, final @NotNull Command command, final @NotNull String label, final @NotNull String @NotNull [] args) {
        final Player player = (Player) sender;
        final Dungeon newDungeon = new Dungeon();

        activeDungeons.add(newDungeon);
        player.teleport(new org.bukkit.util.Vector(0, 10, 0).toLocation(newDungeon.world));
        player.setWalkSpeed(1f);

        return true;
    }
}
