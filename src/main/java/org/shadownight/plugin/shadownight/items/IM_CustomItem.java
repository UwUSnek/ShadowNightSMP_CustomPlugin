package org.shadownight.plugin.shadownight.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.Objects;
import java.util.logging.Level;


public abstract class IM_CustomItem implements Listener {
    //private static final HashSet<CustomItemId> initializedItems = new HashSet<>();
    public static final NamespacedKey itemIdKey = new NamespacedKey(ShadowNight.plugin, "customItemId");

    protected final ItemStack item;




    public IM_CustomItem() {
        item = utils.createItemStackCustom(getMaterial(), 1, getDisplayName(), getCustomModelData(), getCustomId().getValue());
        setItemAttributes();
        createRecipe();
        Bukkit.getServer().getPluginManager().registerEvents(this, ShadowNight.plugin);
    }
    public abstract Material getMaterial();
    public abstract String getDisplayName();
    public abstract int getCustomModelData();
    public abstract CustomItemId getCustomId();


    /**
     * Creates a copy of the ItemStack this CustomItem uses. Useful if you need the ItemStack properties without the functionality of a CustomItem instance
     * @return The item stack
     */
    public ItemStack createStackCopy() {
        return new ItemStack(item);
    }




    public void createRecipe() {
        NamespacedKey recipeKey = new NamespacedKey(ShadowNight.plugin, getCustomId().name());
        ShapedRecipe shapedRecipe = new ShapedRecipe(recipeKey, item);

        setRecipe(shapedRecipe);
        Bukkit.addRecipe(shapedRecipe);
    }
    protected abstract void setRecipe(ShapedRecipe recipe);
    protected abstract void setItemAttributes();




    private boolean checkUsedItem(ItemStack usedItem) {
        PersistentDataContainer container = Objects.requireNonNull(usedItem.getItemMeta(), "Item meta is null").getPersistentDataContainer();
        Long key = container.get(itemIdKey, PersistentDataType.LONG);
        return key != null && key == getCustomId().getValue();
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void _onInteract(PlayerInteractEvent event) {
        Bukkit.broadcastMessage("BASE INTERACT");
        ItemStack eventItem = event.getItem();
        if(eventItem != null && event.getHand() == EquipmentSlot.HAND && checkUsedItem(eventItem)) { // Check if item is being used in the main hand
            Bukkit.broadcastMessage("DEDICATED INTERACT");
            onInteract(event);
        }
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void _onAttack(EntityDamageByEntityEvent event) {
        if(event.getDamager() instanceof Player player && checkUsedItem(player.getInventory().getItemInMainHand())){
            onAttack(event);
        }
    }
    protected abstract void onInteract(PlayerInteractEvent event);
    protected abstract void onAttack(EntityDamageByEntityEvent event);
}
