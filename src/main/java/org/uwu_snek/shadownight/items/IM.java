package org.uwu_snek.shadownight.items;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
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
import org.uwu_snek.shadownight.items.guiManagers.CustomUpgradeSmithingRecipe;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.Objects;
import java.util.UUID;




public abstract class IM {
    // Basic data
    public static final NamespacedKey itemIdKey = new NamespacedKey(ShadowNight.plugin, "customItemId");
    protected final ItemStack defaultItem;

    // Attack data and infrastructure
    private final long atkCooldown;
    public final ATK attack;
    private static final Table<UUID, Long, Long> last_times = HashBasedTable.create();
    private final boolean replaceVanillaLInteractions;


    // Generated data
    private final Material _generated_material;
    private final int _generated_customModelData;


    // Custom Item properties
    private final CustomItemId customItemId; @SuppressWarnings("unused") public final @NotNull CustomItemId getCustomItemId() { return customItemId; }
    private final String displayName;        @SuppressWarnings("unused") public final @NotNull String getDisplayName()        { return displayName; }
    private final double hitDamage;          @SuppressWarnings("unused") public final double getHitDamage()                   { return hitDamage; }
    private final double kbMultiplier;       @SuppressWarnings("unused") public final double getHitKnockbackMultiplier()      { return kbMultiplier; }
    private final double atkSpeed;           @SuppressWarnings("unused") public final double getAttackSpeed()                 { return atkSpeed; }


    // Upgrade recipe. Null if item is not upgradeable
    protected CustomUpgradeSmithingRecipe upgradeRecipe = null; public final @Nullable CustomUpgradeSmithingRecipe getUpgradeRecipe(){ return upgradeRecipe; }


    // Item abilities
    private Ability abilityL  = null;
    private Ability abilityLS = null;
    private Ability abilityR  = null;
    private Ability abilityRS = null;



    /**
     * Creates a new Item Manager.
     * @param _displayName The display name of the custom item
     * @param _customItemId The CustomItemId to use for this item. This is used to recognize custom items and is saved in the in-game ItemStack
     * @param _attack The attack preset of this item
     * @param _hitDamage The base damage of the item. This is only for melee hits
     * @param _kbMultiplier The knockback multiplier to apply on melee hits
     * @param _atkSpeed The attack speed. This indicates the time between 2 fully charged hits, measured in seconds
     * @param _atkCooldown The minimum time between 2 hits, measured in seconds
     * @param _replaceVanillaLInteractions Whether the item's custom attack should replace the Vanilla LeftClick block and air interactions
     */
    public IM(
        final @NotNull String _displayName,
        final @NotNull CustomItemId _customItemId,
        final @NotNull ATK _attack,
        final double _hitDamage,
        final double _kbMultiplier,
        final double _atkSpeed,
        final double _atkCooldown,
        final boolean _replaceVanillaLInteractions
    ) {
        customItemId = _customItemId;
        Pair<Material, Integer> _generated_data = _custom_model_ids.getMaterialAndModel(customItemId);
        _generated_material =        _generated_data.getValue0();
        _generated_customModelData = _generated_data.getValue1();

        displayName = "§f" + _displayName;
        hitDamage = _hitDamage;
        kbMultiplier = _kbMultiplier;
        atkSpeed = _atkSpeed;
        atkCooldown = (long)(1000 * _atkCooldown);

        defaultItem = ItemUtils.createItemStackCustom(_generated_material, 1, getDisplayName(), _generated_customModelData, customItemId.getNumericalValue());
        initDefaultItemStack();
        _createRecipe();
        attack = _attack;
        replaceVanillaLInteractions = _replaceVanillaLInteractions;
    }
    protected final void setAbilities(
        final @Nullable Ability abilityL_,
        final @Nullable Ability abilityLS,
        final @Nullable Ability abilityR_,
        final @Nullable Ability abilityRS
    ){
        this.abilityL = abilityL_;
        this.abilityLS = abilityLS;
        this.abilityR = abilityR_;
        this.abilityRS = abilityRS;
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
        final Recipe recipe = createRecipe(recipeKey);
        if(recipe != null) Bukkit.addRecipe(recipe); //FIXME use a better method to add custom smithing recipes
    }
    protected abstract Recipe createRecipe(final @NotNull NamespacedKey key);




    public boolean checkCooldown(final @NotNull UUID playerId, final long id) {
        long cur_time = System.currentTimeMillis();
        final Long last_time = last_times.get(playerId, id);
        if (last_time == null || cur_time - last_time > atkCooldown) {
            last_times.put(playerId, id, cur_time);
            return true;
        }
        else return false;
    }

    /**
     * Determines what custom item the player is holding and executes interaction callbacks accordingly.
     * @param event The interaction event
     */
    public static void triggerAbilities(final @NotNull PlayerInteractEvent event) {
        final ItemStack item = event.getItem();
        if (item != null && event.getHand() == EquipmentSlot.HAND) {
            Long customItemId = ItemUtils.getCustomItemId(item);
            if (customItemId != null) {
                IM manager = ItemManager.getValueFromId(customItemId);
                Player player = event.getPlayer();
                if (event.getAction() == Action.LEFT_CLICK_BLOCK  || event.getAction() == Action.LEFT_CLICK_AIR)  {
                    if(manager.replaceVanillaLInteractions) {
                        event.setCancelled(true);
                        if(manager.checkCooldown(player.getUniqueId(), customItemId)) manager.attack.execute(player, null, player.getLocation(), item);
                    }
                    Ability targetAbility = (player.isSneaking() ? manager.abilityLS : manager.abilityL); if(targetAbility != null) targetAbility.activate(player, item, event);
                }
                else if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    Ability targetAbility = (player.isSneaking() ? manager.abilityRS : manager.abilityR); if(targetAbility != null) targetAbility.activate(player, item, event);
                }
            }
        }
    }
}
