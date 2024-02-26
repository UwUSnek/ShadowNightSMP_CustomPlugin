package org.shadownight.plugin.shadownight.utils.spigot;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;





public final class ClaimUtils extends UtilityClass {
    /**
     * Checks if an entity is protected for a certain player.
     * @param entity The entity to check
     * @param player The player
     * @return True if the entity is inside a claim the player has no permission to build in (and thus hit entities), false otherwise
     */
    public static boolean isEntityProtected(@NotNull final Entity entity, @NotNull final Player player) {
        // False if target is a monster
        // False if target is owned by the player
        if (
            entity instanceof Monster ||
            (entity instanceof Tameable tameable && tameable.getOwner() == entity)
        ) {
            return false;
        }

        // Check if player has no permissions. False if they do, true if not
        else {
            Claim claim = GriefPrevention.instance.dataStore.getClaimAt(entity.getLocation(), false, null);
            return claim != null && !claim.hasExplicitPermission(player, ClaimPermission.Build);
        }
    }
}