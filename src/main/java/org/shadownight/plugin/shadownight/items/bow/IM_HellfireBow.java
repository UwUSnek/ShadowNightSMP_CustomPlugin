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
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.items.CustomItemId;
import org.shadownight.plugin.shadownight.utils.blockdata.BlockProperty;
import org.shadownight.plugin.shadownight.utils.math.Easing;
import org.shadownight.plugin.shadownight.utils.math.Func;

import java.util.Arrays;




public final class IM_HellfireBow extends IM_Bow {
    @Override public Material getMaterial()            { return Material.BOW;              }
    @Override public String       getDisplayName()     { return "§6Hellfire Bow";          }
    @Override public int          getCustomModelData() { return 1;                         }
    @Override public CustomItemId getCustomId()        { return CustomItemId.HELLFIRE_BOW; }


    @Override
    protected void setRecipe(final @NotNull ShapedRecipe recipe) {
        recipe.shape("II ", "I I", "II ");
        recipe.setIngredient('I', Material.COMMAND_BLOCK);
    }

    @Override protected void onInteract(@NotNull PlayerInteractEvent event) {}
    @Override protected void onAttack(@NotNull EntityDamageByEntityEvent event) {}

    @Override
    protected void onShoot(@NotNull EntityShootBowEvent event) {
    }




    @Override
    protected void onProjectileHit(final @NotNull ProjectileHitEvent event, final @NotNull ItemStack usedBow) {
        // (3 is the maximum initial vanilla speed when shot by a player)
        final double maxSpeed = 3.0; // The maximum speed of the arrow. This limit prevents exploits using tnt knockback on custom bow arrows
        final double minSpeed = 0.5; // The minimum speed of the arrow. Doesn't actually speed it up but limits the affected hit area length

        // Get precise initial location
        final Location initialPos;
        /**/ if(event.getHitEntity() != null) initialPos = event.getHitEntity().getLocation().getBlock().getLocation();
        else if(event.getHitBlock()  != null) initialPos = event.getHitBlock().getLocation().add(0, 1, 0);
        else return;

        // Calculate velocity and direction
        final Arrow e = (Arrow)event.getEntity();
        final Vector dir = e.getVelocity().multiply(new Vector(1, 0, 1)).normalize();
        final Vector side = dir.clone().rotateAroundY(Math.PI / 2);
        final int width = (int)Func.clamp(e.getVelocity().length(), 1,        maxSpeed) - 1;  // Min 0 (1 wide), Max 2 (2+1+2 wide)
        final double  length = Func.clamp(e.getVelocity().length(), minSpeed, maxSpeed) * 10; // Min 4, Max 30

        // Start animation
        final int maxWidth = 8; // 7 max width + 1 for safety
        loop(0, 0.05, initialPos, new int[maxWidth], new boolean[maxWidth], length, width, dir, side);
        loop(0, 0.05, initialPos, new int[maxWidth], new boolean[maxWidth], length, width, dir.clone().rotateAroundY(Math.PI /  8), side.clone().rotateAroundY(Math.PI /  8));
        loop(0, 0.05, initialPos, new int[maxWidth], new boolean[maxWidth], length, width, dir.clone().rotateAroundY(Math.PI / -8), side.clone().rotateAroundY(Math.PI / -8));
    }




    private boolean isTargetValid(final @NotNull World w, final @NotNull Location target) {
        return
             BlockProperty.isDelicate(w.getBlockAt(target                          ).getType()) &&
            !BlockProperty.isDelicate(w.getBlockAt(target.clone().subtract(0, 1, 0)).getType())
        ;
    }

    private void loop(final double from, final double to, final @NotNull Location startLocation, final int @NotNull [] shift, final boolean @NotNull [] broken, final double length, final int width, final @NotNull Vector dir, final @NotNull Vector side) {
        final World w = startLocation.getWorld();
        for(int k = -width; k <= width; ++k) {
            int stripeIndex = k + width; // Get stripe index
            if(broken[stripeIndex]) continue; // Skip stripe if broken
            for (double i = Easing.cubicOut(from) * length; i < Easing.cubicOut(to) * length; i += 0.25f) {
                final Location target = (
                    startLocation.clone().add(0, shift[stripeIndex], 0)
                    .add(dir.clone().multiply(i))
                    .add(side.clone().multiply(k))
                );
                if(w.getBlockAt(target).getType() == Material.FIRE) continue; // Skip step computation if fire is already present
                /**/ if(isTargetValid(w, target                  ))   w.getBlockAt(target).setType(Material.FIRE);
                else if(isTargetValid(w, target.add     (0, 1, 0))) { w.getBlockAt(target).setType(Material.FIRE); shift[stripeIndex] += 1; }
                else if(isTargetValid(w, target.subtract(0, 2, 0))) { w.getBlockAt(target).setType(Material.FIRE); shift[stripeIndex] -= 1; }
                else { broken[stripeIndex] = true; break;} // If no valid spot is found, stop the current stripe
            }
        }
        if(to < 1) {
            Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () ->
                loop(to, to + 0.05, startLocation, shift, broken, length, width, dir, side),
                1L
            );
        }
    }
}
