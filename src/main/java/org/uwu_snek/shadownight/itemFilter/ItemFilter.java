package org.uwu_snek.shadownight.itemFilter;

import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.itemFilter.blacklists.EnchantBlacklist;
import org.uwu_snek.shadownight.itemFilter.blacklists.ItemBlacklist;
import org.uwu_snek.shadownight.itemFilter.blacklists.ItemVolatileList;
import org.uwu_snek.shadownight.itemFilter.decorators.Decorator;
import org.uwu_snek.shadownight.utils.UtilityClass;




public final class ItemFilter extends UtilityClass {
    public static void onInventoryDrag(final @NotNull InventoryDragEvent event) {
        ItemVolatileList.onInventoryDrag(event);
    }


    /**
     * Detects items in slots clicked by the player.
     * - Filters items and enchantments
     */
    public static void onInventiryClick(final @NotNull InventoryClickEvent event) {
        ItemVolatileList.onInventiryClick(event);
        final Player player = event.getWhoClicked() instanceof Player p ? p : null;
        final ItemStack cursor = event.getCursor();
        final ItemStack item = event.getCurrentItem();

        if (!ItemBlacklist.deleteIfBLacklisted(cursor, player))
            if (!EnchantBlacklist.fixItemEnchants(cursor))
                Decorator.decorate(cursor);

        if (item != null) {
            if (!ItemBlacklist.deleteIfBLacklisted(item, player))
                if (!EnchantBlacklist.fixItemEnchants(item))
                    Decorator.decorate(item);
        }
    }


    /**
     * Detects items dropped by a player.
     * - Filters items
     */
    public static void onPlayerDropItem(final @NotNull PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final Item itemEntity = event.getItemDrop();
        final ItemStack newItemStack = itemEntity.getItemStack();

        if(ItemBlacklist.deleteIfBLacklisted(newItemStack, player)) itemEntity.remove();
        else {
            Decorator.decorate(newItemStack);
            itemEntity.setItemStack(newItemStack);
        }
    }


    /**
     * Detects any item entity spawn.
     * - Filters items
     */
    public static void onItemSpawn(final @NotNull ItemSpawnEvent event) {
        final Item itemEntity = event.getEntity();
        final ItemStack newItemStack = itemEntity.getItemStack();

        if(ItemBlacklist.deleteIfBLacklisted(newItemStack, null)) itemEntity.remove();
        else if(!ItemVolatileList.onItemSpawn(event)) Decorator.decorate(newItemStack);
    }


    /**
     * Detects items fished up by players.
     * - Filters items and enchantments
     */
    public static void onPlayerFish(final @NotNull PlayerFishEvent event) {
        if (event.getCaught() instanceof Item e) {
            final ItemStack item = e.getItemStack();
            if(EnchantBlacklist.fixItemEnchants(item)) e.setItemStack(item);
            else Decorator.decorate(item);
        }
    }

    /**
     * Detects items in containers opened by players.
     * Filters items and enchantments
     */
    public static void onPlayerInteract(final @NotNull PlayerInteractEvent event) {
        final Block block = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getState() instanceof BlockInventoryHolder b) {
            Inventory inv = b.getInventory();
            for (ItemStack item : inv.getContents()) {
                if (item != null) {
                    if(!ItemBlacklist.deleteIfBLacklisted(item, null))
                        if(!EnchantBlacklist.fixItemEnchants(item))
                            Decorator.decorate(item);
                }
            }
        }
    }

    /**
     * Detects drops from broken containers.
     * Filters items and enchantments
     */
    public static void onBlockBreak(final @NotNull BlockBreakEvent event) {
        final Block block = event.getBlock();
        if (block.getState() instanceof BlockInventoryHolder b) {
            Inventory inv = b.getInventory();
            for (ItemStack item : inv.getContents()) {
                if (item != null) {
                    if(!ItemBlacklist.deleteIfBLacklisted(item, null))
                        if(!EnchantBlacklist.fixItemEnchants(item))
                            Decorator.decorate(item);
                }
            }
        }
    }

    /**
     * Detects new trades of librarian villagers.
     * Filters enchantments
     */
    public static void onVillagerAquireTrade(final @NotNull VillagerAcquireTradeEvent event) {
        final MerchantRecipe recipe = event.getRecipe();
        if(event.getEntity() instanceof Villager v && v.getProfession() == Villager.Profession.LIBRARIAN) {
            if(EnchantBlacklist.fixItemEnchants(recipe.getResult())) {
                event.setRecipe(EnchantBlacklist.rerollVillager(recipe));
            }
            else {
                final ItemStack item = recipe.getResult();
                Decorator.decorate(item);
                event.setRecipe(new MerchantRecipe(
                    item,
                    recipe.getUses(),
                    recipe.getMaxUses(),
                    recipe.hasExperienceReward(),
                    recipe.getVillagerExperience(),
                    recipe.getPriceMultiplier(),
                    recipe.shouldIgnoreDiscounts()
                ));
            }
        }
    }


    /**
     * Detects item crafting.
     * Filters items
     */
    public static void onPrepareItemCraft(final @NotNull PrepareItemCraftEvent event) {
        CraftingInventory inv = event.getInventory();
        ItemStack result = inv.getResult();
        if (result != null) {
            if (!ItemBlacklist.deleteIfBLacklisted(result, null))
                Decorator.decorate(result);
            inv.setResult(result);
        }
    }
}
