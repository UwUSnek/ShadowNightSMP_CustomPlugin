package org.shadownightsmp.plugin.shadownightsmp.utils;



import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.shadownightsmp.plugin.shadownightsmp.ShadowNightSMP;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;





public final class SignInput {
    private static final Map<Player, SignInput> openSigns = new HashMap<>();
    public static final PacketAdapter packetListener = new PacketAdapter(ShadowNightSMP.plugin, PacketType.Play.Client.UPDATE_SIGN) {
        @Override
        public void onPacketReceiving(PacketEvent event) {
            Player player = event.getPlayer();
            SignInput menu = openSigns.remove(player);
            if (menu == null) return;
            event.setCancelled(true);

            if (!menu.response.test(player, event.getPacket().getStringArrays().read(0)) && menu.reopenIfFail && !menu.forceClose) {
                // NOTICE: low delays create a loop of closing and opening the sign GUI. 10 should be way more than safe.
                Bukkit.getScheduler().runTaskLater(ShadowNightSMP.plugin, () -> menu.open(player), 10L);
            }
            Bukkit.getScheduler().runTaskLater(ShadowNightSMP.plugin, () -> {
                if (player.isOnline()) {
                    Location location = menu.posWrapper.toLocation(player.getWorld());
                    player.sendBlockChange(location, location.getBlock().getBlockData());
                }
            }, 2L);
        }
    };



    private final BiPredicate<Player, String[]> response;
    private final boolean reopenIfFail;
    private BlockPosition posWrapper;
    private boolean forceClose;


    public SignInput(boolean value, BiPredicate<Player, String[]> response) {
        this.reopenIfFail = value;
        this.response = response;
    }


    public void open(Player player) {
        Objects.requireNonNull(player, "player");
        if (!player.isOnline()) {
            return;
        }
        Location location = player.getLocation();
        posWrapper = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

        player.sendBlockChange(location, Material.OAK_SIGN.createBlockData());
        player.sendSignChange(location, new String[]{"a", "b", "c", "d"});  //NOTICE: This only sets the front side of the sign

        PacketContainer openSign = ShadowNightSMP.protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        openSign.getBlockPositionModifier().write(0, posWrapper);           // Set block position
        openSign.getBooleans().write(0, true);                              // Set side to open (true = front side)
       try {
           ShadowNightSMP.protocolManager.sendServerPacket(player, openSign);
       } catch (InvocationTargetException exception) {
           exception.printStackTrace();
       }
        openSigns.put(player, this);
    }


    public void close(Player player, boolean force) {
        this.forceClose = force;
        if (player.isOnline()) {
            player.closeInventory();
        }
    }
}