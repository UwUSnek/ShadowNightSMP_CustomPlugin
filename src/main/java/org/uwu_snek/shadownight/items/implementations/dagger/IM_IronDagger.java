package org.uwu_snek.shadownight.items.implementations.dagger;

import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;




public final class IM_IronDagger extends IM_Dagger_Craftable {
    public IM_IronDagger() {
        super(
            "Iron Dagger",
            CustomItemId.IRON_DAGGER,
            3,
            0.15
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.IRON_INGOT;
    }
}
