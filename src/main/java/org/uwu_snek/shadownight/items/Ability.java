package org.uwu_snek.shadownight.items;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.BiConsumer;




public class Ability {
    private final boolean cancelOgEvent;
    private final long cooldown;
    private final BiConsumer<Player, ItemStack> onActivate;
    private final HashMap<UUID, Long> last_times;


    public Ability(final boolean cancelOgEvent, final double cooldown, final @NotNull BiConsumer<Player, ItemStack> onActivate) {
        this.cancelOgEvent = cancelOgEvent;
        this.cooldown = (long)(cooldown * 1000);
        this.onActivate = onActivate;
        last_times = cooldown == 0 ? null : new HashMap<>();
    }


    public void activate(final @NotNull Player owner, final @NotNull ItemStack item, final @NotNull Cancellable cancellableEvent){
        if(cancelOgEvent) cancellableEvent.setCancelled(true);

        // Fast no-cooldown logic
        if(last_times == null) {
            onActivate.accept(owner, item);
        }

        // Slower cooldown management logic
        else {
            long cur_time = System.currentTimeMillis();
            UUID ownerId = owner.getUniqueId();
            final Long last_time = last_times.get(ownerId);
            if (last_time == null || cur_time - last_time > cooldown) {
                last_times.put(ownerId, cur_time);
                onActivate.accept(owner, item);
            }
        }
    }
}
