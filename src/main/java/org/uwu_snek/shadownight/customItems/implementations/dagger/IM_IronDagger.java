package org.uwu_snek.shadownight.customItems.implementations.dagger;

import org.bukkit.Material;
import org.uwu_snek.shadownight._generated._custom_item_id;




public final class IM_IronDagger extends IM_Dagger_Craftable {
    public IM_IronDagger() {
        super(
            "Iron Dagger",
            _custom_item_id.IRON_DAGGER,
            3,
            0.15
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.IRON_INGOT;
    }
}
