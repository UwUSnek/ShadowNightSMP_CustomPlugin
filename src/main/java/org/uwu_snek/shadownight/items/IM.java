package org.uwu_snek.shadownight.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight._generated._custom_model_ids;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;




public abstract class IM implements Listener {
    public static final NamespacedKey itemIdKey = new NamespacedKey(ShadowNight.plugin, "customItemId");
    protected final ItemStack defaultItem;
    public final ATK attack;

    private final Material material;
    private final int customModelData;



    /**
     * Creates a new Item Manager.
     */
    public IM(final @NotNull ATK _attack) { //TODO pass parameters to super constructor instead of using get functions for everything
        Pair<Material, Integer> _generated_data = _custom_model_ids.getMaterialAndModel(getCustomId());
        material =        _generated_data.getValue0();
        customModelData = _generated_data.getValue1();

        defaultItem = ItemUtils.createItemStackCustom(material, 1, getDisplayName(), customModelData, getCustomId().getNumericalValue());
        setItemAttributes();
        createRecipe();
        Bukkit.getServer().getPluginManager().registerEvents(this, ShadowNight.plugin);
        attack = _attack;
    }
    public abstract String getDisplayName();
    public abstract CustomItemId getCustomId();

    public final Material getMaterial() { return material; }
    public final int getCustomModelData() { return customModelData; }

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


    protected abstract void onInteract(final @NotNull PlayerInteractEvent event);
}
