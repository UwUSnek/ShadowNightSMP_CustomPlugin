package org.uwu_snek.shadownight.items;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTransformRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;




public class NetheriteUpgradeRecipe extends SmithingTransformRecipe {
    private final ItemStack base;
    private final ItemStack result;

    public NetheriteUpgradeRecipe(@NotNull NamespacedKey key, @NotNull ItemStack result, @NotNull RecipeChoice template, @NotNull ItemStack base, @NotNull RecipeChoice addition) {
        super(key, result, template, new RecipeChoice.ExactChoice(base), addition);
        this.base = base;
        this.result = result;
    }

    @NotNull
    public ItemStack getResult() {
        ItemStack r = base.clone();
        r.setType(result.getType());

        ItemMeta meta = r.getItemMeta();
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);

        return ;
    }
}
