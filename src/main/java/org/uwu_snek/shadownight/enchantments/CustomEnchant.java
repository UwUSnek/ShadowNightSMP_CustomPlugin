package org.uwu_snek.shadownight.enchantments;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;




public abstract class CustomEnchant extends net.minecraft.world.item.enchantment.Enchantment {
    protected final int max;
    protected final String name;
    protected final String id;

    protected CustomEnchant(final @NotNull Rarity rarity, final @NotNull String _name, final @NotNull String _id, final int maxLv) {
        super(rarity, EnchantmentCategory.BREAKABLE, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
        name = _name;
        id = _id;
        max = maxLv;
    }

    @Override
    public final int getMinLevel() {
        return 1;
    }

    @Override
    public final int getMaxLevel() {
        return max;
    }

    @Override
    public final int getMinCost(final int level) {
        return 1 + level * 10;
    }

    @Override
    public final int getMaxCost(final int level) {
        return this.getMinCost(level) + 5;
    }

    @Override
    public final @NotNull net.minecraft.network.chat.Component getFullname(final int level) {
        MutableComponent mutableComponent = net.minecraft.network.chat.Component.literal(name);
        mutableComponent.withStyle(ChatFormatting.GRAY);

        if (level != 1 || this.getMaxLevel() != 1) {
            mutableComponent.append(CommonComponents.SPACE).append(Component.translatable("enchantment.level." + level));
        }
        return mutableComponent;
    }

    @Override
    protected @NotNull String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("enchantment", BuiltInRegistries.ENCHANTMENT.getKey(this));
        }
        return this.descriptionId;
    }

    @Override protected abstract boolean checkCompatibility(final @NotNull net.minecraft.world.item.enchantment.Enchantment other);
    @Override public abstract boolean canEnchant(@Nullable final ItemStack stack);
    @Override public abstract boolean isTreasureOnly();
    @Override public abstract boolean isTradeable();
    @Override public abstract boolean isDiscoverable();
}
