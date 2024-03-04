package org.uwu_snek.shadownight.items.spear;

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
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_LineArea;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.IM;




public abstract class IM_Spear_NormalRecipe extends IM_Spear {
    public IM_Spear_NormalRecipe(final @NotNull String _displayName, final @NotNull CustomItemId _customItemId, final double _hitDamage, final double _atkSpeed) {
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
        recipe.shape("  M", " S ", "S  ");
        recipe.setIngredient('M', getRecipeMaterial());
        recipe.setIngredient('S', Material.STICK);
        return recipe;
    }
    protected abstract Material getRecipeMaterial();
}
