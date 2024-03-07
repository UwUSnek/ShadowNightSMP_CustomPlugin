package org.uwu_snek.shadownight.items.implementations.spear;

import org.bukkit.Material;
import org.uwu_snek.shadownight._generated._custom_item_id;




public final class IM_GoldenSpear extends IM_Spear_Craftable {
    public IM_GoldenSpear() {
        super(
            "Golden Spear",
            _custom_item_id.GOLDEN_SPEAR,
            4,
            0.625 // Swords default
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.GOLD_INGOT;
    }
}
