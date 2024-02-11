package org.shadownight.plugin.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.shadownight.plugin.shadownight.items.CustomItemId;



public class IM_IronScythe extends IM_Scythe {
    @Override public Material     getMaterial()        { return Material.IRON_SWORD;      }
    @Override public String       getDisplayName()     { return "Iron Scythe";            }
    @Override public int          getCustomModelData() { return 1;                        }
    @Override public CustomItemId getCustomId()        { return CustomItemId.IRON_SCYTHE; }

    @Override protected double       getAttackSpeed()     { return -3.4;                     }
    @Override protected double       getDamage()          { return 10;                       }


    @Override
    protected void setRecipe(ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }
}
