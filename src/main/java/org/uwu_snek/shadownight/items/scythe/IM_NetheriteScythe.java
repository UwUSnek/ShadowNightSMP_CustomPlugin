package org.uwu_snek.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;


public final class IM_NetheriteScythe extends IM_Scythe {
    @Override public String       getDisplayName()     { return "Netherite Scythe";            }
    @Override public CustomItemId getCustomId()        { return CustomItemId.NETHERITE_SCYTHE; }

    @Override protected double getAttackSpeed() { return -3.0; }
    @Override public    double getHitDamage()   { return 18;   }
    @Override public double getHitKnockbackMultiplier() { return 1.5d; }


    @Override
    protected void setRecipe(final @NotNull ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }
}
