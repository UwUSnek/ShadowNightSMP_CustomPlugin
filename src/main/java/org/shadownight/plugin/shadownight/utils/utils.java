package org.shadownight.plugin.shadownight.utils;

import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.ShadowNight;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

import static org.shadownight.plugin.shadownight.items.IM_CustomItem.itemIdKey;


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

        meta.setDisplayName("§f" + name);
        if(lore.length > 0) meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);
        return item;
    }

    // Creates an item with a custom name, model, id and lore
    public static ItemStack createItemStackCustom(
        final Material material,
        final int number,
        final String name,
        final int customModelData,
        final long customItemId,
        final String... lore
    ) {
        final ItemStack item = new ItemStack(material, number);
        final ItemMeta meta = Objects.requireNonNull(item.getItemMeta(), "Object meta is null");

        meta.setDisplayName("§f" + name);
        if(lore.length > 0) meta.setLore(Arrays.asList(lore));
        meta.setCustomModelData(customModelData);

        final PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(itemIdKey, PersistentDataType.LONG, customItemId);

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


    public static boolean doubleEquals(double n, double target, double threshold) {
        return !(n < target - threshold || n > target + threshold);
    }


    /**
     * Checks if a coordinate is inside a cone
     * @param pos The origin of the cone
     * @param direction The direction of the cone. Has to be a normalized Vector
     * @param targetPos The target coordinate to check
     * @param viewRadius The starting radius of the cone
     * @return true if <targetPos> is within a cone positioned in <pos> oriented in the direction <direction>, false otherwise
     */
    public static boolean isInCone(final Vector pos, final Vector direction, final Vector targetPos, double viewRadius) {
        //Vector normDirection = direction.clone().normalize();
        Vector relativeTargetPos = targetPos.clone().subtract(pos);
        double viewAxisDis = relativeTargetPos.clone().dot(direction);

        if (viewAxisDis < 0.0f) return false;
        else return relativeTargetPos.getCrossProduct(direction).length() <= viewRadius;
    }




    public static double modulus(final Vector v) {
        return Math.sqrt(v.clone().dot(v));
    }


    public static double distToLine(final Vector l1, final Vector l2, final Vector p) {
        final Vector ab  = l2.clone().subtract(l1);
        final Vector av  =  p.clone().subtract(l1);

        if (av.dot(ab) <= 0.0)           // Point is lagging behind start of the segment, so perpendicular distance is not viable.
            return modulus(av) ;         // Use distance to start of segment instead.

        final Vector bv = p.clone().subtract(l2);

        if (bv.dot(ab) >= 0.0)           // Point is advanced past the end of the segment, so perpendicular distance is not viable.
            return modulus(bv);         // Use distance to end of the segment instead.

        return modulus(ab.getCrossProduct(av)) / modulus(ab);
    }


    public static void damageItem(Player player, ItemStack item) {
        if(player.getGameMode() != GameMode.CREATIVE && item.getAmount() > 0) { // amount <= 0 means that a different thread broke the item. No need to damage it further
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof Damageable _meta) {
                if(_meta.getDamage() >= item.getType().getMaxDurability()) {
                    item.setAmount(0);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                }
                else {
                    _meta.setDamage(_meta.getDamage() + 1);
                    item.setItemMeta(_meta);
                }
            }
        }
    }



    public static void log(Level level, String message) {
        ShadowNight.plugin.getLogger().log(level, message);
    }



    public static byte[] imageToByteArray(BufferedImage image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", stream);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return stream.toByteArray();
        // ByteArrayOutputStreams don't need to be closed (the documentation says so)
    }


    private static long[] createLookupTable() {
        long[] byteTable = new long[256];
        long h = 0x544B2FBACAAF1684L;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 31; j++) {
                h = (h >>> 7) ^ h;
                h = (h << 11) ^ h;
                h = (h >>> 10) ^ h;
            }
            byteTable[i] = h;
        }
        return byteTable;
    }


    public static long createHash64(byte[] data) {
        long h = 0xBB40E64DA205B064L;
        final long hmult = 7664345821815920749L;
        final long[] ht = createLookupTable();
        for (int len = data.length, i = 0; i < len; i++) {
            h = (h * hmult) ^ ht[data[i] & 0xff];
        }
        return h;
    }
}