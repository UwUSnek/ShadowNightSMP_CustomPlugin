package org.uwu_snek.shadownight.itemFilter;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;




public final class EnchantBlacklist extends UtilityClass {

    public static void onPrepareEnchant(@NotNull final PrepareItemEnchantEvent e) {
        for(EnchantmentOffer offer : e.getOffers()){
            if(offer.getEnchantment() != Enchantment.DAMAGE_ALL) {
                e.setCancelled(false);
                break;
            }
        }
    }
}
