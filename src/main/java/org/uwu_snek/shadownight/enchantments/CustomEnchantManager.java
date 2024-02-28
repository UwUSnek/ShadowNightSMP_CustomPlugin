package org.uwu_snek.shadownight.enchantments;

import com.google.common.base.Preconditions;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.enchantments.custom.Reeling;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.utils;

import java.lang.reflect.Field;
import java.util.IdentityHashMap;
import java.util.logging.Level;




public final class CustomEnchantManager extends UtilityClass {
    private static void unfreezeRegistry() {
        try {
            Field fieldL = BuiltInRegistries.ENCHANTMENT.getClass().getDeclaredField("l");
            Field fieldM = BuiltInRegistries.ENCHANTMENT.getClass().getDeclaredField("m");
            fieldL.setAccessible(true);
            fieldM.setAccessible(true);
            fieldL.set(BuiltInRegistries.ENCHANTMENT, false);
            fieldM.set(BuiltInRegistries.ENCHANTMENT, new IdentityHashMap<>());
            fieldL.setAccessible(false);
            fieldM.setAccessible(false);
        }
        catch (NoSuchFieldException | IllegalAccessException | IllegalArgumentException e) {
            throw new RuntimeException(e);
        }
    }

    private static void freezeRegistry() {
        BuiltInRegistries.ENCHANTMENT.freeze();
    }



    private static void registerEnchantment(CustomEnchant e) {
        Registry.register(BuiltInRegistries.ENCHANTMENT, e.id, e);
    }

    public static void registerEnchantments() {
        unfreezeRegistry();
        registerEnchantment(new Reeling());
        freezeRegistry();
    }


    public abstract class CustomEnchantment extends Enchantment {
        public static final Enchantment REELING = getEnchantment("reeling");

        public CustomEnchantment(){}



        private static @NotNull Enchantment getEnchantment(@NotNull String key) {
            NamespacedKey namespacedKey = NamespacedKey.minecraft(key);
            Enchantment enchantment = (Enchantment)org.bukkit.Registry.ENCHANTMENT.get(namespacedKey);
            Preconditions.checkNotNull(enchantment, "No Enchantment found for %s. This is a bug.", namespacedKey);
            return enchantment;
        }
    }
}
