package org.uwu_snek.shadownight.itemFilter;

import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.enchantments.CustomEnchant_Spigot;
import org.uwu_snek.shadownight.utils.Rnd;
import org.uwu_snek.shadownight.utils.UtilityClass;

import java.util.*;




public final class EnchantBlacklist extends UtilityClass implements Rnd {
    public static final List<Enchantment> blockedEnchants = Arrays.asList(Enchantment.SWEEPING_EDGE);
    public static final ArrayList<Enchantment> allowedEnchants = new ArrayList<>();

    static {
        Arrays.stream(CustomEnchant_Spigot.values()).iterator().forEachRemaining(e -> {
            if (!e.isTreasure() && !blockedEnchants.contains(e)) allowedEnchants.add(e);
        });
    }


    /**
     * Removes any blacklisted enchantment from an item or enchanted book.
     * @param item The item to check
     * @return True if the item is an enchanted book and has 0 stored enchantments, false otherwise
     */
    private static boolean fixItemEnchants(final @NotNull ItemStack item) {

        // Remove books that contain one or more of the blocked enchantments
        if (item.getItemMeta() instanceof EnchantmentStorageMeta m) {
            for (Enchantment e : blockedEnchants) m.removeStoredEnchant(e);
            item.setItemMeta(m);
            return !m.hasStoredEnchants();
        }

        // Remove blocked enchants from normal items
        else {
            for (Enchantment e : blockedEnchants) item.removeEnchantment(e);
            return false;
        }
    }


    public static void onPlayerInteract(final @NotNull PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && block != null && block.getState() instanceof BlockInventoryHolder b) {
            Inventory inv = b.getInventory();
            for (ItemStack item : inv.getContents()) {
                if (item != null) if(fixItemEnchants(item)) inv.remove(item);
            }
        }
    }

    public static void onBlockBreak(final @NotNull BlockBreakEvent event) {
        Block block = event.getBlock();
        if (block.getState() instanceof BlockInventoryHolder b) {
            Inventory inv = b.getInventory();
            for (ItemStack item : inv.getContents()) {
                if (item != null) if(fixItemEnchants(item)) inv.remove(item);
            }
        }
    }

    public static void onPlayerFish(final @NotNull PlayerFishEvent event) {
        if (event.getCaught() instanceof Item e) {
            ItemStack item = e.getItemStack();
            fixItemEnchants(item);
            e.setItemStack(item);
        }
    }
}
