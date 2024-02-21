package org.shadownight.plugin.shadownight.items.bow;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
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
import org.shadownight.plugin.shadownight.items.scythe.ScytheThrowDisplay;
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
        final double width = 2;      // The width of either side

        final Arrow e = (Arrow)event.getEntity();
        final World world = e.getWorld();
        final Vector dir = e.getVelocity().multiply(new Vector(1, 0, 1)).normalize();
        final Vector side = dir.clone().rotateAroundY(Math.PI / 2);

        final double length = Func.clamp(e.getVelocity().length(), minSpeed, maxSpeed) * 8;
        loop(0, 0.05, e, world, length, width, dir, side);
    }

    private void loop(final double from, final double to, @NotNull final Arrow e, @NotNull final World world, final double length, final double width, @NotNull final Vector dir, @NotNull final Vector side) {
        for (double i = ScytheThrowDisplay.COMP_cubicOut.apply(from) * length; i < ScytheThrowDisplay.COMP_cubicOut.apply(to) * length; i += 0.5f) {
            for(double j = -width; j < width; j += 0.5f) {
                world.getBlockAt(e.getLocation().add(dir.clone().multiply(i)).add(side.clone().multiply(j))).setType(Material.MAGMA_BLOCK);
            }
        }
        if(to < 1) Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () ->
            loop(to, to + 0.05, e, world, length, width, dir, side),
            1L
        );
    }
}
