package org.uwu_snek.shadownight.items;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;




public class Ability {
    private final boolean cancelOgEvent;
    private final long cooldown;
    private final BiConsumer<Player, ItemStack> onActivate;
    private final HashMap<UUID, Long> last_times;
    private final String name;
    private final List<String> description;

    public final String getName(){ return name; }
    public final List<String> getDescription(){ return description; }
    public final long getCooldown(){ return cooldown; }




    public Ability(final @NotNull String name, final @NotNull String[] description, final boolean cancelOgEvent, final double cooldown, final @NotNull BiConsumer<Player, ItemStack> onActivate) {
        this.name = name;
        this.description = Arrays.asList(description);

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
