package org.shadownightsmp.plugin.shadownightsmp;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.shadownightsmp.plugin.shadownightsmp.Economy.CMD_trade;
import org.shadownightsmp.plugin.shadownightsmp.Economy.Economy;
import org.shadownightsmp.plugin.shadownightsmp.QOL.CMD_flyspeed;
import org.shadownightsmp.plugin.shadownightsmp.QOL.CMD_rtp;
import org.shadownightsmp.plugin.shadownightsmp.QOL.Info.*;
import org.shadownightsmp.plugin.shadownightsmp.ChatManager.CMD_msg;
import org.shadownightsmp.plugin.shadownightsmp.ChatManager.CMD_r;
import org.shadownightsmp.plugin.shadownightsmp.QOL.TPA.CMD_tpa;
import org.shadownightsmp.plugin.shadownightsmp.QOL.TPA.CMD_tpaccept;
import org.bukkit.plugin.java.JavaPlugin;
import org.shadownightsmp.plugin.shadownightsmp.utils.SignInput;


public final class ShadowNightSMP extends JavaPlugin {
    public static JavaPlugin plugin;
    public static LuckPerms lpApi;
    public static ProtocolManager protocolManager;
    public static SignInput signInput;


    @Override
    public void onEnable() {
        plugin = this;


        // Initialize Luckperms API
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) lpApi = provider.getProvider();


        // Initialize protocol manager
        protocolManager = ProtocolLibrary.getProtocolManager();
        signInput = new SignInput();


        // Initialize event listener
        getServer().getPluginManager().registerEvents(new ShadowNightSMP_listener(), this);


        // Initialize Papi expansion
        new ShadowNightSMP_papi().register();


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
    }





    @Override
    public void onDisable() {
        Economy.saveDatabase();
    }
}
