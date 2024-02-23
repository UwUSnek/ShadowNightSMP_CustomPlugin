package org.shadownight.plugin.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.items.CustomItemId;


public final class IM_NetheriteScythe extends IM_Scythe {
    @Override public Material     getMaterial()        { return Material.NETHERITE_SWORD;      }
    @Override public String       getDisplayName()     { return "Netherite Scythe";            }
    @Override public int          getCustomModelData() { return 1;                             }
    @Override public CustomItemId getCustomId()        { return CustomItemId.NETHERITE_SCYTHE; }

    @Override protected double getAttackSpeed() { return -3.0; }
    @Override public    double getHitDamage()   { return 18;   }
    @Override public double getHitKnockback() { return 2; }


    @Override
    protected void setRecipe(final @NotNull ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }
}
