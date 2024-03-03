package org.uwu_snek.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;



public final class IM_IronScythe extends IM_Scythe {
    @Override public String       getDisplayName()     { return "Iron Scythe";            }
    @Override public CustomItemId getCustomId()        { return CustomItemId.IRON_SCYTHE; }

    @Override protected double getAttackSpeed() { return -3.4; }
    @Override public    double getHitDamage()   { return 14;   }
    @Override public double getHitKnockbackMultiplier() { return 1.5d; }


    @Override
    protected void setRecipe(final @NotNull ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }
}
