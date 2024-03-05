package org.uwu_snek.shadownight.items.spear;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;




public final class IM_NetheriteSpear extends IM_Spear {
    public IM_NetheriteSpear() {
        super(
            "Netherite Spear",
            CustomItemId.NETHERITE_SPEAR,
            8,
            0.625 // Swords default
        );
    }

    @Override
    protected Recipe createRecipe(@NotNull NamespacedKey key) {
        return null;
    }
}
