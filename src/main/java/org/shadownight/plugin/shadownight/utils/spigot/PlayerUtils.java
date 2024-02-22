package org.shadownight.plugin.shadownight.utils.spigot;

import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.ShadowNight;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;




public class PlayerUtils {
    /**
     * Checks if the target player is offline and sends an error message to the player <player> if that's the case
     * @param player The player to send error messages to
     * @param target The player to check
     * @param targetName The name of the player to check
     * @return true if target is offline, false otherwise
     */
    public static boolean playerOfflineCheck(final @NotNull org.bukkit.entity.Player player, @Nullable final org.bukkit.entity.Player target, final @NotNull String targetName) {
        if (target == null) {
            ChatUtils.sendMessage(player, "§cThe player \"" + targetName + "\" is offline or doesn't exist. Did you spell their name correctly?");
            return true;
        }
        return false;
    }





    //TODO actually check if this is called from a thread that isn't the main one
    public static User getOfflineLpUser(final @NotNull UUID uniqueId) {
        UserManager userManager = ShadowNight.lpApi.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uniqueId);

        return userFuture.join(); // block until the User is loaded
    }

    // Returns the formatted player name with the Server rank as prefix
    public static String getFancyName(final @NotNull org.bukkit.entity.Player player) {
        User user = ShadowNight.lpApi.getPlayerAdapter(org.bukkit.entity.Player.class).getUser(player);
        return user.getCachedData().getMetaData().getPrefix() + " " + player.getName();
    }

    // Returns the formatted player name with the Server rank as prefix.
    // This works with offline players but is way more expensive.
    // Only use it if you cannot be sure the players is online
    public static String getFancyNameOffline(final @NotNull OfflinePlayer offlinePlayer) {
        User user = getOfflineLpUser(offlinePlayer.getUniqueId());
        return user.getCachedData().getMetaData().getPrefix() + " " + offlinePlayer.getName();
    }





    public static String getGroupDisplayName(final @NotNull org.bukkit.entity.Player player) {
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
    public static String getGroupDisplayNameOffline(final @NotNull OfflinePlayer player) {
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
}
