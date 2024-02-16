package org.shadownight.plugin.shadownight.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.Objects;





public abstract class IM_CustomItem implements Listener {
    public static final NamespacedKey itemIdKey = new NamespacedKey(ShadowNight.plugin, "customItemId");
    protected final ItemStack item;


    /**
     * Creates a new Item Manager.
     */
    public IM_CustomItem() {
        item = utils.createItemStackCustom(getMaterial(), 1, getDisplayName(), getCustomModelData(), getCustomId().getNumericalValue());
        setItemAttributes();
        createRecipe();
        Bukkit.getServer().getPluginManager().registerEvents(this, ShadowNight.plugin);
    }
    public abstract Material getMaterial();
    public abstract String getDisplayName();
    public abstract int getCustomModelData();
    public abstract CustomItemId getCustomId();


    /**
     * Creates a copy of the ItemStack this CustomItem uses.
     * @return The item stack copy
     */
    public ItemStack createStackCopy() {
        return new ItemStack(item);
    }


    private void createRecipe() {
        final NamespacedKey recipeKey = new NamespacedKey(ShadowNight.plugin, getCustomId().name());
        final ShapedRecipe shapedRecipe = new ShapedRecipe(recipeKey, item);

        setRecipe(shapedRecipe);
        Bukkit.addRecipe(shapedRecipe);
    }
    protected abstract void setRecipe(@NotNull final ShapedRecipe recipe);
    protected abstract void setItemAttributes();





    private static Long getCustomItemId(@NotNull final ItemStack usedItem) {
        PersistentDataContainer container = Objects.requireNonNull(usedItem.getItemMeta(), "Item meta is null").getPersistentDataContainer();
        return container.get(IM_CustomItem.itemIdKey, PersistentDataType.LONG);
    }

    /**
     * Determines what custom item the player is holding and executes interaction callbacks accordingly.
     * @param event The interaction event
     */
    public static void chooseOnInteract(@NotNull final PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null && event.getHand() == EquipmentSlot.HAND) {
            Long customItemId = getCustomItemId(item);
            if (customItemId != null) for (ItemManager itemManager : ItemManager.values()) {
                if(customItemId == itemManager.getInstance().getCustomId().getNumericalValue()) itemManager.getInstance().onInteract(event);
            }
        }
    }

    /**
     * Determines what custom item the player is holding and executes attack callbacks accordingly.
     * @param event The attack event
     */
    public static void chooseOnAttack(@NotNull final EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player player) {
            ItemStack item = player.getInventory().getItemInMainHand();
            Long customItemId = getCustomItemId(item);
            if (customItemId != null) for (ItemManager itemManager : ItemManager.values()) {
                if(customItemId == itemManager.getInstance().getCustomId().getNumericalValue()) itemManager.getInstance().onAttack(event);
            }
        }
    }
    protected abstract void onInteract(@NotNull final PlayerInteractEvent event);
    protected abstract void onAttack(@NotNull final EntityDamageByEntityEvent event);
}
