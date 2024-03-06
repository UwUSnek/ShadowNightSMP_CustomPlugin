package org.uwu_snek.shadownight.items.implementations.scythe;


import org.bukkit.Material;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.items.guiManagers.CustomUpgradeSmithingRecipe;




public final class IM_DiamondScythe extends IM_Scythe_Craftable {
    public IM_DiamondScythe(){
        super(
            "Diamond Scythe",
            _custom_item_id.DIAMOND_SCYTHE,
            16,
            1.25
        );
        upgradeRecipe = new CustomUpgradeSmithingRecipe(
            this,
            Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            Material.NETHERITE_INGOT,
            ItemManager.NetheriteScythe.getInstance()
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.DIAMOND;
    }

    @Override
    protected Material getRecipeBlock() {
        return Material.DIAMOND_BLOCK;
    }
}
