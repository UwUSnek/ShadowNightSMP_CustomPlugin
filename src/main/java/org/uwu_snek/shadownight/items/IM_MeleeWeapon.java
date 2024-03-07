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
import org.uwu_snek.shadownight._generated._custom_item_data;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK;
import org.uwu_snek.shadownight.items.guiManagers.CustomUpgradeSmithingRecipe;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.Objects;
import java.util.UUID;




public abstract class IM_MeleeWeapon extends IM {

    /**
     * Creates a new Item Manager.
     *
     * @param _displayName                 The display name of the custom item
     * @param _customItemId                The CustomItemId to use for this item. This is used to recognize custom items and is saved in the in-game ItemStack
     * @param _attack                      The attack preset of this item
     * @param _hitDamage                   The base damage of the item. This is only for melee hits
     * @param _kbMultiplier                The knockback multiplier to apply on melee hits
     * @param _atkSpeed                    The attack speed. This indicates the time between 2 fully charged hits, measured in seconds
     * @param _atkCooldown                 The minimum time between 2 hits, measured in seconds
     * @param _replaceVanillaLInteractions Whether the item's custom attack should replace the Vanilla LeftClick block and air interactions
     */
    public IM_MeleeWeapon(@NotNull String _displayName, @NotNull _custom_item_id _customItemId, @NotNull ATK _attack, double _hitDamage, double _kbMultiplier, double _atkSpeed, double _atkCooldown, boolean _replaceVanillaLInteractions) {
        super(_displayName, _customItemId, _attack, _hitDamage, _kbMultiplier, _atkSpeed, _atkCooldown, _replaceVanillaLInteractions);
    }
}
