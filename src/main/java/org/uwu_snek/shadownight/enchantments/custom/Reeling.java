package org.uwu_snek.shadownight.enchantments.custom;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.attackOverride.CustomKnockback;
import org.uwu_snek.shadownight.enchantments.CustomEnchant;




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
    public boolean canEnchant(@Nullable ItemStack stack) {
        return true;
    }

    @Override public boolean isTreasureOnly() { return false; }
    @Override public boolean isTradeable() { return true; }
    @Override public boolean isDiscoverable() { return true; }

    public static Vector getVelocity(int level) {
        double n = -CustomKnockback.defaultKnockbackXZ + enchantReelingXZ;
        return new Vector(n, 0, n);
    }
}
