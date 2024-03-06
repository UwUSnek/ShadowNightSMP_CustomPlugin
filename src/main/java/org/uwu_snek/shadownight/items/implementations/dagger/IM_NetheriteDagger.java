package org.uwu_snek.shadownight.items.implementations.dagger;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated._custom_item_id;




public final class IM_NetheriteDagger extends IM_Dagger {
    public IM_NetheriteDagger() {
        super(
            "Netherite Dagger",
            _custom_item_id.NETHERITE_DAGGER,
            5,
            0.15
        );
    }

    @Override
    protected Recipe createRecipe(@NotNull NamespacedKey key) {
        return null;
    }
}
