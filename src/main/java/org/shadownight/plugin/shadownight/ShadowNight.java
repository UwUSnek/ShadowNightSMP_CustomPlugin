package org.shadownight.plugin.shadownight;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.onarandombox.MultiverseCore.MultiverseCore;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.shadownight.plugin.shadownight.chatManager.DiscordBotManager;
import org.shadownight.plugin.shadownight.dungeons.CMD_dungeontest;
import org.shadownight.plugin.shadownight.economy.CMD_trade;
import org.shadownight.plugin.shadownight.economy.Economy;
import org.shadownight.plugin.shadownight.items.Scythe;
import org.shadownight.plugin.shadownight.qol.CMD_flyspeed;
import org.shadownight.plugin.shadownight.qol.CMD_rtp;
import org.shadownight.plugin.shadownight.qol.info.*;
import org.shadownight.plugin.shadownight.chatManager.CMD_msg;
import org.shadownight.plugin.shadownight.chatManager.CMD_r;
import org.shadownight.plugin.shadownight.qol.tpa.CMD_tpa;
import org.shadownight.plugin.shadownight.qol.tpa.CMD_tpaccept;
import org.bukkit.plugin.java.JavaPlugin;
import org.shadownight.plugin.shadownight.utils.SignInput;
import org.shadownight.plugin.shadownight.utils.SkinRenderer;

import java.util.Objects;


public final class ShadowNight extends JavaPlugin {
    public static JavaPlugin plugin;
    public static LuckPerms lpApi;
    public static ProtocolManager protocolManager;
    public static MultiverseCore mvcore;

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
        Objects.requireNonNull(this.getCommand("trade"),    "getCommand returned null").setExecutor(new CMD_trade());
        Economy.loadDatabase();




        // Teleports
        Objects.requireNonNull(this.getCommand("rtp"),      "getCommand returned null").setExecutor(new CMD_rtp());
        Objects.requireNonNull(this.getCommand("tpa"),      "getCommand returned null").setExecutor(new CMD_tpa());
        Objects.requireNonNull(this.getCommand("tpaccept"), "getCommand returned null").setExecutor(new CMD_tpaccept());

        // Misc
        Objects.requireNonNull(this.getCommand("flyspeed"), "getCommand returned null").setExecutor(new CMD_flyspeed());
        Objects.requireNonNull(this.getCommand("msg"),      "getCommand returned null").setExecutor(new CMD_msg());
        Objects.requireNonNull(this.getCommand("tell"),     "getCommand returned null").setExecutor(new CMD_msg());
        Objects.requireNonNull(this.getCommand("r"),        "getCommand returned null").setExecutor(new CMD_r());

        //Info
        Objects.requireNonNull(this.getCommand("help"),     "getCommand returned null").setExecutor(new CMD_help());
        Objects.requireNonNull(this.getCommand("vote"),     "getCommand returned null").setExecutor(new CMD_vote());
        Objects.requireNonNull(this.getCommand("ping"),     "getCommand returned null").setExecutor(new CMD_ping());
        Objects.requireNonNull(this.getCommand("playtime"), "getCommand returned null").setExecutor(new CMD_playtime());
        Objects.requireNonNull(this.getCommand("colors"),   "getCommand returned null").setExecutor(new CMD_colors());
        Objects.requireNonNull(this.getCommand("discord"),  "getCommand returned null").setExecutor(new CMD_discord());


        // Other
        Objects.requireNonNull(this.getCommand("dungeontest"),  "getCommand returned null").setExecutor(new CMD_dungeontest());




        // Initialize skin renderer
        SkinRenderer.init();


        // Initialize Discord Bot Manager
        DiscordBotManager.init();
        DiscordBotManager.sendBridgeMessage("🟢 Server is online");





        // Initialize custom items
        Scythe.createRecipes();


        // Initialize multiverse API
        mvcore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
    }





    @Override
    public void onDisable() {
        Economy.saveDatabase();
        DiscordBotManager.sendBridgeMessage("🔴 Server is offline");
    }
}
