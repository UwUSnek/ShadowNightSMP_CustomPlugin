package org.shadownight.plugin.shadownight.utils;

import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.ShadowNight;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class utils {
    public static final String serverPrefix = "§d§l[§5§lSN§d§l] §r";
    public static final String serverIp = "shadownight.g-portal.game";

    public static void sendMessage(@NotNull Player player, String message) {
        player.sendMessage(serverPrefix + message);
    }

    public static void newline(@NotNull Player player) {
        player.sendMessage("");
    }

    public static void separator(@NotNull Player player) {
        player.sendMessage("§d§m                                                                               ");
    }


    // Returns the display name if the item has one.
    // If not, a name is created based on its material and potion effects. this might not match vanilla's name
    public static String getItemName(@NotNull ItemStack item){
        ItemMeta meta = item.getItemMeta();
        if(meta != null && meta.hasDisplayName()) return meta.getDisplayName();
        else {
            String[] words = item.getType().name().split("_");
            StringBuilder r = new StringBuilder();
            for(String w : words) r.append(w.substring(0, 1).toUpperCase()).append(w.substring(1).toLowerCase()).append(" ");
            return r.toString();
        }
        //TODO check potion name from their effect data
    }


    /**
     * Checks if the target player is offline and sends an error message to the player <player> if that's the case
     * @param player The player to send error messages to
     * @param target The player to check
     * @param targetName The name of the player to check
     * @return true if target is offline, false otherwise
     */
    public static boolean playerOfflineCheck(@NotNull Player player, @Nullable Player target, String targetName) {
        if (target == null) {
            utils.sendMessage(player, "§cThe player \"" + targetName + "\" is offline or doesn't exist. Did you spell their name correctly?");
            return true;
        }
        return false;
    }





    //TODO actually check if this is called from a thread that isn't the main one
    public static User getOfflineLpUser(UUID uniqueId) {
        UserManager userManager = ShadowNight.lpApi.getUserManager();
        CompletableFuture<User> userFuture = userManager.loadUser(uniqueId);

        return userFuture.join(); // block until the User is loaded
    }

    // Returns the formatted player name with the Server rank as prefix
    public static String getFancyName(Player player) {
        User user = ShadowNight.lpApi.getPlayerAdapter(Player.class).getUser(player);
        return user.getCachedData().getMetaData().getPrefix() + " " + player.getName();
    }

    // Returns the formatted player name with the Server rank as prefix.
    // This works with offline players but is way more expensive.
    // Only use it if you cannot be sure the players is online
    public static String getFancyNameOffline(OfflinePlayer offlinePlayer) {
        User user = getOfflineLpUser(offlinePlayer.getUniqueId());
        return user.getCachedData().getMetaData().getPrefix() + " " + offlinePlayer.getName();
    }









    // Returns the time duration in a readable format
    // e.g. 17d15h13m5s
    public static String sToDuration(Long s, boolean useSpaces) {
        return sToDuration(s, useSpaces ? " " : "");
    }

    private static String sToDuration(Long s, String c) {
        return
            (s >= 86400 ? (s / 86400      + "d" + c) : "") +
            (s >= 3600  ? (s / 3600  % 24 + "h" + c) : "") +
            (s >= 60    ? (s / 60    % 60 + "m" + c) : "") +
            s                        % 60 + "s"
        ;
    }





    public static String stripPrivateCharacters(String s) {
        return s.replaceAll("[\uE000-\uF8FF]", "");
    }

    public static String stripColor(String s) {
        return s.replaceAll("[&§]([0-9a-fk-or])", "");
    }

    public static String translateColor(String s) {
        return s.replaceAll("&([0-9a-fk-or])", "§$1");
    }





    // Creates an item with a custom name and lore
    public static ItemStack createItemStack(final Material material, final int number, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, number);
        final ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Object meta is null");

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }





    public static String getGroupDisplayName(Player player) {
        return Objects.requireNonNull(ShadowNight.lpApi.getGroupManager().getGroup(
            Objects.requireNonNull(ShadowNight.lpApi.getPlayerAdapter(Player.class)
               .getUser(player)
               .getCachedData()
               .getMetaData()
               .getPrimaryGroup()
            , "Luckperms primary group id is null")
        ), "Luckperms group object is null").getDisplayName();
    }

    // way slower //TODO documentation
    public static String getGroupDisplayNameOffline(OfflinePlayer player) {
        UserManager userManager = ShadowNight.lpApi.getUserManager();
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
