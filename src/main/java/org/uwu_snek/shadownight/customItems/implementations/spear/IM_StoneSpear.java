package org.uwu_snek.shadownight.customItems.implementations.spear;

import org.bukkit.Material;
import org.uwu_snek.shadownight._generated._custom_item_id;




public final class IM_StoneSpear extends IM_Spear_Craftable {
    public IM_StoneSpear() {
        super(
            "Stone Spear",
            _custom_item_id.STONE_SPEAR,
            5,
            0.625 // Swords default
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.COBBLESTONE;
    }
}
