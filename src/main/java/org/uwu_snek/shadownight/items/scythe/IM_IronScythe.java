package org.uwu_snek.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;



public final class IM_IronScythe extends IM_Scythe {
    public IM_IronScythe(){
        super(
            "Iron Scythe",
            CustomItemId.IRON_SCYTHE,
            14,
            1.6
        );
    }


    @Override
    protected void setRecipe(final @NotNull ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }
}
