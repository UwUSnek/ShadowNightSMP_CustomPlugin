package org.uwu_snek.shadownight.utils.spigot;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.utils.UtilityClass;

import java.util.Arrays;
import java.util.Objects;

import static org.uwu_snek.shadownight.items.IM.itemIdKey;




public final class ItemUtils extends UtilityClass {
    /**
     * Creates an item with a custom name and lore.
     * @param material The material of the item stack
     * @param number The stack size
     * @param name The display name
     * @param lore The item lore
     * @return The created ItemStack
     */
    public static ItemStack createItemStack(final @NotNull Material material, final int number, final @NotNull String name, final @NotNull String... lore) {
        final ItemStack item = new ItemStack(material, number);
        final ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Object meta is null");

        meta.setDisplayName("§f" + name);
        if(lore.length > 0) meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);
        return item;
    }



    /**
     * Creates an item with a custom name, model, id and lore.
     * @param material The material of the item stack
     * @param number The stack size
     * @param name The display name
     * @param customModelData The custom model data to use
     * @param customItemId The custom ItemID
     * @param lore The item lore
     * @return The created ItemStack
     */
    public static ItemStack createItemStackCustom(
        final @NotNull Material material,
        final int number,
        final @NotNull String name,
        final int customModelData,
        final long customItemId,
        final @NotNull String... lore
    ) {
        final ItemStack item = new ItemStack(material, number);
        final ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Object meta is null");

        meta.setDisplayName("§f" + name);
        if(lore.length > 0) meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(customModelData);

        final PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(itemIdKey, PersistentDataType.LONG, customItemId);

        item.setItemMeta(meta);
        return item;
    }



    /**
     * Damages an item and deletes it if it's durability reaches 0.
     * @param entity The entity that owns the item
     * @param item The item to damage
     */
    public static void damageItem(final @NotNull LivingEntity entity, final @NotNull ItemStack item) {
        if(!(entity instanceof Player player && player.getGameMode() == GameMode.CREATIVE) && item.getAmount() > 0) { // amount <= 0 means that a different thread broke the item. No need to damage it further
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable _meta) {
                if(_meta.getDamage() >= item.getType().getMaxDurability()) {
                    item.setAmount(0);
                    entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                }
                else {
                    _meta.setDamage(_meta.getDamage() + 1);
                    item.setItemMeta(_meta);
                }
            }
        }
    }


    /**
     * Returns the display name if the item has one.
     * If not, a name is created based on its material and potion effects. This might not match the Vanilla name.
     * @param item The ItemStack to get the name of
     * @return The display name
     */
    public static String getItemName(final @NotNull ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(item.hasItemMeta() && meta.hasDisplayName()) {
            return meta.getDisplayName();
        }
        else {
            final String[] words = item.getType().name().split("_");
            final StringBuilder r = new StringBuilder();
            for(String w : words) r.append(w.substring(0, 1).toUpperCase()).append(w.substring(1).toLowerCase()).append(" ");
            return r.toString();
        }
        //TODO check potion name from their effect data
    }


    /**
     * Finds and returns the CustomItemId of an ItemStack.
     * @param item The item stack
     * @return The custom ID. null if the item is null or air or has no custom id
     */
    public static Long getCustomItemId(final @Nullable ItemStack item) {
        if(item == null || item.getType() == Material.AIR) return null;
        PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta(), "Item meta is null").getPersistentDataContainer();
        return container.get(IM.itemIdKey, PersistentDataType.LONG);
    }
}
