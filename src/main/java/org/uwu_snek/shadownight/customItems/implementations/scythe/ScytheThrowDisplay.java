package org.uwu_snek.shadownight.customItems.implementations.scythe;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_LineArea;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.customItems.IM;
import org.uwu_snek.shadownight.customItems.ItemManager;
import org.uwu_snek.shadownight.utils.math.Easing;
import org.uwu_snek.shadownight.utils.math.Func;
import org.uwu_snek.shadownight.utils.math.K;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;

import java.util.concurrent.Callable;
import java.util.function.Function;


public final class ScytheThrowDisplay {
    private final ItemDisplay display;
    private static final float throwDistance = 40;
    private static final int stepDuration = 2;

    private BukkitTask rotationTask;
    private final Player player;
    private final ItemStack item;

    private final ATK_LineArea attack = new ATK_LineArea(0, 2, true);


    //TODO make specific class for this type of objects
    /**
     * Creates a new scythe display.
     * @param _player The player that owns this object
     * @param _item The item stack to display
     */
    public ScytheThrowDisplay(final @NotNull Player _player, final @NotNull ItemStack _item) {
        player = _player;
        item = _item;
        final Location playerPos = player.getLocation();


        display = (ItemDisplay)player.getWorld().spawnEntity(new Location(
            playerPos.getWorld(),
            playerPos.getX(),
            playerPos.getY() + 1,
            playerPos.getZ(),
            0,
            90
        ), EntityType.ITEM_DISPLAY);
        IM data = ItemManager.getValueFromId(_custom_item_id.KLAUE_SCYTHE);
        display.setItemStack(ItemUtils.createItemStackDisplay(data.getMaterial(), data.getCustomModelData()));
        display.setTeleportDuration((int)(stepDuration * 1.5));



        //player.swingMainHand(); // For some reason, right-clicking air triggers left clicks as well
        animateRotation(2);

        final Vector startPos = player.getLocation().toVector().add(new Vector(0, 1, 0));
        animateTranslation(
            startPos.clone().add(playerPos.getDirection().multiply(throwDistance)),
            Easing::sineOut,
            () -> animateTranslationDynamic(
                startPos,
                Easing::sineIn,
                () -> player.getLocation().toVector().add(new Vector(0, 1, 0)),
                () -> {
                    rotationTask.cancel();
                    display.remove();
                }
            )
        );


        ItemUtils.damageItem(player, item, 1);
    }




    /**
     * Animates a rotation loop on the entity.
     * @param rotations_s The number of rotations per second
     */
    public void animateRotation(final double rotations_s) {
        double step_parts = 1.99d;
        int step_duration = (int)Func.clampMin(20 / (rotations_s * (step_parts * 2)), 1);      // This is 1 / (rotations_s / 20) / 3
        display.setInterpolationDuration(step_duration);
        Transformation transformation = display.getTransformation();

        transformation.getScale().set(2, 2, 1);
        display.setTransformation(transformation);
        display.setInterpolationDelay(0);

        rotationTask = Scheduler.loop(() -> {
            Quaternionf diff = transformation.getLeftRotation();
            diff.rotateAxis((float)(-K.PI * 2 / (step_parts * 2)), 0, 0, 1);
            transformation.getLeftRotation().set(diff);
            display.setTransformation(transformation);
            display.setInterpolationDelay(0);
        }, 2L, step_duration);
    }






    /**
     * Animates the translation of the entity to a static target location <target>.
     * @param target The target location where the animation ends
     * @param f The easing function to use
     * @param onComplete A function to run when the animation ends
     */
    public void animateTranslation(final @NotNull Vector target, final @NotNull Function<Double, Double> f, @Nullable final Runnable onComplete) {
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
        final @NotNull Vector target,
        final @NotNull Function<Double, Double> f,
        final @NotNull Callable<Vector> onTargetUpdate,
        @Nullable final Runnable onComplete
    ) {
        animateTranslationLoop(0, display.getLocation().toVector(), target, f, onTargetUpdate, onComplete);
    }



    private void animateTranslationLoop(
        final double progress,
        final @NotNull Vector start,
        @NotNull Vector end,
        final @NotNull Function<Double, Double> f,
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
        Location oldPos = progressToCoords(f.apply(progress - stepSize), start, _final_end).toLocation(display.getWorld());
        if(progress > 0.0f) {
            attack.len = oldPos.toVector().distance(pos);
            Location origin = oldPos.clone().setDirection(pos.clone().subtract(oldPos.toVector()).normalize());
            attack.execute(player, null, origin, item);
        }


        // Stop the animation if target has been reached
        if(progress + stepSize < 1) Scheduler.delay(() -> animateTranslationLoop(progress + stepSize, start, _final_end, f, onTargetUpdate, onComplete), stepDuration);
        else if(onComplete != null) Scheduler.delay(onComplete, stepDuration);
    }


    private Vector progressToCoords(final double progress, final @NotNull Vector start, final @NotNull Vector end){
        return start.clone().add((end.clone().subtract(start)).multiply(progress));
    }
}