package org.shadownight.plugin.shadownight.items;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.utils;




public class Scythe {
    private static final int SCYTHE_DATA = 1;
    public static final ItemStack ironItem      = utils.createItemStackCustom(Material.IRON_SWORD,      1, "Iron Scythe",         SCYTHE_DATA, ItemManager.CusotmItemId.IRON_SCYTHE);
    public static final ItemStack diamondItem   = utils.createItemStackCustom(Material.DIAMOND_SWORD,   1, "Diamond Scythe",      SCYTHE_DATA, ItemManager.CusotmItemId.DIAMOND_SCYTHE);
    public static final ItemStack netheriteItem = utils.createItemStackCustom(Material.NETHERITE_SWORD, 1, "Netherite Scythe",    SCYTHE_DATA, ItemManager.CusotmItemId.NETHERITE_SCYTHE);
    public static final ItemStack klaueItem     = utils.createItemStackCustom(Material.NETHERITE_SWORD, 1, "Klaue's Edgy Scythe", 14,          ItemManager.CusotmItemId.KLAUE_SCYTHE);




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

        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if(player.isSneaking()) Bukkit.broadcastMessage("shadow fury knock off");
        }
        else if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(player.isSneaking()) {
                Block targetBlock = player.getTargetBlockExact(1000, FluidCollisionMode.NEVER);
                if(targetBlock != null) {
                    Location targetBlockLocation = targetBlock.getLocation();
                    Location playerLocation = player.getLocation();
                    // Teleport with delay. 0 delay causes the event to be fired twice
                    Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () -> {
                        player.teleport(new Location(
                            playerLocation.getWorld(),
                            targetBlockLocation.getX() + 0.5,
                            targetBlockLocation.getY() + 1,
                            targetBlockLocation.getZ() + 0.5,
                            playerLocation.getYaw(),
                            playerLocation.getPitch()
                        ));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute at " + player.getName() + " run particle minecraft:witch ~ ~.1 ~ 1 0 1 0 100 force");
                    }, 2L);
                }
            }
            else {
                ScytheThrowDisplay display = new ScytheThrowDisplay(player);
            }
        }
    }
}
