package org.uwu_snek.shadownight.items.spear;

import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;




public final class IM_DiamondSpear extends IM_Spear_NormalRecipe {
    public IM_DiamondSpear() {
        super(
            "Diamond Spear",
            CustomItemId.DIAMOND_SPEAR,
            7,
            0.625 // Swords default
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.DIAMOND;
    }
}
