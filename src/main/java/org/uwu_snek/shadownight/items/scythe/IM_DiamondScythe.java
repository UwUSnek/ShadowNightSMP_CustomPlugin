package org.uwu_snek.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;


public final class IM_DiamondScythe extends IM_Scythe {
    public IM_DiamondScythe(){
        super(
            "Diamond Scythe",
            CustomItemId.DIAMOND_SCYTHE,
            16,
            1.25
        );
    }


    @Override
    protected void setRecipe(final @NotNull ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.DIAMOND);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }
}
