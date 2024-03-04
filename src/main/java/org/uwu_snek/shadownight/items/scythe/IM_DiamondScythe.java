package org.uwu_snek.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;


public final class IM_DiamondScythe extends IM_Scythe_NormalRecipe {
    public IM_DiamondScythe(){
        super(
            "Diamond Scythe",
            CustomItemId.DIAMOND_SCYTHE,
            16,
            1.25
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.DIAMOND;
    }

    @Override
    protected Material getRecipeBlock() {
        return Material.DIAMOND_BLOCK;
    }
}
