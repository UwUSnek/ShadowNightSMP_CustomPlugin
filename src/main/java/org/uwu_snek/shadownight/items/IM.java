package org.uwu_snek.shadownight.items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight._generated._custom_model_ids;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK;
import org.uwu_snek.shadownight.items.recipeManagers.CustomUpgradeSmithingRecipe;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.Objects;
import java.util.UUID;




public abstract class IM {
    public static final NamespacedKey itemIdKey = new NamespacedKey(ShadowNight.plugin, "customItemId");
    protected final ItemStack defaultItem;
    public final ATK attack;

    private final Material _generated_material;
    private final int _generated_customModelData;

    private final CustomItemId customItemId; @SuppressWarnings("unused") public final @NotNull CustomItemId getCustomItemId() { return customItemId; }
    private final String displayName;        @SuppressWarnings("unused") public final @NotNull String getDisplayName()        { return displayName; }
    private final double hitDamage;          @SuppressWarnings("unused") public final double getHitDamage()                   { return hitDamage; }
    private final double kbMultiplier;       @SuppressWarnings("unused") public final double getHitKnockbackMultiplier()      { return kbMultiplier; }
    private final double atkSpeed;           @SuppressWarnings("unused") public final double getAttackSpeed()                 { return atkSpeed; }

    protected CustomUpgradeSmithingRecipe upgradeRecipe = null; public final @Nullable CustomUpgradeSmithingRecipe getUpgradeRecipe(){ return upgradeRecipe; }


    /**
     * Creates a new Item Manager.
     * @param _displayName The display name of the custom item
     * @param _customItemId The CustomItemId to use for this item. This is used to recognize custom items and is saved in the in-game ItemStack
     * @param _attack The attack preset of this item
     * @param _hitDamage The base damage of the item. This is only for melee hits
     * @param _kbMultiplier The knockback multiplier to apply on melee hits
     * @param _atkSpeed The attack speed. This indicates the time between 2 fully charged hits, measured in seconds
     */
    public IM(final @NotNull String _displayName, final @NotNull CustomItemId _customItemId, final @NotNull ATK _attack, final double _hitDamage, final double _kbMultiplier, final double _atkSpeed) {
        customItemId = _customItemId;
        Pair<Material, Integer> _generated_data = _custom_model_ids.getMaterialAndModel(customItemId);
        _generated_material =        _generated_data.getValue0();
        _generated_customModelData = _generated_data.getValue1();

        displayName = "§f" + _displayName;
        hitDamage = _hitDamage;
        kbMultiplier = _kbMultiplier;
        atkSpeed = _atkSpeed;

        defaultItem = ItemUtils.createItemStackCustom(_generated_material, 1, getDisplayName(), _generated_customModelData, customItemId.getNumericalValue());
        initDefaultItemStack();
        _createRecipe();
        attack = _attack;
    }

    public final @NotNull Material getMaterial() { return _generated_material; }
    public final int getCustomModelData() { return _generated_customModelData; }


    private void initDefaultItemStack() {
        final double playerDefaultAtkSpeed = 4;
        final ItemMeta meta = defaultItem.getItemMeta();

        Objects.requireNonNull(meta, "Object meta is null");
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,  new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", (1 / atkSpeed) - playerDefaultAtkSpeed, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        defaultItem.setItemMeta(meta);
    }




    /**
     * Creates a copy of the default ItemStack this CustomItem uses.
     * @return The item stack copy
     */
    public @NotNull ItemStack createDefaultItemStack() {
        return defaultItem.clone();
    }


    private void _createRecipe() {
        final NamespacedKey recipeKey = new NamespacedKey(ShadowNight.plugin, customItemId.name());
        Recipe recipe = createRecipe(recipeKey);
        if(recipe != null) Bukkit.addRecipe(recipe); //FIXME use a better method to add custom smithing recipes
    }
    protected abstract Recipe createRecipe(final @NotNull NamespacedKey key);






    /**
     * Determines what custom item the player is holding and executes interaction callbacks accordingly.
     * @param event The interaction event
     */
    public static void chooseOnInteract(final @NotNull PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item != null && event.getHand() == EquipmentSlot.HAND) {
            Long customItemId = ItemUtils.getCustomItemId(item);
            if (customItemId != null) for (ItemManager itemManager : ItemManager.values()) {
                if(customItemId == itemManager.getInstance().customItemId.getNumericalValue()) itemManager.getInstance().onInteract(event);
            }
        }
    }
    protected abstract void onInteract(final @NotNull PlayerInteractEvent event);
}
