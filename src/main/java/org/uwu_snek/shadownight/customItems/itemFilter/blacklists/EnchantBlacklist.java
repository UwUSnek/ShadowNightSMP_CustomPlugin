package org.uwu_snek.shadownight.customItems.itemFilter.blacklists;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.enchantments.CustomEnchant_Spigot;
import org.uwu_snek.shadownight.customItems.itemFilter.decorators.Decorator;
import org.uwu_snek.shadownight.utils.Rnd;
import org.uwu_snek.shadownight.utils.UtilityClass;

import java.util.*;




public final class EnchantBlacklist extends UtilityClass implements Rnd {
    public static final List<Enchantment> blockedEnchants = Collections.singletonList(Enchantment.SWEEPING_EDGE);
    public static final ArrayList<Enchantment> eTableWhitelist = new ArrayList<>();
    public static final ArrayList<Enchantment> villagerWhitelist = new ArrayList<>();

    static {
        Arrays.stream(CustomEnchant_Spigot.values()).iterator().forEachRemaining(e -> {
            if (!e.isTreasure() && !blockedEnchants.contains(e)) eTableWhitelist.add(e);
            if (e.isTradeable() && !blockedEnchants.contains(e)) villagerWhitelist.add(e);
        });
    }


    /**
     * Removes any blacklisted enchantment from an item or enchanted book.
     * If an enchanted book only contains blacklisted enchantments, the item is deleted.
     * @param item The item to check
     * @return True if the item has been deleted, false otherwise
     */
    public static boolean fixItemEnchants(final @NotNull ItemStack item) {

        // Remove books that contain one or more of the blocked enchantments
        if (item.getItemMeta() instanceof EnchantmentStorageMeta m) {
            for (Enchantment e : blockedEnchants) m.removeStoredEnchant(e);
            item.setItemMeta(m);
            if(!m.hasStoredEnchants()) {
                item.setAmount(0);
                return true;
            }
        }

        // Remove blocked enchants from normal items
        else {
            for (Enchantment e : blockedEnchants) item.removeEnchantment(e);
        }
        return false;
    }




    /**
     * Rolls a new enchantment. This can be a Vanilla enchant or one of the custom ones that can be obtained from the enchanting table.
     * @param item The item to enchant. This is used to determine what enchants are allowed
     * @return The new enchantment
     */
    public static Enchantment rerollETable(ItemStack item){
        final int chosen = rnd.nextInt(EnchantBlacklist.eTableWhitelist.size() - 1);
        if(item.getType() == Material.BOOK || (EnchantBlacklist.eTableWhitelist.get(chosen).canEnchantItem(item))){
            return EnchantBlacklist.eTableWhitelist.get(chosen);
        }
        else return rerollETable(item);
    }


    /**
     * Rolls a new Librarian Villager book recipe which never contains one of the blacklisted enchantments.
     * @param oldRecipe The old recipe. All the extra data is copied from this one.
     * @return The new recipe containing the modified enchantment and level.
     */
    public static MerchantRecipe rerollVillager(final @NotNull MerchantRecipe oldRecipe){
        // Create new result ItemStack
        final ItemStack newResult = new ItemStack(Material.ENCHANTED_BOOK);
        final EnchantmentStorageMeta meta = (EnchantmentStorageMeta)newResult.getItemMeta();

        // Reroll enchantment and level
        final Enchantment newEnchant = villagerWhitelist.get(rnd.nextInt(villagerWhitelist.size()));
        meta.addStoredEnchant(newEnchant, rnd.nextInt(newEnchant.getMaxLevel()) + 1, true);
        newResult.setItemMeta(meta);
        Decorator.decorate(newResult, false);

        // Return modified copy of the recipe
        final MerchantRecipe newRecipe = new MerchantRecipe(
            new ItemStack(newResult),
            oldRecipe.getUses(),
            oldRecipe.getMaxUses(),
            oldRecipe.hasExperienceReward(),
            oldRecipe.getVillagerExperience(),
            oldRecipe.getPriceMultiplier(),
            oldRecipe.shouldIgnoreDiscounts()
        );
        newRecipe.setIngredients(oldRecipe.getIngredients());
        newRecipe.setSpecialPrice(oldRecipe.getSpecialPrice());
        return newRecipe;
    }
}
