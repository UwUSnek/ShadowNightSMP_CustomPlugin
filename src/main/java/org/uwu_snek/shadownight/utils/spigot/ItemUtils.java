package org.uwu_snek.shadownight.utils.spigot;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
    public static ItemStack createItemStack(final @NotNull Material material, final int number, final @NotNull String name, final @NotNull Component... lore) {
        final ItemStack item = new ItemStack(material, number);
        final ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Object meta is null");

        meta.displayName(Component.text("§f" + name));
        if(lore.length > 0) meta.lore(Arrays.asList(lore));

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
        final @NotNull Component... lore
    ) {
        final ItemStack item = new ItemStack(material, number);
        final ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Object meta is null");

        meta.displayName(Component.text(name));
        if(lore.length > 0) meta.lore(Arrays.asList(lore));
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
    public static void damageItem(final @NotNull LivingEntity entity, final @NotNull ItemStack item, final int amount) {
        if(!(entity instanceof Player player && player.getGameMode() == GameMode.CREATIVE) && item.getAmount() > 0) { // amount <= 0 means that a different thread broke the item. No need to damage it further
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable _meta) {
                if(_meta.getDamage() >= item.getType().getMaxDurability()) {
                    item.setAmount(0);
                    entity.getWorld().playSound(entity.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                }
                else {
                    _meta.setDamage(_meta.getDamage() + amount);
                    item.setItemMeta(_meta);
                }
            }
        }
    }


    /**
     * Returns the display name without any formatting if the item has one.
     * If not, a name is created based on its material and potion effects. This might not perfectly match the Vanilla name and is not translated to the player's selected language.
     * @param item The ItemStack to get the name of
     * @return The display name
     */
    public static String getPlainItemName(final @NotNull ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            Component displayName = meta.displayName();
            if(displayName != null) {
                String plaindDisplayName = PlainTextComponentSerializer.plainText().serialize(displayName);
                if (!plaindDisplayName.isEmpty()) return plaindDisplayName;
            }
        }
        return getVanillaName(item);
    }


    /**
     * Returns the display name as a component if the item has one.
     * If not, a name is created based on its material and potion effects. This might not perfectly match the Vanilla name and is not translated to the player's selected language.
     * @param item The ItemStack to get the name of
     * @return The display name
     */
    public static Component getFancyItemName(final @NotNull ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta != null) {
            Component displayName = meta.displayName();
            if(displayName != null) return displayName;
        }
        return Component.text(getVanillaName(item));
    }


    //TODO cache generated names
    private static @NotNull String getVanillaName(final @NotNull ItemStack item) {
        final String[] words = item.getType().name().split("_");
        final StringBuilder r = new StringBuilder();
        for(String w : words) r.append(w.substring(0, 1).toUpperCase()).append(w.substring(1).toLowerCase()).append(" ");
        return r.toString().strip();
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
