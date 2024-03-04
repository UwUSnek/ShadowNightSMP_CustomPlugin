package org.uwu_snek.shadownight.items.dagger;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.ItemManager;




public final class IM_NetheriteDagger extends IM_Dagger {
    public IM_NetheriteDagger() {
        super(
            "Netherite Dagger",
            CustomItemId.NETHERITE_DAGGER,
            5,
            0.25
        );
    }

    @Override
    protected Recipe createRecipe(@NotNull NamespacedKey key) {
        return new SmithingTransformRecipe(
            key,
            defaultItem,
            new RecipeChoice.MaterialChoice(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
            new RecipeChoice.ExactChoice(ItemManager.DiamondDagger.getInstance().createDefaultItemStack()),
            new CustomItemUpgradeRecipe()
        new RecipeChoice.MaterialChoice(Material.NETHERITE_INGOT)
        );
    }
}
