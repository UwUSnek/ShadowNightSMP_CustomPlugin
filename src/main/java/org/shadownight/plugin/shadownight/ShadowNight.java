package org.shadownight.plugin.shadownight;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.shadownight.plugin.shadownight.ChatManager.DiscordBotManager;
import org.shadownight.plugin.shadownight.Economy.CMD_trade;
import org.shadownight.plugin.shadownight.Economy.Economy;
import org.shadownight.plugin.shadownight.QOL.CMD_flyspeed;
import org.shadownight.plugin.shadownight.QOL.CMD_rtp;
import org.shadownight.plugin.shadownight.QOL.Info.*;
import org.shadownight.plugin.shadownight.ChatManager.CMD_msg;
import org.shadownight.plugin.shadownight.ChatManager.CMD_r;
import org.shadownight.plugin.shadownight.QOL.TPA.CMD_tpa;
import org.shadownight.plugin.shadownight.QOL.TPA.CMD_tpaccept;
import org.bukkit.plugin.java.JavaPlugin;
import org.shadownight.plugin.shadownight.utils.SignInput;


public final class ShadowNight extends JavaPlugin {
    public static JavaPlugin plugin;
    public static LuckPerms lpApi;
    public static ProtocolManager protocolManager;


    @Override
    public void onEnable() {
        plugin = this;


        // Initialize Luckperms API
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) lpApi = provider.getProvider();


        // Initialize protocol manager
        protocolManager = ProtocolLibrary.getProtocolManager();
        //signInput = new SignInput();
        ShadowNight.protocolManager.addPacketListener(SignInput.packetListener);


        // Initialize event listener
        getServer().getPluginManager().registerEvents(new ShadowNight_listener(), this);


        // Initialize Papi expansion
        new ShadowNight_papi().register();


        // Load Economy database
        this.getCommand("trade").setExecutor(new CMD_trade());
        Economy.loadDatabase();




        // Teleports
        this.getCommand("rtp").setExecutor(new CMD_rtp());
        this.getCommand("tpa").setExecutor(new CMD_tpa());
        this.getCommand("tpaccept").setExecutor(new CMD_tpaccept());

        // Misc
        this.getCommand("flyspeed").setExecutor(new CMD_flyspeed());
        this.getCommand("msg").setExecutor(new CMD_msg());
        this.getCommand("tell").setExecutor(new CMD_msg());
        this.getCommand("r").setExecutor(new CMD_r());

        //Info
        this.getCommand("help").setExecutor(new CMD_help());
        this.getCommand("vote").setExecutor(new CMD_vote());
        this.getCommand("ping").setExecutor(new CMD_ping());
        this.getCommand("playtime").setExecutor(new CMD_playtime());
        this.getCommand("colors").setExecutor(new CMD_colors());
        this.getCommand("discord").setExecutor(new CMD_discord());




        // Initialize Discord Bot Manager
        DiscordBotManager.init();
        DiscordBotManager.sendBridgeMessage("🟢 Server is online");
    }





    @Override
    public void onDisable() {
        Economy.saveDatabase();
        DiscordBotManager.sendBridgeMessage("🔴 Server is offline");
    }
}
