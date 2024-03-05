package org.uwu_snek.shadownight.items.implementations.dagger;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;




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
        return null;
    }
}
