package org.uwu_snek.shadownight.customItems.itemFilter.blacklists;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.List;




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
