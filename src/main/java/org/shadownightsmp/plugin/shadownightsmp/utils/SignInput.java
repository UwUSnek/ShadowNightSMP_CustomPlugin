package org.shadownightsmp.plugin.shadownightsmp.utils;

/*
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.PacketType;

import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.data.BlockData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.SignChangeEvent;
import org.shadownightsmp.plugin.shadownightsmp.QOL.StarterKit;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.shadownightsmp.plugin.shadownightsmp.ShadowNightSMP;


import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

import static org.bukkit.Bukkit.getServer;


public class SignInput implements Listener {
    private static final int ACTION_INDEX = 9;
    Material oldBlockType;
    BlockData oldBlockData;
    Block block;


    private final HashMap<Player, SignInput> inputs = new HashMap<>();




    public SignInput(){
        getServer().getPluginManager().registerEvents(this, ShadowNightSMP.plugin);
    }



    public void open(Player player){
        //player.sendBlockChange(pos, Material.OAK_SIGN.createBlockData());
        // Get info
        Location playerPos = player.getLocation();
        World w = player.getWorld();
        Location pos = new Location(w, playerPos.getX(), playerPos.getY(), playerPos.getZ());

        //Save current block and create sign for the player to open
        block = w.getBlockAt(pos);
        //oldBlockType = block.getType();
        //oldBlockData = block.getBlockData();
        //block.setType(Material.OAK_SIGN); //TODO check if this copies the data from the old block

        player.sendBlockChange(pos, Material.OAK_SIGN.createBlockData());

        // Place the old block back for the client
        //block.setType(oldBlockType);
        //block.setBlockData(oldBlockData);


        // Open the sign
        //BlockState block = pos.getBlock().getState();
        //player.openSign(((Sign) block));
        BlockPosition posWrapper = new BlockPosition(pos.getBlockX(), pos.getBlockY(), pos.getBlockZ());
        PacketContainer openSign = ShadowNightSMP.protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        PacketContainer signData = ShadowNightSMP.protocolManager.createPacket(PacketType.Play.Server.TILE_ENTITY_DATA);
        openSign.getBlockPositionModifier().write(0, posWrapper);
        NbtCompound signNBT = (NbtCompound) signData.getNbtModifier().read(0);
        
        //for (int line = 0; line < SIGN_LINES; line++) {
        //    signNBT.put("Text" + (line + 1), this.text.size() > line ? String.format(NBT_FORMAT, color(this.text.get(line))) : "");
        //}
        signNBT.put("x", posWrapper.getX());
        signNBT.put("y", posWrapper.getY());
        signNBT.put("z", posWrapper.getZ());
        signNBT.put("id", "minecraft:oak_sign");


        signData.getBlockPositionModifier().write(0, posWrapper);
        //signData.getIntegers().write(0, ACTION_INDEX);
        signData.getNbtModifier().write(0, signNBT);

        try {
            ShadowNightSMP.protocolManager.sendServerPacket(player, signData);
            ShadowNightSMP.protocolManager.sendServerPacket(player, openSign);
        } catch (InvocationTargetException exception) {
            exception.printStackTrace();
        }
        inputs.put(player, this);

        
        //fakeExplosion.getDoubles()
        //    .write(0, player.getLocation().getX())
        //    .write(1, player.getLocation().getY())
        //    .write(2, player.getLocation().getZ());
        //fakeExplosion.getFloat().write(0, 3.0F);
        //fakeExplosion.getBlockPositionCollectionModifier().write(0, new ArrayList<>());
        //fakeExplosion.getVectors().write(0, player.getVelocity().add(new Vector(1, 1, 1)));
//
        //protocolManager.sendServerPacket(player, fakeExplosion);
    }







    public SignInput response(BiPredicate<Player, String[]> response) {
        this.response = response;
        return this;
    }




    private void listen() {
        ShadowNightSMP.protocolManager.addPacketListener(new PacketAdapter(ShadowNightSMP.plugin, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                SignInput menu = inputs.remove(player);

                if (menu == null) {
                    return;
                }
                event.setCancelled(true);

                boolean success = menu.response.test(player, event.getPacket().getStringArrays().read(0));

                if (!success && menu.reopenIfFail && !menu.forceClose) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> menu.open(player), 2L);
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    if (player.isOnline()) {
                        Location location = menu.position.toLocation(player.getWorld());
                        player.sendBlockChange(location, location.getBlock().getBlockData());
                    }
                }, 2L);
            }
        });
    }





    @EventHandler(priority = EventPriority.LOWEST)
    public void onSignDone(SignChangeEvent event) {
        Bukkit.broadcastMessage("EVENT TRIGGERED");
        if(event.getBlock().getLocation() == block.getLocation()) {
            event.setCancelled(true);
            Bukkit.broadcastMessage(event.getLine(0)); //TODO
        }
    }
}

*/







import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.shadownightsmp.plugin.shadownightsmp.ShadowNightSMP;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiPredicate;

public final class SignInput {

    private static final int ACTION_INDEX = 9;
    private static final int SIGN_LINES = 4;

    private static final String NBT_FORMAT = "{\"text\":\"%s\"}";
    private static final String NBT_BLOCK_ID = "minecraft:sign";

    private final Map<Player, Menu> inputs;

    public SignInput() {
        this.inputs = new HashMap<>();
        this.listen();
    }

    public Menu newMenu(List<String> text) {
        return new Menu(text);
    }

    private void listen() {
        ShadowNightSMP.protocolManager.addPacketListener(new PacketAdapter(ShadowNightSMP.plugin, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                Player player = event.getPlayer();

                Menu menu = inputs.remove(player);

                if (menu == null) {
                    return;
                }
                event.setCancelled(true);

                boolean success = menu.response.test(player, event.getPacket().getStringArrays().read(0));


                if (!success && menu.reopenIfFail && !menu.forceClose) {
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
        });
    }

    public final class Menu {

        private final List<String> text;

        private BiPredicate<Player, String[]> response;
        private boolean reopenIfFail;

        private BlockPosition posWrapper;

        private boolean forceClose;

        Menu(List<String> text) {
            this.text = text;
        }

        public Menu reopenIfFail(boolean value) {
            this.reopenIfFail = value;
            return this;
        }

        public Menu response(BiPredicate<Player, String[]> response) {
            this.response = response;
            return this;
        }

        public void open(Player player) {
            Objects.requireNonNull(player, "player");
            if (!player.isOnline()) {
                return;
            }
            Location location = player.getLocation();
            this.posWrapper = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());

            player.sendBlockChange(this.posWrapper.toLocation(location.getWorld()), Material.OAK_SIGN.createBlockData());
            //TODO player.sendSignChange(location, text.stream().map(this::color).toList().toArray(new String[]{ "a", "b", "c", "d" }));

            PacketContainer openSign = ShadowNightSMP.protocolManager.createPacket(PacketType.Play.Server.OPEN_SIGN_EDITOR);
            //PacketContainer signData = ShadowNightSMP.protocolManager.createPacket(PacketType.Play.Server.TILE_ENTITY_DATA);

            openSign.getBlockPositionModifier().write(0, this.posWrapper);

            //NbtCompound signNBT = (NbtCompound) signData.getNbtModifier().read(0);

            //for (int line = 0; line < SIGN_LINES; line++) {
            //    signNBT.put("Text" + (line + 1), this.text.size() > line ? String.format(NBT_FORMAT, color(this.text.get(line))) : "");
            //}

            //signNBT.put("x", this.position.getX());
            //signNBT.put("y", this.position.getY());
            //signNBT.put("z", this.position.getZ());
            //signNBT.put("id", NBT_BLOCK_ID);

            //signData.getBlockPositionModifier().write(0, this.position);
            ////signData.getIntegers().write(0, ACTION_INDEX);
            //signData.getNbtModifier().write(0, signNBT);

            try {
                //ShadowNightSMP.protocolManager.sendServerPacket(player, signData);
                ShadowNightSMP.protocolManager.sendServerPacket(player, openSign);
            } catch (InvocationTargetException exception) {
                exception.printStackTrace();
            }
            inputs.put(player, this);
        }

        /**
         * closes the menu. if force is true, the menu will close and will ignore the reopen
         * functionality. false by default.
         *
         * @param player the player
         * @param force decides whether or not it will reopen if reopen is enabled
         */
        public void close(Player player, boolean force) {
            this.forceClose = force;
            if (player.isOnline()) {
                player.closeInventory();
            }
        }

        public void close(Player player) {
            close(player, false);
        }

        private String color(String input) {
            return ChatColor.translateAlternateColorCodes('&', input);
        }
    }
}