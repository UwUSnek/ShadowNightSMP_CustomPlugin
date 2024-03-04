package org.uwu_snek.shadownight.utils;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;




public final class ResetPotionEffect extends UtilityClass {
    /**
     * Applies a potion effect to the target entity, removing the old effect if needed.
     * Effects of the same type but with different amplifier are not removed.
     * @param target The target entity
     * @param type The type of the potion effect
     * @param duration The duration of the effect expressed in ticks
     * @param amplifier The amplifier of the effect (level 1 = 0)
     */
    public static void reset(final @NotNull LivingEntity target, final @NotNull PotionEffectType type, final int duration, final int amplifier) {
        if(target.hasPotionEffect(type)) {
            PotionEffect effect = target.getPotionEffect(type);
            if(effect != null && effect.getDuration() <= duration && amplifier == effect.getAmplifier()) target.removePotionEffect(type);
        }
        target.addPotionEffect(new PotionEffect(type, duration, amplifier));
    }
}
