package org.uwu_snek.shadownight.items.scythe;


import org.bukkit.Material;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.items.recipeManagers.CustomUpgradeSmithingRecipe;




public final class IM_DiamondScythe extends IM_Scythe_NormalRecipe {
    public IM_DiamondScythe(){
        super(
            "Diamond Scythe",
            CustomItemId.DIAMOND_SCYTHE,
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
