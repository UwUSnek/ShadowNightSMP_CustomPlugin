package org.uwu_snek.shadownight;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.uwu_snek.shadownight.adminCommands.CMD_sngive;
import org.uwu_snek.shadownight.adminCommands.CMD_summontest;
import org.uwu_snek.shadownight.adminCommands.CMD_testrotation;
import org.uwu_snek.shadownight.chatManager.discord.BotManager;
import org.uwu_snek.shadownight.dungeons.CMD_dungeontest;
import org.uwu_snek.shadownight.dungeons.CMD_recreatedungeon;
import org.uwu_snek.shadownight.dungeons.Dungeon;
import org.uwu_snek.shadownight.economy.CMD_trade;
import org.uwu_snek.shadownight.economy.Economy;
import org.uwu_snek.shadownight.enchantments.CustomEnchantManager;
import org.uwu_snek.shadownight.adminCommands.CMD_sngiveid;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.customItems.ItemManager;
import org.uwu_snek.shadownight.qol.CMD_flyspeed;
import org.uwu_snek.shadownight.qol.CMD_rtp;
import org.uwu_snek.shadownight.qol.CMD_show;
import org.uwu_snek.shadownight.qol.info.*;
import org.uwu_snek.shadownight.chatManager.CMD_msg;
import org.uwu_snek.shadownight.chatManager.CMD_r;
import org.uwu_snek.shadownight.qol.tpa.CMD_tpa;
import org.uwu_snek.shadownight.qol.tpa.CMD_tpaccept;
import org.bukkit.plugin.java.JavaPlugin;
import org.uwu_snek.shadownight.utils.SignInput;
import org.uwu_snek.shadownight.utils.SkinRenderer;
import org.uwu_snek.shadownight.utils.utils;

import java.util.Objects;
import java.util.logging.Level;


public final class ShadowNight extends JavaPlugin {
    public static JavaPlugin plugin;
    public static LuckPerms lpApi;
    public static ProtocolManager protocolManager;
    public static MultiverseCore mvCoreApi;
    public static MVWorldManager mvWorldManager;



    @Override
    public void onEnable() {
        plugin = this;

        // Get Multiverse-Core API instance
        mvCoreApi = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        mvWorldManager = Objects.requireNonNull(mvCoreApi, "MultiverseCore API is null").getMVWorldManager();

        // Initialize protocol manager
        protocolManager = ProtocolLibrary.getProtocolManager();




        // Add custom enchantments to the Vanilla registry
        //ShadowNight.protocolManager.addPacketListener(new ETablePacketReplacer());
        CustomEnchantManager.registerEnchantments();



        // Print custom items for debug and to make the IDs known to ops
        for(ItemManager itemManager : ItemManager.values()) {
            _custom_item_id itemId = itemManager.getInstance().getCustomItemId();
            utils.log(Level.INFO, "Loaded CustomItem \"" + itemId.name() + "\" with ID " + itemId.getNumericalValue());
        }


        // Initialize Luckperms API
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) lpApi = provider.getProvider();


        // Add Sign Input packet listener
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
        Objects.requireNonNull(this.getCommand("show"),     "getCommand returned null").setExecutor(new CMD_show());

        //Info
        Objects.requireNonNull(this.getCommand("help"),     "getCommand returned null").setExecutor(new CMD_help());
        Objects.requireNonNull(this.getCommand("vote"),     "getCommand returned null").setExecutor(new CMD_vote());
        Objects.requireNonNull(this.getCommand("ping"),     "getCommand returned null").setExecutor(new CMD_ping());
        Objects.requireNonNull(this.getCommand("playtime"), "getCommand returned null").setExecutor(new CMD_playtime());
        Objects.requireNonNull(this.getCommand("colors"),   "getCommand returned null").setExecutor(new CMD_colors());
        Objects.requireNonNull(this.getCommand("discord"),  "getCommand returned null").setExecutor(new CMD_discord());


        // Other
        Objects.requireNonNull(this.getCommand("dungeontest"),     "getCommand returned null").setExecutor(new CMD_dungeontest());
        Objects.requireNonNull(this.getCommand("recreatedungeon"), "getCommand returned null").setExecutor(new CMD_recreatedungeon());
        Objects.requireNonNull(this.getCommand("sngiveid"),          "getCommand returned null").setExecutor(new CMD_sngiveid());
        Objects.requireNonNull(this.getCommand("sngive"),          "getCommand returned null").setExecutor(new CMD_sngive());
        Objects.requireNonNull(this.getCommand("summontest"),          "getCommand returned null").setExecutor(new CMD_summontest());
        Objects.requireNonNull(this.getCommand("testrotation"),          "getCommand returned null").setExecutor(new CMD_testrotation());




        // Initialize skin renderer
        SkinRenderer.init();


        // Initialize Discord Bot Manager
        BotManager.init();
        BotManager.sendBridgeMessage("🟢 Server is online");



        // Initialize Dungeons
        Dungeon.deleteOldDungeons();
    }





    @Override
    public void onDisable() {
        Economy.saveDatabase();
        BotManager.sendBridgeMessage("🔴 Server is offline");
    }
}
