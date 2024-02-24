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
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;




public abstract class IM implements Listener {
    public static final NamespacedKey itemIdKey = new NamespacedKey(ShadowNight.plugin, "customItemId");
    protected final ItemStack defaultItem;


    /**
     * Creates a new Item Manager.
     */
    public IM() {
        defaultItem = ItemUtils.createItemStackCustom(getMaterial(), 1, getDisplayName(), getCustomModelData(), getCustomId().getNumericalValue());
        setItemAttributes();
        createRecipe();
        Bukkit.getServer().getPluginManager().registerEvents(this, ShadowNight.plugin);
    }
    public abstract Material getMaterial();
    public abstract String getDisplayName();
    public abstract int getCustomModelData();
    public abstract CustomItemId getCustomId();

    public abstract double getHitDamage();
    public abstract double getHitKnockbackMultiplier();


    /**
     * Creates a copy of the default ItemStack this CustomItem uses.
     * @return The item stack copy
     */
    public ItemStack createDefaultItemStack() {
        return new ItemStack(defaultItem);
    }


    private void createRecipe() {
        final NamespacedKey recipeKey = new NamespacedKey(ShadowNight.plugin, getCustomId().name());
        final ShapedRecipe shapedRecipe = new ShapedRecipe(recipeKey, defaultItem);

        setRecipe(shapedRecipe);
        Bukkit.addRecipe(shapedRecipe);
    }
    protected abstract void setRecipe(final @NotNull ShapedRecipe recipe);
    protected abstract void setItemAttributes();






    /**
     * Determines what custom item the player is holding and executes interaction callbacks accordingly.
     * @param event The interaction event
     */
    public static void chooseOnInteract(final @NotNull PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item != null && event.getHand() == EquipmentSlot.HAND) {
            Long customItemId = ItemUtils.getCustomItemId(item);
            if (customItemId != null) for (ItemManager itemManager : ItemManager.values()) {
                if(customItemId == itemManager.getInstance().getCustomId().getNumericalValue()) itemManager.getInstance().onInteract(event);
            }
        }
    }

    /**
     * Determines what custom item the player is holding and executes attack callbacks accordingly.
     * @param event The attack event
     */
    public static void chooseOnAttack(final @NotNull EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player player) {
            final ItemStack item = player.getInventory().getItemInMainHand();
            Long customItemId = ItemUtils.getCustomItemId(item);
            if (customItemId != null) for (ItemManager itemManager : ItemManager.values()) {
                if(customItemId == itemManager.getInstance().getCustomId().getNumericalValue()) itemManager.getInstance().onAttack(event);
            }
        }
    }
    protected abstract void onInteract(final @NotNull PlayerInteractEvent event);
    protected abstract void onAttack(final @NotNull EntityDamageByEntityEvent event);
}
