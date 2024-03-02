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
    private static final List<Enchantment> blockedEnchants = Arrays.asList(Enchantment.SWEEPING_EDGE);
    private static final ArrayList<Enchantment> allowedEnchants = new ArrayList<>();
    static {
        Arrays.stream(CustomEnchant_Spigot.values()).iterator().forEachRemaining(e -> {
            if(!e.isTreasure() && !blockedEnchants.contains(e)) allowedEnchants.add(e);
        });
    }

    private static final HashMap<UUID, HashMap<Integer, Pair<Enchantment, Integer>>> overrides = new HashMap<>();


    /**
     * Rolls a new enchantment. This can be a Vanilla enchant or one of the custom ones that can be obtained from the enchanting table.
     * @param item The item to enchant. This is used to determine what enchants are allowed
     * @return The new enchantment
     */
    public static Enchantment reroll(ItemStack item){
        int chosen = rnd.nextInt(allowedEnchants.size() - 1);
        if(item.getType() == Material.BOOK || (allowedEnchants.get(chosen).canEnchantItem(item))){
            return allowedEnchants.get(chosen);
        }
        else return reroll(item);
    }



    public static void onPrepareItemEnchant(@NotNull final PrepareItemEnchantEvent e){
        if(e.isCancelled()) return;

        // For each offer
        HashMap<Integer, Pair<Enchantment, Integer>> _overrides = new HashMap<>();
        for(EnchantmentOffer offer : e.getOffers()){
            // Skip empty offers
            if(offer == null) continue;

            // Roll new enchantment
            Enchantment newEnchant = reroll(e.getItem());
            int lvl = ((offer.getCost() / 30) * newEnchant.getMaxLevel());

            // Calculate level
            /**/ if(lvl < 1 || newEnchant.getMaxLevel() == 1) lvl = 1;
            else if(lvl == newEnchant.getMaxLevel())          lvl = lvl - 1;
            else if(lvl > newEnchant.getMaxLevel())           lvl = newEnchant.getMaxLevel();

            // Save data in the override map and change the offers on the client's screen (changing them doesn't do anything on the server)
            _overrides.put(offer.getCost(), Pair.with(newEnchant, lvl));
            offer.setEnchantment(_enchantment_overrides.getOverride(newEnchant.translationKey()));
            ChatManager.broadcast("" + _enchantment_overrides.getOverride(newEnchant.translationKey()));
            offer.setEnchantmentLevel(lvl);
        }
        overrides.put(e.getEnchanter().getUniqueId(), _overrides);
    }



    public static void onEnchantItem(@NotNull final EnchantItemEvent event) {
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
