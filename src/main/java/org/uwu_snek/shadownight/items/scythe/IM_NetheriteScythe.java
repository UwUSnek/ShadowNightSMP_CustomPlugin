package org.uwu_snek.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.ItemManager;




public final class IM_NetheriteScythe extends IM_Scythe {
    public IM_NetheriteScythe(){
        super(
            "Netherite Scythe",
            CustomItemId.NETHERITE_SCYTHE,
            18,
            1
        );
    }

    @Override
    protected Recipe createRecipe(@NotNull NamespacedKey key) {
        return new SmithingTransformRecipe(
            key,
            defaultItem,
            new RecipeChoice.MaterialChoice(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            new RecipeChoice.ExactChoice(ItemManager.DiamondScythe.getInstance().createDefaultItemStack()),
            new RecipeChoice.MaterialChoice(Material.NETHERITE_INGOT)
        );
    }
}
