package org.uwu_snek.shadownight.itemFilter;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated._enchantment_overrides;
import org.uwu_snek.shadownight.chatManager.ChatManager;
import org.uwu_snek.shadownight.enchantments.CustomEnchant_Spigot;
import org.uwu_snek.shadownight.utils.Rnd;
import org.uwu_snek.shadownight.utils.UtilityClass;

import java.util.*;




public final class EnchantBlacklist extends UtilityClass implements Rnd {
    public static final List<Enchantment> blockedEnchants = Arrays.asList(Enchantment.SWEEPING_EDGE);
    public static final ArrayList<Enchantment> allowedEnchants = new ArrayList<>();
    static {
        Arrays.stream(CustomEnchant_Spigot.values()).iterator().forEachRemaining(e -> {
            if(!e.isTreasure() && !blockedEnchants.contains(e)) allowedEnchants.add(e);
        });
    }
}
