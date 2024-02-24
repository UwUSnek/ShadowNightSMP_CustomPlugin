package org.shadownight.plugin.shadownight;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.shadownight.plugin.shadownight.chatManager.ChatManager;
import org.shadownight.plugin.shadownight.chatManager.DeathMessages;
import org.shadownight.plugin.shadownight.chatManager.discord.InGameBanner;
import org.shadownight.plugin.shadownight.chatManager.JoinLeaveMessages;
import org.shadownight.plugin.shadownight.economy.Economy;
import org.shadownight.plugin.shadownight.items.AttackOverride;
import org.shadownight.plugin.shadownight.items.IM;
import org.shadownight.plugin.shadownight.items.bow.IM_Bow;
import org.shadownight.plugin.shadownight.qol.SpawnInvincibility;
import org.shadownight.plugin.shadownight.qol.StarterKit;
import org.shadownight.plugin.shadownight.qol.SurvivalFly;
import org.shadownight.plugin.shadownight.qol.tpa.CMD_tpa;


public final class ShadowNight_listener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        JoinLeaveMessages.formatJoin(event);
        InGameBanner.startLoop(event.getPlayer(), ShadowNight.plugin);
        StarterKit.onJoin(event.getPlayer());
        SurvivalFly.updateState(event.getPlayer());
        Economy.addPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(final PlayerQuitEvent event) {
        JoinLeaveMessages.formatQuit(event);
        CMD_tpa.removeTargetFromAllRequesters(event.getPlayer().getName());
        event.getPlayer().setInvulnerable(false);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(final PlayerDeathEvent event) {
        DeathMessages.formatDeathMessage(event);
    }

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent event) {
        StarterKit.onRespawn(event.getPlayer(), event.getRespawnReason());
        SpawnInvincibility.onRespawn(event.getPlayer(), event.getRespawnReason());
    }

    @EventHandler
    public void onItemSpawn(final ItemSpawnEvent event) {
        StarterKit.onItemDrop(event.getEntity());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
        SurvivalFly.updateState(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent(final AsyncPlayerChatEvent event) {
        ChatManager.processPlayerMessage(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(final InventoryClickEvent event) {
        StarterKit.onClickEvent(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryDrag(final InventoryDragEvent event) {
        StarterKit.onDragEvent(event);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(final PlayerInteractEvent event) {
        IM.chooseOnInteract(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAttack(final EntityDamageByEntityEvent event) {
        //IM.chooseOnAttack(event);
        AttackOverride.customAttack(event, true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onShoot(final EntityShootBowEvent event) {
        IM_Bow.chooseOnShoot(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectileHit(final ProjectileHitEvent event) {
        if(event.getEntityType() == EntityType.ARROW) IM_Bow.chooseOnProjectileHit(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntitySpawn(final EntitySpawnEvent event) {
        //TODO use dedicated class
        if(event.getEntity() instanceof ArmorStand entity) entity.setArms(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSprintToggle(final PlayerToggleSprintEvent event) {
        AttackOverride.resetKnockbackSprintBuff(event);
    }
}
