package org.uwu_snek.shadownight.items.implementations.dagger;

import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.items.guiManagers.CustomUpgradeSmithingRecipe;




public final class IM_DiamondDagger extends IM_Dagger_NormalRecipe {
    public IM_DiamondDagger() {
        super(
            "Diamond Dagger",
            CustomItemId.DIAMOND_DAGGER,
            4,
            0.2
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
