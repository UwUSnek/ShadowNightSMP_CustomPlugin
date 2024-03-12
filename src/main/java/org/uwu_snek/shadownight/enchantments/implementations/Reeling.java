package org.uwu_snek.shadownight.enchantments.implementations;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.attackOverride.CustomKnockback;
import org.uwu_snek.shadownight.enchantments.CustomEnchant;
import org.uwu_snek.shadownight.customItems.IM_MeleeWeapon;
import org.uwu_snek.shadownight.customItems.ItemManager;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;




public final class Reeling extends CustomEnchant {
    private static final double enchantReelingXZ = -0.2;

    public Reeling() {
        super(Rarity.COMMON, "Reeling", "reeling", 2);
    }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment other) {
        return other != this && other.getClass() != net.minecraft.world.item.enchantment.KnockbackEnchantment.class;
    }

    @Override
    public boolean canEnchant(@Nullable ItemStack item) {
        if(item != null) {
            final org.bukkit.inventory.ItemStack bukkitItem = item.asBukkitCopy();
            final Long id = ItemUtils.getCustomItemId(bukkitItem);
            if(id != null) {
                return ItemManager.getValueFromId(id) instanceof IM_MeleeWeapon;
            }
            else {
                final Material type = bukkitItem.getType();
                return Tag.ITEMS_SWORDS.isTagged(type);
            }
        }
        else return false;
    }

    @Override public boolean isTreasureOnly() { return false; }
    @Override public boolean isTradeable() { return true; }
    @Override public boolean isDiscoverable() { return true; }

    public static Vector getVelocity(int level) {
        double n = -CustomKnockback.defaultKnockbackXZ + enchantReelingXZ * level;
        return new Vector(n, 0, n);
    }
}
