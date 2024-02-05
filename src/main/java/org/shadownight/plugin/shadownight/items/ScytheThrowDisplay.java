package org.shadownight.plugin.shadownight.items;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import org.shadownight.plugin.shadownight.ShadowNight;


public class ScytheThrowDisplay {
    private ItemDisplay display;
    private static final float throwDistance = 40;
    private static final int stepDuration = 5;


    BukkitTask rotationTask;
    Player player;



    public ScytheThrowDisplay(Player _player) {
        this.player = _player;
        Location playerPos = player.getLocation();
        display = (ItemDisplay) player.getWorld().spawnEntity(new Location(
            playerPos.getWorld(),
            playerPos.getX(),
            playerPos.getY() + 1,
            playerPos.getZ(),
            0,
            90
        ), EntityType.ITEM_DISPLAY);
        display.setItemStack(Scythe.klaueItem);

        rotationLoop(2);



        Vector direction = playerPos.getDirection();
        easeSineIn(new Vector3d(
            playerPos.getX() +     direction.getX() * throwDistance,
            playerPos.getY() + 1 + direction.getY() * throwDistance,
            playerPos.getZ() +     direction.getZ() * throwDistance
        ));
    }




    public void rotationLoop(double rotations_s) {
        int third_duration = (int) Math.max(1, 20 / (rotations_s * 3)); //   1 / (rotations_s / 20) / 3
        display.setInterpolationDuration(third_duration);
        display.setInterpolationDelay(0);
        Transformation transformation = display.getTransformation();

        transformation.getScale().set(2, 2, 1);
        display.setTransformation(transformation);

        rotationTask = Bukkit.getScheduler().runTaskTimer(ShadowNight.plugin, () -> {

            Quaternionf diff = transformation.getLeftRotation();
            diff.rotateAxis((float) -Math.PI / 3 * 2, 0, 0, 1);
            transformation.getLeftRotation().set(diff);
            display.setTransformation(transformation);
            display.setInterpolationDelay(0);
        }, 2L, third_duration);
    }




    public void easeSineIn(@NotNull Vector3d _target) {
        display.setTeleportDuration(stepDuration);
        loop_easeSineIn(0, player.getLocation().toVector().toVector3d(), _target);
    }

    private void loop_easeSineIn(double progress, Vector3d start, Vector3d end){
        Vector3d pos = ENCODE_easeSineIn(progress, start, end);
        display.teleport(new Location(player.getLocation().getWorld(), pos.x, pos.y, pos.z, 0, 90));
        if(progress < 1) Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () -> loop_easeSineIn(progress + 0.1, start, end), stepDuration);
    }

    private Vector3d ENCODE_easeSineIn(double progress, Vector3d start, Vector3d end){
        return new Vector3d(start).add((new Vector3d(end).sub(start)).mul(progress));
    }
}