package org.uwu_snek.shadownight.itemFilter;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.SweepingEdgeEnchantment;
import org.bukkit.Material;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.entity.Player;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated.EnchantmentOverrideCodes;
import org.uwu_snek.shadownight.chatManager.ChatManager;
import org.uwu_snek.shadownight.enchantments.Enchantment_BukkitExtension;
import org.uwu_snek.shadownight.utils.Rnd;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;




public final class EnchantBlacklist extends UtilityClass implements Rnd {
    private static final List<Enchantment> blockedEnchants = Arrays.asList(
        Enchantment.SWEEPING_EDGE,
        Enchantment.DAMAGE_ARTHROPODS,
        Enchantment.DAMAGE_UNDEAD,
        Enchantment.DURABILITY,
        Enchantment.FIRE_ASPECT,
        Enchantment.LOOT_BONUS_MOBS,
        Enchantment.KNOCKBACK
    );
    private static final ArrayList<Enchantment> allowedEnchants = new ArrayList<>();
    static {
        Arrays.stream(Enchantment_BukkitExtension.values()).iterator().forEachRemaining(e -> {
            if(!e.isTreasure() && !blockedEnchants.contains(e)) allowedEnchants.add(e);
        });
    }

    /*.filter(
        e -> !e.isTreasureOnly() &&
        !blockedEnchants.contains(e.getClass())
    ).toList();


/*
    private static final List<Enchantment> notFromTable = Arrays.asList(new Enchantment[]{
        Enchantment.VANISHING_CURSE,
        Enchantment.BINDING_CURSE,
        Enchantment.FROST_WALKER,
        Enchantment.MENDING,
        Enchantment.SOUL_SPEED,
        Enchantment.SWIFT_SNEAK
    });
*/


    public static Enchantment reroll(ItemStack item){
        int chosen = rnd.nextInt(allowedEnchants.size() - 1);
        if(item.getType() == Material.BOOK || (allowedEnchants.get(chosen).canEnchantItem(item))){
            return allowedEnchants.get(chosen);
        }
        else return reroll(item);
    }



    public static void onPrepareItemEnchant(@NotNull final PrepareItemEnchantEvent e){
        boolean replaced = false;
        int i = -1;
        utils.log(Level.WARNING, "opened enchanting table");
        for(EnchantmentOffer offer : e.getOffers()){
            ++i;
            if(offer == null) continue;
            utils.log(Level.SEVERE, "offer " + i + ": " + offer.getEnchantment().getName());

            if(blockedEnchants.contains(offer.getEnchantment())){
                Enchantment newEnchant = reroll(e.getItem());
                utils.log(Level.INFO, "replaced with " + newEnchant.getName());
                int lvl = ((offer.getCost() / 30) * newEnchant.getMaxLevel());

                /**/ if(lvl < 1 || newEnchant.getMaxLevel() == 1) lvl = 1;
                else if(lvl == newEnchant.getMaxLevel())          lvl = lvl - 1;
                else if(lvl > newEnchant.getMaxLevel())           lvl = newEnchant.getMaxLevel();

                //e.getOffers()[i].setEnchantment(newEnchant);

                //e.getOffers()[i].setEnchantment(Registry.ENCHANTMENT.get(EnchantmentOverrideCodes.getOverrideKey(newEnchant.getKey())));
                e.getOffers()[i].setEnchantment(newEnchant);
                ChatManager.broadcast(newEnchant.getKey().toString());
                e.getOffers()[i].setEnchantmentLevel(lvl);
                replaced = true;
            }
        }
        //if(replaced) e.getEnchanter().updateInventory();
    }
}
