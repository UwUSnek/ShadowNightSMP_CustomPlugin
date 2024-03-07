package org.uwu_snek.shadownight.itemFilter;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.minecraft.world.item.EnchantedBookItem;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.VillagerAcquireTradeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.utils;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;




public final class ItemBlacklist extends UtilityClass {
    private static void delete(final @NotNull ItemStack item, final @Nullable Player player) {
        item.setAmount(0);
        if(player != null) {
            //TODO log this stuff in a separate directory
            ChatUtils.sendMessage(player, "§c§lA blacklisted item has been removed from your inventory (§f\"" + ItemUtils.getPlainItemName(item) + "\"§c§l).");
            ChatUtils.sendMessage(player, "§cIf you believe this is an error, screenshot this message and contact a staff member on discord.");
        }
    }

    /**
     * Checks if an item is blacklisted and deletes it if that's the case.
     * @param item The item to check
     * @return True if the item has been deleted, false otherwise
     */
    private static boolean deleteIfBLacklisted(final @NotNull ItemStack item, final @Nullable Player player) {
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            List<Component> lore = meta.lore();
            if (lore != null) for (Component line : lore) {
                if (PlainTextComponentSerializer.plainText().serialize(line).equals("Incendium")) {
                    delete(item, player);
                    return true;
                }
            }
        }
        return false;
    }






    /**
     * Detects items in slots clicked by the player.
     * - Filters items and enchantments
     */
    public static void onClickEvent(final @NotNull InventoryClickEvent event) {
        final Player player = event.getWhoClicked() instanceof Player p ? p : null;
        final ItemStack cursor = event.getCursor();
        final ItemStack item = event.getCurrentItem();
        deleteIfBLacklisted(cursor, player);
        if(item != null) if(!deleteIfBLacklisted(item, player)) EnchantBlacklist.fixItemEnchants(item);
    }


    /**
     * Detects items dropped by a player.
     * - Filters items
     */
    public static void onPlayerDropItem(final @NotNull PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final Item itemEntity = event.getItemDrop();
        if(deleteIfBLacklisted(itemEntity.getItemStack(), player)) itemEntity.remove();
    }


    /**
     * Detects any item entity spawn.
     * - Filters items
     */
    public static void onItemSpawn(final @NotNull ItemSpawnEvent event) {
        final Item itemEntity = event.getEntity();
        if(deleteIfBLacklisted(itemEntity.getItemStack(), null)) itemEntity.remove();
    }


    /**
     * Detects items fished up by players.
     * - Filters items and enchantments
     */
    public static void onPlayerFish(final @NotNull PlayerFishEvent event) {
        if (event.getCaught() instanceof Item e) {
            final ItemStack item = e.getItemStack();
            if(EnchantBlacklist.fixItemEnchants(item)) e.setItemStack(item);
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
                    if(!deleteIfBLacklisted(item, null)) EnchantBlacklist.fixItemEnchants(item);
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
                    if(!deleteIfBLacklisted(item, null)) EnchantBlacklist.fixItemEnchants(item);
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
        }
    }
}
