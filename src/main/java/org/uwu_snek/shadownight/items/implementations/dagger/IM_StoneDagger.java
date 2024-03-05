package org.uwu_snek.shadownight.items.implementations.dagger;

import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;




public final class IM_StoneDagger extends IM_Dagger_Craftable {
    public IM_StoneDagger() {
        super(
            "Stone Dagger",
            CustomItemId.STONE_DAGGER,
            2,
            0.15
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.COBBLESTONE;
    }
}
