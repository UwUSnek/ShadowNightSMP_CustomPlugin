package org.uwu_snek.shadownight.items.implementations.scythe;


import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;



public final class IM_IronScythe extends IM_Scythe_Craftable {
    public IM_IronScythe(){
        super(
            "Iron Scythe",
            CustomItemId.IRON_SCYTHE,
            14,
            1.6
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.IRON_INGOT;
    }

    @Override
    protected Material getRecipeBlock() {
        return Material.IRON_BLOCK;
    }
}
