package org.uwu_snek.shadownight.customItems.implementations.spear;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated._custom_item_id;




public final class IM_NetheriteSpear extends IM_Spear {
    public IM_NetheriteSpear() {
        super(
            "Netherite Spear",
            _custom_item_id.NETHERITE_SPEAR,
            8,
            0.625 // Swords default
        );
    }

    @Override
    protected Recipe createRecipe(@NotNull NamespacedKey key) {
        return null;
    }
}
