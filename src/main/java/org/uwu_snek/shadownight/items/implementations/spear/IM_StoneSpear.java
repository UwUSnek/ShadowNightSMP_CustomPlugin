package org.uwu_snek.shadownight.items.implementations.spear;

import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;




public final class IM_StoneSpear extends IM_Spear_Craftable {
    public IM_StoneSpear() {
        super(
            "Stone Spear",
            CustomItemId.STONE_SPEAR,
            5,
            0.625 // Swords default
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.COBBLESTONE;
    }
}
