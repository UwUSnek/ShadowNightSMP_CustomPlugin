package org.uwu_snek.shadownight.utils.spigot;

import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight.utils.UtilityClass;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;




public final class PlayerUtils extends UtilityClass {
    /**
     * Checks if the target player is offline and sends an error message to the player <player> if that's the case
     * @param player The player to send error messages to
     * @param target The player to check
     * @param targetName The name of the player to check
     * @return true if target is offline, false otherwise
     */
    public static boolean playerOfflineCheck(final @NotNull Player player, @Nullable final Player target, final @NotNull String targetName) {
        if (target == null) {
            ChatUtils.sendMessage(player, "§cThe player \"" + targetName + "\" is offline or doesn't exist. Did you spell their name correctly?");
            return true;
        }
        return false;
    }





    //TODO actually check if this is called from a thread that isn't the main one
    /**
     * Retrieves the LuckPerms user of an offline player.
     * @param uniqueId The UUID of the player. Must be a valid UUID
     * @return The LuckPerms user
     */
    public static @NotNull User getOfflineLpUser(final @NotNull UUID uniqueId) {
        UserManager userManager = ShadowNight.lpApi.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uniqueId);

        return userFuture.join(); // block until the User is loaded
    }

    /**
     * Returns the formatted name of a player with the Server rank as prefix
     * @param player The player
     * @return The formatted name
     */
    public static @NotNull String getFancyName(final @NotNull Player player) {
        User user = ShadowNight.lpApi.getPlayerAdapter(org.bukkit.entity.Player.class).getUser(player);
        return user.getCachedData().getMetaData().getPrefix() + " " + player.getName();
    }

    /**
     * Returns the formatted name of an offline player with the Server rank as prefix.
     * This works with offline players but is way more expensive.
     * Only use it if you cannot be sure the players is online
     * @param offlinePlayer The offline player
     * @return The formatted name
     */
    public static @NotNull String getFancyNameOffline(final @NotNull OfflinePlayer offlinePlayer) {
        User user = getOfflineLpUser(offlinePlayer.getUniqueId());
        return user.getCachedData().getMetaData().getPrefix() + " " + offlinePlayer.getName();
    }





    public static @Nullable String getGroupDisplayName(final @NotNull org.bukkit.entity.Player player) {
        return Objects.requireNonNull(ShadowNight.lpApi.getGroupManager().getGroup(
            Objects.requireNonNull(ShadowNight.lpApi.getPlayerAdapter(org.bukkit.entity.Player.class)
                    .getUser(player)
                    .getCachedData()
                    .getMetaData()
                    .getPrimaryGroup()
                , "Luckperms primary group id is null")
        ), "Luckperms group object is null").getDisplayName();
    }

    // way slower //TODO documentation
    public static @Nullable String getGroupDisplayNameOffline(final @NotNull OfflinePlayer player) {
        final UserManager userManager = ShadowNight.lpApi.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(player.getUniqueId());

        return Objects.requireNonNull(ShadowNight.lpApi.getGroupManager().getGroup(
            Objects.requireNonNull(userFuture.join()
                    .getCachedData()
                    .getMetaData()
                    .getPrimaryGroup()
                , "Luckperms primary group id is null")
        ), "Luckperms group object is null").getDisplayName();
    }


    /**
     * Returns the first block that doesn't have the BlockProperty <property>
     * @param initialPos The initial position and direction
     * @param property The property to check for
     * @param dist The maximum distance from the initial position
     * @return The target block, or null if none was found
     */
    public static @Nullable Block getTargetBlock(final @NotNull Location initialPos, final @NotNull Function<@NotNull Material, @NotNull Boolean> property, final int dist) {
        final Vector dir = initialPos.getDirection();
        for(double i = 0d; i < dist; i += 0.25d) {
            Block block = initialPos.getWorld().getBlockAt(initialPos.clone().add(dir.clone().multiply(i)));
            if(!property.apply(block.getType())) return block;
        }
        return null;
    }
}
