package org.shadownight.plugin.shadownight.items.bow;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.javatuples.Pair;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.items.IM;
import org.shadownight.plugin.shadownight.items.ItemManager;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;

import java.util.HashMap;
import java.util.UUID;




public abstract class IM_Bow extends IM {
    private static final HashMap<UUID, Pair<IM_Bow, ItemStack>> activeProjectiles = new HashMap<>();


    @Override public double getHitDamage() { return 1; }
    @Override public double getHitKnockbackMultiplier() { return 1; }


    /**
     * Determines what custom item the player is holding and executes shoot callbacks accordingly.
     * @param event The shoot event
     */
    public static void chooseOnShoot(final @NotNull EntityShootBowEvent event) {
        if(event.getEntity() instanceof Player player) {
            final ItemStack item = player.getInventory().getItemInMainHand();
            Long customItemId = ItemUtils.getCustomItemId(item);
            if (customItemId != null) for (ItemManager itemManager : ItemManager.values()) {
                //if(customItemId == itemManager.getInstance().getCustomId().getNumericalValue()) ((IM_Bow)itemManager.getInstance()).onShoot(event);
                if(customItemId == itemManager.getInstance().getCustomId().getNumericalValue()) {
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




    @Override
    protected void setItemAttributes() { }
}
