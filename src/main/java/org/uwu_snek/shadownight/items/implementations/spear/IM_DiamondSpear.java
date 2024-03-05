package org.uwu_snek.shadownight.items.implementations.spear;

import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.items.guiManagers.CustomUpgradeSmithingRecipe;




public final class IM_DiamondSpear extends IM_Spear_NormalRecipe {
    public IM_DiamondSpear() {
        super(
            "Diamond Spear",
            CustomItemId.DIAMOND_SPEAR,
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
