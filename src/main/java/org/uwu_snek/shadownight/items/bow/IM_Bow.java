package org.uwu_snek.shadownight.items.bow;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_Standard;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.items.ItemManager;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.HashMap;
import java.util.UUID;




public abstract class IM_Bow extends IM {
    private static final HashMap<UUID, Pair<IM_Bow, ItemStack>> activeProjectiles = new HashMap<>();


    public IM_Bow(final @NotNull String _displayName, final @NotNull CustomItemId _customItemId) {
        super(
            _displayName,
            _customItemId,
            new ATK_Standard(),
            1,
            0.5,
            0.5
        );
    }


    /**
     * Determines what custom item the player is holding and executes shoot callbacks accordingly.
     * @param event The shoot event
     */
    public static void chooseOnShoot(final @NotNull EntityShootBowEvent event) {
        if(event.getEntity() instanceof Player player) {
            final ItemStack item = player.getInventory().getItemInMainHand();
            Long customItemId = ItemUtils.getCustomItemId(item);
            if (customItemId != null) for (ItemManager itemManager : ItemManager.values()) {
                if(customItemId == itemManager.getInstance().getCustomItemId().getNumericalValue()) {
                    ((IM_Bow)itemManager.getInstance()).onShoot(event);
                    activeProjectiles.put(event.getProjectile().getUniqueId(), Pair.with((IM_Bow)itemManager.getInstance(), event.getBow()));
                }
            }
        }
    }
    protected abstract void onShoot(final @NotNull EntityShootBowEvent event);


    public static void chooseOnProjectileHit(final @NotNull ProjectileHitEvent event) {
        final Pair<IM_Bow, ItemStack> projectileData = activeProjectiles.get(event.getEntity().getUniqueId());
        if(projectileData != null) {
            activeProjectiles.remove(event.getEntity().getUniqueId());
            event.getEntity().remove();
            projectileData.getValue0().onProjectileHit(event, projectileData.getValue1());
        }
    }
    protected abstract void onProjectileHit(final @NotNull ProjectileHitEvent event, final @NotNull ItemStack usedBow);
}
