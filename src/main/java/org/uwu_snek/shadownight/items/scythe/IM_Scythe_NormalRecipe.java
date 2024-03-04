package org.uwu_snek.shadownight.items.scythe;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_ConeArea;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.IM;




public abstract class IM_Scythe_NormalRecipe extends IM_Scythe {
    public IM_Scythe_NormalRecipe(final @NotNull String _displayName, final @NotNull CustomItemId _customItemId, final double _hitDamage, final double _atkSpeed) {
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
        recipe.shape("MMB", " S ", "S  ");
        recipe.setIngredient('M', getRecipeMaterial());
        recipe.setIngredient('B', getRecipeBlock());
        recipe.setIngredient('S', Material.STICK);
        return recipe;
    }
    protected abstract Material getRecipeMaterial();
    protected abstract Material getRecipeBlock();
}
