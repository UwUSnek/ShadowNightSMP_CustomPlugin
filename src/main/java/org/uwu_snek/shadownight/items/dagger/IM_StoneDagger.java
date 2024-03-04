package org.uwu_snek.shadownight.items.dagger;

import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;




public final class IM_StoneDagger extends IM_Dagger_NormalRecipe {
    public IM_StoneDagger() {
        super(
            "Stone Dagger",
            CustomItemId.STONE_DAGGER,
            2,
            0.25
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.COBBLESTONE;
    }
}
