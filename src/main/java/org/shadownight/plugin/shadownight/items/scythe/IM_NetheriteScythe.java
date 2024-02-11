package org.shadownight.plugin.shadownight.items.scythe;


import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;
import org.shadownight.plugin.shadownight.items.CustomItemId;


public class IM_NetheriteScythe extends IM_Scythe {
    @Override public Material     getMaterial()        { return Material.NETHERITE_SWORD;      }
    @Override public String       getDisplayName()     { return "Netherite Scythe";            }
    @Override public int          getCustomModelData() { return 1;                             }
    @Override public CustomItemId getCustomId()        { return CustomItemId.NETHERITE_SCYTHE; }

    @Override protected double       getAttackSpeed()     { return -3.0;                          }
    @Override protected double       getDamage()          { return 14;                            }


    @Override
    protected void setRecipe(ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }
}
