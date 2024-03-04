package org.uwu_snek.shadownight.items.dagger;

import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;




public final class IM_DiamondDagger extends IM_Dagger_NormalRecipe {
    public IM_DiamondDagger() {
        super(
            "Diamond Dagger",
            CustomItemId.DIAMOND_DAGGER,
            4,
            0.2
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.DIAMOND;
    }
}
