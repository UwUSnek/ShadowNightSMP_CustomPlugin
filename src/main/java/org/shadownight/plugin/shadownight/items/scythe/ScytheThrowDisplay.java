package org.shadownight.plugin.shadownight.items.scythe;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.items.CustomItemId;
import org.shadownight.plugin.shadownight.items.IM;
import org.shadownight.plugin.shadownight.items.ItemManager;
import org.shadownight.plugin.shadownight.utils.math.Func;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.function.Function;


public final class ScytheThrowDisplay {
    private final ItemDisplay display;
    private static final float throwDistance = 40;
    private static final int stepDuration = 2;

    private BukkitTask rotationTask;
    private final Player player;
    private final ItemStack item;


    //TODO make specific class for this type of objects
    /**
     * Creates a new scythe display.
     * @param _player The player that owns this object
     * @param _item The item stack to display
     */
    public ScytheThrowDisplay(@NotNull final Player _player, @NotNull final ItemStack _item) {
        player = _player;
        item = _item;
        final Location playerPos = player.getLocation();



        display = (ItemDisplay) player.getWorld().spawnEntity(new Location(
            playerPos.getWorld(),
            playerPos.getX(),
            playerPos.getY() + 1,
            playerPos.getZ(),
            0,
            90
        ), EntityType.ITEM_DISPLAY);
        IM data = ItemManager.getValueFromId(CustomItemId.KLAUE_SCYTHE);
        display.setItemStack(utils.createItemStackCustom(data.getMaterial(), 1, data.getDisplayName(), data.getCustomModelData(), data.getCustomId().getNumericalValue()));
        display.setTeleportDuration(stepDuration);



        //player.swingMainHand(); // For some reason, right-clicking air triggers left clicks as well
        animateRotation(2);

        final Vector startPos = player.getLocation().toVector().add(new Vector(0, 1, 0));
        animateTranslation(
            startPos.clone().add(playerPos.getDirection().multiply(throwDistance)),
            COMP_sineOut,
            () -> animateTranslationDynamic(
                startPos,
                COMP_sineIn,
                () -> player.getLocation().toVector().add(new Vector(0, 1, 0)),
                () -> {
                    rotationTask.cancel();
                    display.remove();
                }
            )
        );
    }




    /**
     * Animates a rotation loop on the entity.
     * @param rotations_s The number of rotations per second
     */
    public void animateRotation(final double rotations_s) {
        int third_duration = (int)Func.clampMin(20 / (rotations_s * 3), 1);      // This is 1 / (rotations_s / 20) / 3
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






    /**
     * Animates the translation of the entity to a static target location <target>.
     * @param target The target location where the animation ends
     * @param f The easing function to use
     * @param onComplete A function to run when the animation ends
     */
    public void animateTranslation(@NotNull final Vector target, @NotNull final Function<Double, Double> f, @Nullable final Runnable onComplete) {
        utils.damageItem(player, item);
        animateTranslationLoop(0, display.getLocation().toVector(), target, f, null, onComplete);
    }

    /**
     * Animates the translation of the entity to a target location which can be changed freely. The animation will adapt accordingly.
     * @param target The initial target location
     * @param f The easing function to use
     * @param onTargetUpdate The function to run before each step. Used to update the dynamic target location
     * @param onComplete A function to run when the animation ends
     */
    public void animateTranslationDynamic(
        @NotNull final Vector target,
        @NotNull final Function<Double, Double> f,
        @NotNull final Callable<Vector> onTargetUpdate,
        @Nullable final Runnable onComplete
    ) {
        utils.damageItem(player, item);
        animateTranslationLoop(0, display.getLocation().toVector(), target, f, onTargetUpdate, onComplete);
    }



    private void animateTranslationLoop(
        final double progress,
        @NotNull final Vector start,
        @NotNull Vector end,
        @NotNull final Function<Double, Double> f,
        @Nullable final Callable<Vector> onTargetUpdate,
        @Nullable final Runnable onComplete
    ){
        double stepSize = 0.1; //TODO replace this and stepDuration with a configurable steps/s



        // Update target if needed
        if(onTargetUpdate != null) try {
            end = onTargetUpdate.call();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        final Vector _final_end = end;


        // Teleport to new location
        Vector pos = progressToCoords(f.apply(progress), start, _final_end);
        display.teleport(new Location(player.getLocation().getWorld(), pos.getX(), pos.getY(), pos.getZ(), 0, 90));


        // Damage entities
        if(progress > 0.0f) {
            World world = display.getWorld();
            Vector oldPos = progressToCoords(f.apply(progress - stepSize), start, _final_end);
            Vector mid = pos.getMidpoint(oldPos);
            Vector boxSize_ = oldPos.clone().subtract(mid);
            Vector boxSize = new Vector(Math.abs(boxSize_.getX()), Math.abs(boxSize_.getY()), Math.abs(boxSize_.getZ())).divide(new Vector(2, 2, 2)).add(new Vector(2, 2, 2));
            Collection<Entity> entities = world.getNearbyEntities(mid.toLocation(world), Math.abs(boxSize.getX()), Math.abs(boxSize.getY()), Math.abs(boxSize.getZ()));
            for (Entity e : entities) {
                if (e instanceof LivingEntity && utils.distToLine(oldPos, pos, e.getLocation().toVector()) <= 2 && !e.getUniqueId().equals(player.getUniqueId())) {
                    IM_Scythe.attackQueue.put(player.getUniqueId(), e.getUniqueId());
                    ((LivingEntity) e).damage(10, player);
                }
            }
        }


        // Stop the animation if target has been reached
        if(progress + stepSize < 1) Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () -> animateTranslationLoop(progress + stepSize, start, _final_end, f, onTargetUpdate, onComplete), stepDuration);
        else if(onComplete != null) Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, onComplete, stepDuration);
    }


    private Vector progressToCoords(final double progress, @NotNull final Vector start, @NotNull final Vector end){
        return start.clone().add((end.clone().subtract(start)).multiply(progress));
    }







    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_linear = x -> x;



    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_sineIn    = x -> 1 - Math.cos((x * Math.PI) / 2);
    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_sineOut   = x ->     Math.sin((x * Math.PI) / 2);
    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_sineInOut = x ->   -(Math.cos( x * Math.PI) - 1) / 2;



    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_cubicIn    = x ->     Math.pow(    x, 3);
    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_cubicOut   = x -> 1 - Math.pow(1 - x, 3);
    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_cubicInOut = x -> x < 0.5 ? 4 * Math.pow(x, 3) : 1 - Math.pow(-2 * x + 2, 3) / 2;



    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_bounceIn    = x -> 1 - ScytheThrowDisplay.COMP_bounceOut.apply(1 - x);
    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_bounceOut   = x -> {
        final double n = 7.5625;
        final double d = 2.75;
        if      (x < 1   / d)                   return n * x * x;
        else if (x < 2   / d) { x -=   1.5 / d; return n * x * x + 0.75;     }
        else if (x < 2.5 / d) { x -=  2.25 / d; return n * x * x + 0.9375;   }
        else                  { x -= 2.625 / d; return n * x * x + 0.984375; }
    };
    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_bounceInOut = x -> x < 0.5 ? (1 - ScytheThrowDisplay.COMP_bounceOut.apply(1 - 2 * x)) / 2 : (1 + ScytheThrowDisplay.COMP_bounceOut.apply(2 * x - 1)) / 2;



    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_elasticIn    = x -> Func.doubleEquals(x, 0, 0.001) ? 0 : (Func.doubleEquals(x, 1, 0.001) ? 1 : -Math.pow(2,  10 * x - 10) * Math.sin((x * 10 - 10.75) * ((2 * Math.PI) / 3)));
    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_elasticOut   = x -> Func.doubleEquals(x, 0, 0.001) ? 0 : (Func.doubleEquals(x, 1, 0.001) ? 1 :  Math.pow(2, -10 * x     ) * Math.sin((x * 10 -  0.75) * ((2 * Math.PI) / 3)) + 1);
    @SuppressWarnings("unused") public static final Function<Double, Double> COMP_elasticInOut = x -> {
        final double c = Math.sin(20 * x - 11.125) * (2 * Math.PI) / 4.5;
        return Func.doubleEquals(x, 0, 0.001) ? 0 : (Func.doubleEquals(x, 1, 0.001) ? 1 : (
            x < 0.5 ?
            -(Math.pow(2,  20 * x - 10) * c) / 2 :
             (Math.pow(2, -20 * x + 10) * c) / 2 + 1
        ));
    };
}