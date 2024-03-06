package org.uwu_snek.shadownight.items.implementations.spear;

import org.bukkit.Material;
import org.uwu_snek.shadownight._generated._custom_item_id;




public final class IM_IronSpear extends IM_Spear_Craftable {
    public IM_IronSpear() {
        super(
            "Iron Spear",
            _custom_item_id.IRON_SPEAR,
            6,
            0.625 // Swords default
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.IRON_INGOT;
    }
}
