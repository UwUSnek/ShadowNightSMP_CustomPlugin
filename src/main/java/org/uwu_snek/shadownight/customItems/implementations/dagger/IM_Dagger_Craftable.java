package org.uwu_snek.shadownight.customItems.implementations.dagger;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated._custom_item_id;




public abstract class IM_Dagger_Craftable extends IM_Dagger {
    public IM_Dagger_Craftable(final @NotNull String _displayName, final @NotNull _custom_item_id _customItemId, final double _hitDamage, final double _atkSpeed) {
        super(
            _displayName,
            _customItemId,
            _hitDamage,
            _atkSpeed
        );
    }



    @Override
    protected Recipe createRecipe(@NotNull NamespacedKey key) {
        final ShapedRecipe recipe = new ShapedRecipe(key, defaultItem);
        recipe.shape("   ", " M ", " S ");
        recipe.setIngredient('M', getRecipeMaterial());
        recipe.setIngredient('S', Material.STICK);
        return recipe;
    }
    protected abstract Material getRecipeMaterial();
}
