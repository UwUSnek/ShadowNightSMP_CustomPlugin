package org.shadownight.plugin.shadownight.attackOverride.attacks;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;




public final class ATK_Standard extends ATK {
    @Override
    public void execute(@NotNull final LivingEntity damager, @Nullable final LivingEntity directTarget, @Nullable final ItemStack item) {
        if(directTarget != null) executeBasicAttack(damager, directTarget, item, getEntityCharge(damager), true);
    }
}
