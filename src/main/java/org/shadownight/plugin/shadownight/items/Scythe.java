package org.shadownight.plugin.shadownight.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.utils;




public class Scythe {
    private static final int SCYTHE_DATA = 1;
    private static final ItemStack ironItem      = utils.createItemStackCustom(Material.IRON_SWORD,      1, "Iron Scythe",         SCYTHE_DATA, ItemManager.CusotmItemId.IRON_SCYTHE);
    private static final ItemStack diamondItem   = utils.createItemStackCustom(Material.DIAMOND_SWORD,   1, "Diamond Scythe",      SCYTHE_DATA, ItemManager.CusotmItemId.DIAMOND_SCYTHE);
    private static final ItemStack netheriteItem = utils.createItemStackCustom(Material.NETHERITE_SWORD, 1, "Netherite Scythe",    SCYTHE_DATA, ItemManager.CusotmItemId.NETHERITE_SCYTHE);
    private static final ItemStack klaueItem     = utils.createItemStackCustom(Material.NETHERITE_SWORD, 1, "Klaue's Edgy Scythe", 14,          ItemManager.CusotmItemId.KLAUE_SCYTHE);




    public static void createRecipes(){
        NamespacedKey ironKey = new NamespacedKey(ShadowNight.plugin, "ironScythe");
        ShapedRecipe ironRecipe = new ShapedRecipe(ironKey, ironItem);

        ironRecipe.shape("III", "  S", " S ");
        ironRecipe.setIngredient('I', Material.IRON_INGOT);
        //ironRecipe.setIngredient('S', Material.STICK);
        ironRecipe.setIngredient('S', Material.COMMAND_BLOCK);
        Bukkit.addRecipe(ironRecipe); //TODO make this a rare drop instead of a recipe




        NamespacedKey diamondKey = new NamespacedKey(ShadowNight.plugin, "diamondScythe");
        ShapedRecipe diamondRecipe = new ShapedRecipe(diamondKey, diamondItem);

        diamondRecipe.shape("DDD", "  S", " S ");
        diamondRecipe.setIngredient('D', Material.DIAMOND);
        //diamondRecipe.setIngredient('S', Material.STICK);
        diamondRecipe.setIngredient('S', Material.COMMAND_BLOCK);
        Bukkit.addRecipe(diamondRecipe); //TODO make this a rare drop instead of a recipe




        NamespacedKey klaueKey = new NamespacedKey(ShadowNight.plugin, "klaueScythe");
        ShapedRecipe klaueRecipe = new ShapedRecipe(klaueKey, klaueItem);

        klaueRecipe.shape("DDD", "  S", " S ");
        klaueRecipe.setIngredient('D', Material.COMMAND_BLOCK);
        klaueRecipe.setIngredient('S', Material.COMMAND_BLOCK);
        Bukkit.addRecipe(klaueRecipe);




        NamespacedKey netheriteKey = new NamespacedKey(ShadowNight.plugin, "netheriteScythe");
        SmithingTransformRecipe netheriteRecipe = new SmithingTransformRecipe(
            netheriteKey,
            netheriteItem,
            new RecipeChoice.ExactChoice(new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1)),
            new RecipeChoice.ExactChoice(diamondItem),
            new RecipeChoice.ExactChoice(netheriteItem)
        );
        Bukkit.addRecipe(netheriteRecipe);
    }





    static public void onInteractNormal(PlayerInteractEvent event) {

    }




    static public void onInteractKlaue(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if(player.isSneaking()) Bukkit.broadcastMessage("shadow fury knock off");
        }
        if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if(player.isSneaking()) Bukkit.broadcastMessage("teleport");
            else Bukkit.broadcastMessage("throw");
        }
    }
}
