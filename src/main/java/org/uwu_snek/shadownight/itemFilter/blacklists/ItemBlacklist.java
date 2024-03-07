package org.uwu_snek.shadownight.itemFilter.blacklists;

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
    public static boolean deleteIfBLacklisted(final @NotNull ItemStack item, final @Nullable Player player) {
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
}
