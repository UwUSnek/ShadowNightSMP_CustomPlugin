package org.uwu_snek.shadownight.items.dagger;

import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;




public final class IM_GoldenDagger extends IM_Dagger_NormalRecipe {
    public IM_GoldenDagger() {
        super(
            "Golden Dagger",
            CustomItemId.GOLDEN_DAGGER,
            1.5,
            0.2
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.GOLD_INGOT;
    }
}
