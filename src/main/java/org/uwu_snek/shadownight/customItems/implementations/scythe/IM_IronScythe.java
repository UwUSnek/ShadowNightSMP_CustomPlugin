package org.uwu_snek.shadownight.customItems.implementations.scythe;


import org.bukkit.Material;
import org.uwu_snek.shadownight._generated._custom_item_id;



public final class IM_IronScythe extends IM_Scythe_Craftable {
    public IM_IronScythe(){
        super(
            "Iron Scythe",
            _custom_item_id.IRON_SCYTHE,
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
