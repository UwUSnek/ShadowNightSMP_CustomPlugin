package org.shadownightsmp.plugin.shadownightsmp;

import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.shadownightsmp.plugin.shadownightsmp.ChatManager.ChatManager;
import org.shadownightsmp.plugin.shadownightsmp.ChatManager.DeathMessages;
import org.shadownightsmp.plugin.shadownightsmp.ChatManager.DiscordBanner;
import org.shadownightsmp.plugin.shadownightsmp.ChatManager.JoinLeaveMessages;
import org.shadownightsmp.plugin.shadownightsmp.Economy.Economy;
import org.shadownightsmp.plugin.shadownightsmp.QOL.SpawnInvincibility;
import org.shadownightsmp.plugin.shadownightsmp.QOL.StarterKit;
import org.shadownightsmp.plugin.shadownightsmp.QOL.SurvivalFly;
import org.shadownightsmp.plugin.shadownightsmp.QOL.TPA.CMD_tpa;


public class ShadowNightSMP_listener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        JoinLeaveMessages.formatJoin(event);
        DiscordBanner.startLoop(event.getPlayer(), ShadowNightSMP.plugin);
        StarterKit.onJoin(event.getPlayer());
        SurvivalFly.updateState(event.getPlayer());
        Economy.addPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        JoinLeaveMessages.formatQuit(event);
        CMD_tpa.removeTargetFromAllRequesters(event.getPlayer().getName());
        event.getPlayer().setInvulnerable(false);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        DeathMessages.formatDeathMessage(event);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        StarterKit.onRespawn(event.getPlayer(), event.getRespawnReason());
        SpawnInvincibility.onRespawn(event.getPlayer(), event.getRespawnReason());
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        StarterKit.onItemDrop(event.getEntity());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        SurvivalFly.updateState(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent(AsyncPlayerChatEvent event) {
        ChatManager.processPlayerMessage(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(InventoryClickEvent event) {
        StarterKit.onClickEvent(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryDrag(InventoryDragEvent event) {
        StarterKit.onDragEvent(event);
    }
}
