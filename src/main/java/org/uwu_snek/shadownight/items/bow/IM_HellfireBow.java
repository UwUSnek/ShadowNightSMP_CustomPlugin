package org.uwu_snek.shadownight.items.bow;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.utils.blockdata.BlockProperty;
import org.uwu_snek.shadownight.utils.math.Easing;
import org.uwu_snek.shadownight.utils.math.Func;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public final class IM_HellfireBow extends IM_Bow {
    @Override public String       getDisplayName()     { return "§6Hellfire Bow";          }
    @Override public CustomItemId getCustomId()        { return CustomItemId.HELLFIRE_BOW; }



    @Override
    protected void setRecipe(final @NotNull ShapedRecipe recipe) {
        recipe.shape("II ", "I I", "II ");
        recipe.setIngredient('I', Material.COMMAND_BLOCK);
    }

    @Override protected void onInteract(@NotNull PlayerInteractEvent event) {}
    //@Override public void onAttack(@NotNull EntityDamageByEntityEvent event) {}

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
        initialPos.getWorld().playSound(initialPos, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 0.5f, 1);
    }




    private boolean isTargetValid(final @NotNull Location target) {
        return
            !BlockProperty.isWaterlogged(target.getBlock().getBlockData()) &&
             BlockProperty.isDelicate(target.getBlock().getType()) &&
            !BlockProperty.isDelicate(target.clone().subtract(0, 1, 0).getBlock().getType())
        ;
    }

    private void loop(final double from, final double to, final @NotNull Location startLocation, final int @NotNull [] shift, final boolean @NotNull [] broken, final double length, final int baseWidth, final @NotNull Vector dir, final @NotNull Vector side) {
        final World w = startLocation.getWorld();
        boolean soundPlayed = false;
        final int width = (int)Math.round(baseWidth * (1 - from));

        // For each stripe
        for(int k = -width; k <= width; ++k) {
            int stripeIndex = k + width; // Get stripe index
            if(broken[stripeIndex]) continue; // Skip stripe if broken

            // For each step
            for (double i = Easing.cubicOut(from) * length; i < Easing.cubicOut(to) * length; i += 0.25f) {
                // Get base target block
                final Location target = (
                    startLocation.clone().add(0, shift[stripeIndex], 0)
                    .add(dir.clone().multiply(i))
                    .add(side.clone().multiply(k))
                );

                // Skip step computation if fire is already present
                if(w.getBlockAt(target).getType() == Material.FIRE) continue;

                // Play sounds
                if(!soundPlayed) {
                    target.getWorld().playSound(target.getBlock().getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.5f, 1);
                    soundPlayed = true;
                }

                // Check if block is valid and shift Y level if needed
                if(!isTargetValid(target)) {
                    /**/ if(isTargetValid(target.add     (0, 1, 0))) shift[stripeIndex] += 1;
                    else if(isTargetValid(target.subtract(0, 2, 0))) shift[stripeIndex] -= 1;
                    else { broken[stripeIndex] = true; break;} // If no valid spot is found, stop the current stripe
                }


                // If it is, break the old block and place the fire
                Block block = target.getBlock();
                block.breakNaturally();
                block.setType(Material.FIRE);
            }
        }

        // If animation is not finished, schedule a new step
        if(to < 1) {
            Scheduler.delay(() ->
                loop(to, to + 0.05, startLocation, shift, broken, length, baseWidth, dir, side),
                1L
            );
        }
    }
}
