package org.shadownight.plugin.shadownight.items.bow;


import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.items.CustomItemId;

import java.util.Objects;

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
        Bukkit.broadcastMessage("bow detected");
    }

    @Override
    protected void onProjectileHit(@NotNull final ProjectileHitEvent event, @NotNull final ItemStack usedBow) {
        Location pos = event.getEntity().getLocation();
        event.getEntity().remove();
        pos.getWorld().createExplosion(pos, 200);
    }
}
