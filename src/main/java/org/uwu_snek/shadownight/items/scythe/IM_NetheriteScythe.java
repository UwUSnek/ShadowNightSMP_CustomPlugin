package org.uwu_snek.shadownight.items.scythe;


import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;




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
        return null;
    }
}
