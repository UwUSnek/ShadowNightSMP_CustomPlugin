package org.uwu_snek.shadownight.enchantments;

import com.google.common.base.Preconditions;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;




public abstract class CustomEnchant_Spigot extends Enchantment {
    public static final Enchantment REELING = getEnchantment("reeling");


    private static @NotNull Enchantment getEnchantment(@NotNull final String key) {
        NamespacedKey namespacedKey = NamespacedKey.minecraft(key);
        Enchantment enchantment = org.bukkit.Registry.ENCHANTMENT.get(namespacedKey);
        Preconditions.checkNotNull(enchantment, "No Enchantment found for %s. This is a bug.", namespacedKey);
        return enchantment;
    }
}
