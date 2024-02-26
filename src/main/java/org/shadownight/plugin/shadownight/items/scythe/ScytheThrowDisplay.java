package org.shadownight.plugin.shadownight.items.scythe;

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
import org.shadownight.plugin.shadownight.attackOverride.AttackOverride;
import org.shadownight.plugin.shadownight.items.CustomItemId;
import org.shadownight.plugin.shadownight.items.IM;
import org.shadownight.plugin.shadownight.items.ItemManager;
import org.shadownight.plugin.shadownight.utils.math.Easing;
import org.shadownight.plugin.shadownight.utils.math.Func;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;
import org.shadownight.plugin.shadownight.utils.spigot.Scheduler;

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
    public ScytheThrowDisplay(final @NotNull Player _player, final @NotNull ItemStack _item) {
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
        display.setItemStack(ItemUtils.createItemStackCustom(data.getMaterial(), 1, data.getDisplayName(), data.getCustomModelData(), data.getCustomId().getNumericalValue()));
        display.setTeleportDuration(stepDuration);



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

        rotationTask = Scheduler.loop(() -> {
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
    public void animateTranslation(final @NotNull Vector target, final @NotNull Function<Double, Double> f, @Nullable final Runnable onComplete) {
        ItemUtils.damageItem(player, item);
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
        ItemUtils.damageItem(player, item);
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
        if(progress > 0.0f) {
            World world = display.getWorld();
            Vector oldPos = progressToCoords(f.apply(progress - stepSize), start, _final_end);
            Vector mid = pos.getMidpoint(oldPos);
            Vector boxSize_ = oldPos.clone().subtract(mid);
            Vector boxSize = new Vector(Math.abs(boxSize_.getX()), Math.abs(boxSize_.getY()), Math.abs(boxSize_.getZ())).divide(new Vector(2, 2, 2)).add(new Vector(2, 2, 2));
            Collection<Entity> entities = world.getNearbyEntities(mid.toLocation(world), Math.abs(boxSize.getX()), Math.abs(boxSize.getY()), Math.abs(boxSize.getZ()));
            for (Entity entity : entities) {
                if (entity instanceof LivingEntity e && Func.distToLine(oldPos, pos, e.getLocation().toVector()) <= 2 && !e.getUniqueId().equals(player.getUniqueId())) {
                    //IM_Scythe.attackQueue.put(player.getUniqueId(), e.getUniqueId());
                    //((LivingEntity) e).damage(10, player);
                    //AttackOverride.customAttack(player, e,  false);
                    //TODO use reach area
                }
            }
        }


        // Stop the animation if target has been reached
        if(progress + stepSize < 1) Scheduler.delay(() -> animateTranslationLoop(progress + stepSize, start, _final_end, f, onTargetUpdate, onComplete), stepDuration);
        else if(onComplete != null) Scheduler.delay(onComplete, stepDuration);
    }


    private Vector progressToCoords(final double progress, final @NotNull Vector start, final @NotNull Vector end){
        return start.clone().add((end.clone().subtract(start)).multiply(progress));
    }
}