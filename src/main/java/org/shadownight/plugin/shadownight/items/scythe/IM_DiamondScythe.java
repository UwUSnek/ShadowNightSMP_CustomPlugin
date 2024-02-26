package org.shadownight.plugin.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.attackOverride.attacks.ATK_ConeArea;
import org.shadownight.plugin.shadownight.items.CustomItemId;


public final class IM_DiamondScythe extends IM_Scythe {
    @Override public Material     getMaterial()        { return Material.DIAMOND_SWORD;      }
    @Override public String       getDisplayName()     { return "Diamond Scythe";            }
    @Override public int          getCustomModelData() { return 1;                           }
    @Override public CustomItemId getCustomId()        { return CustomItemId.DIAMOND_SCYTHE; }

    @Override protected double getAttackSpeed() { return -3.2; }
    @Override public    double getHitDamage()   { return 16;   }
    @Override public double getHitKnockbackMultiplier() { return 2; }


    @Override
    protected void setRecipe(final @NotNull ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.DIAMOND);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }
}
