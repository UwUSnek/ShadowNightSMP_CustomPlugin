package org.uwu_snek.shadownight.customItems.implementations.dagger;

import org.bukkit.Material;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.customItems.ItemManager;
import org.uwu_snek.shadownight.customItems.guiManagers.CustomUpgradeSmithingRecipe;




public final class IM_DiamondDagger extends IM_Dagger_Craftable {
    public IM_DiamondDagger() {
        super(
            "Diamond Dagger",
            _custom_item_id.DIAMOND_DAGGER,
            4,
            0.1
        );
        upgradeRecipe = new CustomUpgradeSmithingRecipe(
            this,
            Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE,
            Material.NETHERITE_INGOT,
            ItemManager.NetheriteDagger.getInstance()
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.DIAMOND;
    }
}
