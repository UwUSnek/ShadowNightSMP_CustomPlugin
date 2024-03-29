package org.uwu_snek.shadownight.customItems.itemFilter;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.customItems.itemFilter.blacklists.EnchantBlacklist;
import org.uwu_snek.shadownight.customItems.itemFilter.blacklists.ItemBlacklist;
import org.uwu_snek.shadownight.customItems.itemFilter.blacklists.ItemVolatileList;
import org.uwu_snek.shadownight.customItems.itemFilter.decorators.Decorator;
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

        if(cursor.getType() != Material.AIR) {
            if (!ItemBlacklist.deleteIfBLacklisted(cursor, player)) {
                if (!EnchantBlacklist.fixItemEnchants(cursor)) {
                    Decorator.decorate(cursor, false);
                }
            }
        }

        if (item != null && item.getType() != Material.AIR) {
            if (!ItemBlacklist.deleteIfBLacklisted(item, player)) {
                if (!EnchantBlacklist.fixItemEnchants(item)) {
                    Decorator.decorate(item, false);
                }
            }
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
            Decorator.decorate(newItemStack, false);
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
        else if(!ItemVolatileList.onItemSpawn(event)) Decorator.decorate(newItemStack, false);
    }


    /**
     * Detects items fished up by players.
     * - Filters items and enchantments
     */
    public static void onPlayerFish(final @NotNull PlayerFishEvent event) {
        if (event.getCaught() instanceof Item e) {
            final ItemStack item = e.getItemStack();
            if(EnchantBlacklist.fixItemEnchants(item)) e.setItemStack(item);
            else Decorator.decorate(item, false);
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
                if (item != null && item.getType() != Material.AIR) {
                    if(!ItemBlacklist.deleteIfBLacklisted(item, null)) // Don't send message to player as it would also appear when opening freshly generated loot containers
                        if(!EnchantBlacklist.fixItemEnchants(item))
                            Decorator.decorate(item, false);
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
                if (item != null && item.getType() != Material.AIR) {
                    if(!ItemBlacklist.deleteIfBLacklisted(item, null))
                        if(!EnchantBlacklist.fixItemEnchants(item))
                            Decorator.decorate(item, false);
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
                Decorator.decorate(item, false);
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
        final CraftingInventory inv = event.getInventory();
        final ItemStack result = inv.getResult();
        if (result != null && result.getType() != Material.AIR) {
            if (!ItemBlacklist.deleteIfBLacklisted(result, null))
                Decorator.decorate(result, true);
            inv.setResult(result);
        }
    }

    /**
     * Detects anvil merges.
     */
    public static void onPrepareAnvil(final @NotNull PrepareAnvilEvent event) {
        final AnvilInventory inv = event.getInventory();
        final ItemStack result = inv.getResult();
        if (result != null && result.getType() != Material.AIR) {
            Decorator.decorate(result, true);
            inv.setResult(result);
        }
    }

    /**
     * Detects smithing table events.
     */
    public static void onPrepareSmithing(final @NotNull PrepareSmithingEvent event) {
        final SmithingInventory inv = event.getInventory();
        final ItemStack result = inv.getResult();
        if (result != null && result.getType() != Material.AIR) {
            Decorator.decorate(result, true);
            inv.setResult(result);
        }
    }

    /**
     * Detects grindstone events.
     */
    public static void onPrepareGrindstone(final @NotNull PrepareGrindstoneEvent event) {
        final GrindstoneInventory inv = event.getInventory();
        final ItemStack result = inv.getResult();
        if (result != null && result.getType() != Material.AIR) {
            Decorator.decorate(result, true);
            event.setResult(result);
        }
    }

    /**
     * Detects anvil merges.
     */
    public static void onEnchantItem(final @NotNull EnchantItemEvent event) {
        final ItemStack item = event.getItem();
        Decorator.decorate(item, true);
    }

    /**
     * Detects player logins.
     * Filters items and enchants
     */
    public static void onPlayerJoin(final @NotNull PlayerJoinEvent event) {
        final PlayerInventory inv = event.getPlayer().getInventory();
        for(int i = 0; i < inv.getSize(); ++i) {
            final ItemStack item = inv.getItem(i);
            if (item != null && item.getType() != Material.AIR) {
                if(!ItemBlacklist.deleteIfBLacklisted(item, null))
                    if(!EnchantBlacklist.fixItemEnchants(item))
                        Decorator.decorate(item, false);
            }
        }
    }
}
