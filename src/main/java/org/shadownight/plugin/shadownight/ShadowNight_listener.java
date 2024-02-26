package org.shadownight.plugin.shadownight;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.attackOverride.CustomKnockback;
import org.shadownight.plugin.shadownight.chatManager.ChatManager;
import org.shadownight.plugin.shadownight.chatManager.DeathMessages;
import org.shadownight.plugin.shadownight.chatManager.discord.InGameBanner;
import org.shadownight.plugin.shadownight.chatManager.JoinLeaveMessages;
import org.shadownight.plugin.shadownight.economy.Economy;
import org.shadownight.plugin.shadownight.attackOverride.AttackOverride;
import org.shadownight.plugin.shadownight.items.IM;
import org.shadownight.plugin.shadownight.items.bow.IM_Bow;
import org.shadownight.plugin.shadownight.qol.ArmorStandArms;
import org.shadownight.plugin.shadownight.qol.SpawnInvincibility;
import org.shadownight.plugin.shadownight.qol.StarterKit;
import org.shadownight.plugin.shadownight.qol.SurvivalFly;
import org.shadownight.plugin.shadownight.qol.tpa.CMD_tpa;


public final class ShadowNight_listener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        JoinLeaveMessages.formatJoin(event);
        InGameBanner.startLoop(event.getPlayer(), ShadowNight.plugin);
        StarterKit.onJoin(event.getPlayer());
        SurvivalFly.updateState(event.getPlayer());
        Economy.addPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(@NotNull final PlayerQuitEvent event) {
        JoinLeaveMessages.formatQuit(event);
        CMD_tpa.removeTargetFromAllRequesters(event.getPlayer().getName());
        event.getPlayer().setInvulnerable(false);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(@NotNull final PlayerDeathEvent event) {
        DeathMessages.formatDeathMessage(event);
    }

    @EventHandler
    public void onPlayerRespawn(@NotNull final PlayerRespawnEvent event) {
        StarterKit.onRespawn(event.getPlayer(), event.getRespawnReason());
        SpawnInvincibility.onRespawn(event.getPlayer(), event.getRespawnReason());
    }

    @EventHandler
    public void onItemSpawn(@NotNull final ItemSpawnEvent event) {
        StarterKit.onItemDrop(event.getEntity());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerChangedWorld(final @NotNull PlayerChangedWorldEvent event) {
        SurvivalFly.updateState(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChatEvent(@NotNull final AsyncPlayerChatEvent event) {
        ChatManager.processPlayerMessage(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClick(@NotNull final InventoryClickEvent event) {
        StarterKit.onClickEvent(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryDrag(@NotNull final InventoryDragEvent event) {
        StarterKit.onDragEvent(event);
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(@NotNull final PlayerInteractEvent event) {
        IM.chooseOnInteract(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAttack(@NotNull final EntityDamageByEntityEvent event) {
        AttackOverride.customAttack(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(@NotNull final EntityDeathEvent event) {
        AttackOverride.onDeath(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onShoot(@NotNull final EntityShootBowEvent event) {
        IM_Bow.chooseOnShoot(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onProjectileHit(@NotNull final ProjectileHitEvent event) {
        if(event.getEntityType() == EntityType.ARROW) IM_Bow.chooseOnProjectileHit(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntitySpawn(@NotNull final EntitySpawnEvent event) {
        ArmorStandArms.onSpawn(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onSprintToggle(@NotNull final PlayerToggleSprintEvent event) {
        CustomKnockback.resetKnockbackSprintBuff(event);
    }
}
