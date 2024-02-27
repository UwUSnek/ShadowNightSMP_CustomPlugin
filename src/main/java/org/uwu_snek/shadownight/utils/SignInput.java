package org.uwu_snek.shadownight.utils;



import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;





public final class SignInput {
    private static final Map<Player, SignInput> openSigns = new HashMap<>();
    public static final PacketAdapter packetListener = new PacketAdapter(ShadowNight.plugin, PacketType.Play.Client.UPDATE_SIGN) {
        @Override
        public void onPacketReceiving(PacketEvent event) {
            Player player = event.getPlayer();
            SignInput menu = openSigns.remove(player);
            if (menu == null) return;
            event.setCancelled(true);

            if (!menu.inputCallback.test(player, event.getPacket().getStringArrays().read(0)) && menu.reopenIfFail && !menu.forceClose) {
                // NOTICE: low delays create a loop of closing and opening the sign GUI. 10 should be way more than safe.
                Scheduler.delay(() -> menu.open(player), 10L);
            }
            else Scheduler.delay(() -> {
                if (player.isOnline()) {
                    Location location = menu.posWrapper.toLocation(player.getWorld());
                    player.sendBlockChange(location, location.getBlock().getBlockData());
                    if(menu.afterCloseCallback != null) menu.afterCloseCallback.run();
                }
            }, 2L);
        }
    };



    private final boolean reopenIfFail;
    private final String[] lines;
    private final BiPredicate<Player, String[]> inputCallback;
    private final Runnable afterCloseCallback;

    private BlockPosition posWrapper;
    private boolean forceClose;


    /**
     * Creates a new sign GUI that allows the player to input values
     * @param reopenIfFail True if the GUI has to be re-opened if the player has entered an invalid input
     * @param lines The placeholder lines to display in the sign when it is opened by the player. Must contain exactly 4 strings
     * @param inputCallback Called after the player clicks done or closes the GUI. Has to return true if the input is valid
     * @param afterCloseCallback Called after the GUI is closed (or after a valid input if _reopenIfFail is set to true)
     */
    public SignInput(final boolean reopenIfFail, @Nullable final String @NotNull [] lines, final @NotNull BiPredicate<@NotNull Player, @NotNull String @NotNull []> inputCallback, @Nullable final Runnable afterCloseCallback) {
        this.reopenIfFail = reopenIfFail;
        this.lines = lines;
        this.inputCallback = inputCallback;
        this.afterCloseCallback = afterCloseCallback;
    }


    /**
     * Opens the GUI for the player <player>
     * @param player The target player
     */
    public void open(final @NotNull Player player) {
        if (player.isOnline()) {
            Location location = player.getLocation();
            posWrapper = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

            player.sendBlockChange(location, Material.BIRCH_SIGN.createBlockData());
            player.sendSignChange(location, lines);                     //NOTICE: This only sets the front side of the sign

            PacketContainer openSign = ShadowNight.protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
            openSign.getBlockPositionModifier().write(0, posWrapper);   // Set block position
            openSign.getBooleans().write(0, true);                      // Set side to open (true = front side)
            try {
                ShadowNight.protocolManager.sendServerPacket(player, openSign);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            openSigns.put(player, this);
        }
    }


    /**
     * Closes the GUI for the player <player>
     * @param player The target player
     * @param force True to forcibly close the GUI and ignore the reopenIfFail option specified in the constructor, false to respect it
     */
    public void close(@NotNull Player player, final boolean force) {
        forceClose = force;
        if (player.isOnline()) player.closeInventory();
    }
}
