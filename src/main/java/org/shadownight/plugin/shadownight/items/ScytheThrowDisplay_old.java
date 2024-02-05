//package org.shadownight.plugin.shadownight.items;
//
//import org.bukkit.Bukkit;
//import org.bukkit.Location;
//import org.bukkit.entity.EntityType;
//import org.bukkit.entity.ItemDisplay;
//import org.bukkit.entity.Player;
//import org.bukkit.scheduler.BukkitTask;
//import org.bukkit.util.Transformation;
//import org.bukkit.util.Vector;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.joml.Quaternionf;
//import org.joml.Vector3d;
//import org.shadownight.plugin.shadownight.ShadowNight;
//
//import java.util.concurrent.Callable;
//
//
//public class ScytheThrowDisplay_old {
    //private ItemDisplay display;
    //private static final float throwDistance = 40;
    //private static final int stepDuration = 5;
//
    //private static final int loopDuration = 5;
//
    //BukkitTask rotationTask;
    //BukkitTask movementTask;
//
//
//
    //public ScytheThrowDisplay_old(Player player) {
        //Location playerPos = player.getLocation();
        //display = (ItemDisplay) player.getWorld().spawnEntity(new Location(
            //playerPos.getWorld(),
            //playerPos.getX(),
            //playerPos.getY() + 1,
            //playerPos.getZ(),
            //0,
            //90
        //), EntityType.ITEM_DISPLAY);
        //display.setItemStack(Scythe.klaueItem);
//
        //throwRotationLoop();
        ////throwMovement(player);
//
//
//
//
        //Vector direction = playerPos.getDirection();
        //throwMovement(
            //new Vector3d(
                //playerPos.getX() +     direction.getX() * throwDistance,
                //playerPos.getY() + 1 + direction.getY() * throwDistance,
                //playerPos.getZ() +     direction.getZ() * throwDistance
            //),
            //20,
            //1,
            //false,
            //null,
            //() -> throwMovement(
                //new Vector3d(
                    //playerPos.getX(),
                    //playerPos.getY() + 1,
                    //playerPos.getZ()
                //),
                //20,
                //1,
                //false,
                //() -> {
                    //Vector3d newTarget = player.getLocation().toVector().toVector3d();
                    //newTarget.y += 1;
                    //return newTarget;
                //},
                //null
            //)
        //);
    //}
//
//
//
//
    //public void throwRotationLoop() {
        //display.setInterpolationDuration(loopDuration);
        //display.setInterpolationDelay(0);
        //Transformation transformation = display.getTransformation();
//
        //transformation.getScale().set(2, 2, 1);
        //display.setTransformation(transformation);
//
        //rotationTask = Bukkit.getScheduler().runTaskTimer(ShadowNight.plugin, () -> {
//
            //Quaternionf diff = transformation.getLeftRotation();
            //diff.rotateAxis((float) -Math.PI / 3 * 2, 0, 0, 1);
            //transformation.getLeftRotation().set(diff);
            //display.setTransformation(transformation);
            //display.setInterpolationDelay(0);
        //}, 2L, loopDuration);
    //}
//
//
//
//
//    /**
      //Makes the entity move to the position <target> smoothly
      //@param _target The target location. This vector can be changed freely, the movement will adapt accordingly
      //@param max The largest possible step
      //@param min The smallest possible step
      //@param invert Should the easing be inverted?
      //@param onUpdate A function to run after each step. Can be used to update target
      //@param onTargetReached A function to run when the entity has reached the target location
     //*/
    //public void throwMovement(@NotNull Vector3d _target, double max, double min, boolean invert, @Nullable Callable<Vector3d> onUpdate, @Nullable Runnable onTargetReached) {
        //display.setTeleportDuration(stepDuration);
        //movementTask = Bukkit.getScheduler().runTaskTimer(ShadowNight.plugin, () -> {
            //final Location currentLocation = display.getLocation();
            //Vector3d pos = currentLocation.toVector().toVector3d();
//
            //Vector3d target = _target;
            //Bukkit.broadcastMessage("" + target.distance(pos));
            //if(target.distance(pos) < 5) {
                //movementTask.cancel();
                //if(onTargetReached != null) Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, onTargetReached, stepDuration);
            //}
            //else {
                //if(onUpdate != null) try {
                    //target = onUpdate.call();
                //}
                //catch (Exception e) {
                    //e.printStackTrace();
                //}
//
                //if(invert) pos.add(new Vector3d());
                //else {
                    //Vector3d diff = new Vector3d(target).sub(pos);
                    //pos.add(new Vector3d(Math.sqrt(diff.x), Math.sqrt(diff.y), Math.sqrt(diff.z)));
                //}
                //display.teleport(new Location(
                    //currentLocation.getWorld(),
                    //pos.x,
                    //pos.y,
                    //pos.z,
                    //currentLocation.getYaw(),
                    //currentLocation.getPitch()
                //));
            //}
        //}, 2L, stepDuration);
    //}
//}
//
////display.setTeleportDuration(duration);
////Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () -> {
////    Location playerPos = player.getLocation();
////    Vector direction = playerPos.getDirection();
////    display.teleport(new Location(
////        playerPos.getWorld(),
////        playerPos.getX() +     direction.getX() * n * easing,
////        playerPos.getY() + 1 + direction.getY() * n * easing,
////        playerPos.getZ() +     direction.getZ() * n * easing,
////        0, 90));
////
////    Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () -> {
////        display.teleport(new Location(
////            playerPos.getWorld(),
////            playerPos.getX() +     direction.getX() * n,
////            playerPos.getY() + 1 + direction.getY() * n ,
////            playerPos.getZ() +     direction.getZ() * n,
////            0, 90));
////
////        Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () -> {
////            display.teleport(new Location(
////                playerPos.getWorld(),
////                playerPos.getX() +     direction.getX() * n * easing,
////                playerPos.getY() + 1 + direction.getY() * n * easing,
////                playerPos.getZ() +     direction.getZ() * n * easing,
////                0, 90));
////            Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () -> {
////                throwHoming(player);
////            }, duration);
////        }, duration);
////    }, (long) (duration));
////}, 2L);
//