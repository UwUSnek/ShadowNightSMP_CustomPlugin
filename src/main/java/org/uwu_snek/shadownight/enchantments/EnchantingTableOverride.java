package org.uwu_snek.shadownight.enchantments;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentOffer;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated._enchantment_overrides;
import org.uwu_snek.shadownight.customItems.itemFilter.blacklists.EnchantBlacklist;
import org.uwu_snek.shadownight.utils.Rnd;
import org.uwu_snek.shadownight.utils.UtilityClass;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;




public final class EnchantingTableOverride extends UtilityClass implements Rnd {
    private static final HashMap<UUID, HashMap<Integer, Pair<Enchantment, Integer>>> overrides = new HashMap<>();


    /**
     * Detects Enchanting Table preparation events and replaces the client's offers with modified ones that
     * display the overridden name for custom enchantments and don't include the blacklisted ones.
     * This has the side effect of shuffling the enchanting table every time it is opened, differently from Vanilla where
     * a list of offers for a specific item type is kept until one is chosen by the player.
     * This method doesn't replace the enchants on the server. That's onEnchantItem()'s job.
     * @param event The event
     */
    public static void onPrepareItemEnchant(final @NotNull PrepareItemEnchantEvent event){
        if(event.isCancelled()) return;

        // For each offer
        final HashMap<Integer, Pair<Enchantment, Integer>> _overrides = new HashMap<>();
        for(EnchantmentOffer offer : event.getOffers()){
            // Skip empty offers
            if(offer == null) continue;

            // Roll new enchantment
            final Enchantment newEnchant = EnchantBlacklist.rerollETable(event.getItem());
            int lvl = ((offer.getCost() / 30) * newEnchant.getMaxLevel());

            // Calculate level
            /**/ if(lvl < 1 || newEnchant.getMaxLevel() == 1) lvl = 1;
            else if(lvl == newEnchant.getMaxLevel())          lvl = lvl - 1;
            else if(lvl > newEnchant.getMaxLevel())           lvl = newEnchant.getMaxLevel();

            // Save data in the override map and change the offers on the client's screen (changing them doesn't do anything on the server)
            _overrides.put(offer.getCost(), Pair.with(newEnchant, lvl));
            offer.setEnchantment(_enchantment_overrides.getOverride(newEnchant.translationKey()));
            offer.setEnchantmentLevel(lvl);
        }
        overrides.put(event.getEnchanter().getUniqueId(), _overrides);
    }


    /**
     * Detects players putting enchantments on items using an Enchanting Table and replaces them with the actual enchantments in the offer displayed to the client.
     * @param event The event
     */
    public static void onEnchantItem(final @NotNull EnchantItemEvent event) {
        // Get offer overrides
        HashMap<Integer, Pair<Enchantment, Integer>> override = overrides.remove(event.getEnchanter().getUniqueId());
        if(override != null) {

            // Cancel the Vanilla event to enchant the item manually
            event.setCancelled(true);

            // Determine which offer was chosen and enchant the item accordingly
            Pair<Enchantment, Integer> overrideData = override.get(event.getExpLevelCost());
            event.getItem().addEnchantment(overrideData.getValue0(), overrideData.getValue1());

            // Add the additional random enchantments, excluding the blacklisted ones
            int i = -1;
            for(Map.Entry<Enchantment, Integer> e : event.getEnchantsToAdd().entrySet()) {
                ++i;
                if(i == 0) continue; // Skip the first enchant as it is the one that gets rerolled
                if(e.getKey() == overrideData.getValue0()) continue; // Skip enchants of the same type as the rerolled one
                if(overrideData.getValue0().conflictsWith(e.getKey())) continue; // Skip enchants that aren't compatible with the rerolled one
                event.getItem().addEnchantment(e.getKey(), e.getValue());
            }
        }
    }
}
