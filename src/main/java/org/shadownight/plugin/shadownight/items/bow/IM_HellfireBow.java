package org.shadownight.plugin.shadownight.items.bow;


import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.Vector;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.items.CustomItemId;
import org.shadownight.plugin.shadownight.utils.blockdata.BlockProperty;
import org.shadownight.plugin.shadownight.utils.math.Easing;
import org.shadownight.plugin.shadownight.utils.math.Func;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.Objects;
import java.util.logging.Level;




public final class IM_HellfireBow extends IM_Bow {
    @Override public Material getMaterial()            { return Material.BOW;              }
    @Override public String       getDisplayName()     { return "§6Hellfire Bow";          }
    @Override public int          getCustomModelData() { return 1;                         }
    @Override public CustomItemId getCustomId()        { return CustomItemId.HELLFIRE_BOW; }


    @Override
    protected void setRecipe(@NotNull final ShapedRecipe recipe) {
        recipe.shape("II ", "I I", "II ");
        recipe.setIngredient('I', Material.COMMAND_BLOCK);
    }

    @Override protected void onInteract(@NotNull PlayerInteractEvent event) {}
    @Override protected void onAttack(@NotNull EntityDamageByEntityEvent event) {}

    @Override
    protected void onShoot(@NotNull EntityShootBowEvent event) {
    }




    @Override
    protected void onProjectileHit(@NotNull final ProjectileHitEvent event, @NotNull final ItemStack usedBow) {
        // (3 is the maximum initial vanilla speed when shot by a player)
        final double maxSpeed = 3.0; // The maximum speed of the arrow. This limit prevents exploits using tnt knockback on custom bow arrows
        final double minSpeed = 0.5; // The minimum speed of the arrow. Doesn't actually speed it up but limits the affected hit area length

        // Get precise initial location
        final Location initialPos;
        /**/ if(event.getHitEntity() != null) initialPos = event.getHitEntity().getLocation().getBlock().getLocation();
        else if(event.getHitBlock()  != null) initialPos = event.getHitBlock().getLocation();
        else return;

        // Calculate velocity and direction
        final Arrow e = (Arrow)event.getEntity();
        final Vector dir = e.getVelocity().multiply(new Vector(1, 0, 1)).normalize();
        final Vector side = dir.clone().rotateAroundY(Math.PI / 2);
        final double width  = Func.clamp(e.getVelocity().length(), 1,        maxSpeed);     // Min 1, Max 3
        final double length = Func.clamp(e.getVelocity().length(), minSpeed, maxSpeed) * 10; // Min 4, Max 30

        // Start animation
        loop(0, 0.05, initialPos, length, width, dir, side);
        loop(0, 0.05, initialPos, length, width, dir.clone().rotateAroundY(Math.PI /  8), side.clone().rotateAroundY(Math.PI /  8));
        loop(0, 0.05, initialPos, length, width, dir.clone().rotateAroundY(Math.PI / -8), side.clone().rotateAroundY(Math.PI / -8));
    }




    private boolean isTargetValid(@NotNull final World w, @NotNull final Location target) {
        return
            BlockProperty.isDelicate(w.getBlockAt(target).getType()) &&
            !BlockProperty.isDelicate(w.getBlockAt(target.clone().subtract(0, 1, 0)).getType())
        ;
    }

    private void loop(final double from, final double to, @NotNull final Location startLocation, final double length, final double width, @NotNull final Vector dir, @NotNull final Vector side) {
        final World w = startLocation.getWorld();
        for(double k = -width; k < width; k += 0.5f) {
            for (double i = Easing.cubicOut(from) * length; i < Easing.cubicOut(to) * length; i += 0.25f) {
                final Location target = (
                    startLocation.clone()
                    .add(dir.clone().multiply(i))
                    .add(side.clone().multiply(k))
                );
                /**/ if(isTargetValid(w, target                  )) w.getBlockAt(target).setType(Material.FIRE);
                //else if(isTargetValid(w, target.add     (0, 1, 0))) { w.getBlockAt(target).setType(Material.FIRE);
                //    utils.log(Level.WARNING, "TARGET +1");}
                //else if(isTargetValid(w, target.subtract(0, 2, 0))) { w.getBlockAt(target).setType(Material.FIRE);
                //    utils.log(Level.WARNING, "TARGET -1");}
                else break; // If no valid spot is found, stop the current stripe
            }
        }
        if(to < 1) Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () ->
            loop(to, to + 0.05, startLocation, length, width, dir, side),
            1L
        );
    }
}
