package org.uwu_snek.shadownight.itemFilter;

import io.papermc.paper.enchantments.EnchantmentRarity;
import kotlin.jvm.internal.Reflection;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.EntityCategory;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;




public final class EnchantBlacklist extends UtilityClass {/*
    static Enchantment test = new Enchantment() {
        @NotNull
        @Override
        public String getName() {
            return "TEST ENCHANT";
        }

        @Override
        public int getMaxLevel() {
            return 5;
        }

        @Override
        public int getStartLevel() {
            return 0;
        }

        @NotNull
        @Override
        public EnchantmentTarget getItemTarget() {
            return EnchantmentTarget.WEAPON;
        }

        @Override
        public boolean isTreasure() {
            return false;
        }

        @Override
        public boolean isCursed() {
            return false;
        }

        @Override
        public boolean conflictsWith(@NotNull Enchantment enchantment) {
            return false;
        }

        @Override
        public boolean canEnchantItem(@NotNull ItemStack itemStack) {
            return true;
        }

        @NotNull
        @Override
        public NamespacedKey getKey() {
            return new NamespacedKey("minecraft", "testenchant");
        }
    };*/

    public static void onPrepareEnchant(@NotNull final PrepareItemEnchantEvent e) {
        //EnchantmentOffer[] offers = new EnchantmentOffer[] {
        //    new EnchantmentOffer(Enchantment.DAMAGE_ALL, 255, 1),
        //    new EnchantmentOffer(Enchantment.DAMAGE_ALL, 255, 1),
        //    new EnchantmentOffer(Enchantment.DAMAGE_ALL, 255, 1)
        //};
        for(EnchantmentOffer offer : e.getOffers()){
            //if(offer.getEnchantment() == Enchantment.SWEEPING_EDGE) {
            if(offer.getEnchantment() != Enchantment.DAMAGE_ALL) {
                e.setCancelled(false);
                Bukkit.broadcastMessage(offer.getEnchantment().getKey().toString());
                //Scheduler.delay(() -> Bukkit.getPluginManager().callEvent(new PrepareItemEnchantEvent(e.getEnchanter(), e.getView(), e.getEnchantBlock(), e.getItem(), offers, e.getEnchantmentBonus())), 5L);
                //e.getOffers()[0] = new EnchantmentOffer(test, 3, 1);
                break;
            }
        }
    }
}
