package org.uwu_snek.shadownight.customItems.implementations.spear;

import org.bukkit.Material;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.customItems.ItemManager;
import org.uwu_snek.shadownight.customItems.guiManagers.CustomUpgradeSmithingRecipe;




public final class IM_DiamondSpear extends IM_Spear_Craftable {
    public IM_DiamondSpear() {
        super(
            "Diamond Spear",
            _custom_item_id.DIAMOND_SPEAR,
            7,
            0.625 // Swords default
        );
        upgradeRecipe = new CustomUpgradeSmithingRecipe(
            this,
            Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            Material.NETHERITE_INGOT,
            ItemManager.NetheriteSpear.getInstance()
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.DIAMOND;
    }
}
