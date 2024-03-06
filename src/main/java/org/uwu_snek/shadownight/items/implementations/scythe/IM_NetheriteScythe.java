package org.uwu_snek.shadownight.items.implementations.scythe;


import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated._custom_item_id;




public final class IM_NetheriteScythe extends IM_Scythe {
    public IM_NetheriteScythe(){
        super(
            "Netherite Scythe",
            _custom_item_id.NETHERITE_SCYTHE,
            18,
            1
        );
    }

    @Override
    protected Recipe createRecipe(@NotNull NamespacedKey key) {
        return null;
    }
}
