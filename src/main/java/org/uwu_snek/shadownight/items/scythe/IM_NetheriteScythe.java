package org.uwu_snek.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;


public final class IM_NetheriteScythe extends IM_Scythe {
    public IM_NetheriteScythe(){
        super(
            "Netherite Scythe",
            CustomItemId.NETHERITE_SCYTHE,
            18,
            1
        );
    }


    @Override
    protected void setRecipe(final @NotNull ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }
}
