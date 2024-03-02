package org.uwu_snek.shadownight;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.ryanhamshire.GriefPrevention.Claim;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.CustomKnockback;
import org.uwu_snek.shadownight.chatManager.ChatManager;
import org.uwu_snek.shadownight.chatManager.DeathMessages;
import org.uwu_snek.shadownight.chatManager.discord.InGameBanner;
import org.uwu_snek.shadownight.chatManager.JoinLeaveMessages;
import org.uwu_snek.shadownight.economy.Economy;
import org.uwu_snek.shadownight.attackOverride.AttackOverride;
import org.uwu_snek.shadownight.itemFilter.EnchantBlacklist;
import org.uwu_snek.shadownight.items.AnvilFilter;
import org.uwu_snek.shadownight.items.GrindstoneFilter;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.items.bow.IM_Bow;
import org.uwu_snek.shadownight.qol.ArmorStandArms;
import org.uwu_snek.shadownight.qol.SpawnInvincibility;
import org.uwu_snek.shadownight.qol.StarterKit;
import org.uwu_snek.shadownight.qol.SurvivalFly;
import org.uwu_snek.shadownight.qol.tpa.CMD_tpa;
import org.uwu_snek.shadownight.utils.spigot.ClaimUtils;




public final class ShadowNight_listener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(@NotNull final PlayerJoinEvent event) {
        JoinLeaveMessages.formatJoin(event);
        InGameBanner.startLoop(event.getPlayer());
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
    public void onChatEvent(@NotNull final AsyncChatEvent event) {
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


    /**
     * GriefPrevention fix.
     * For some reason, GriefPrevention doesn't stop sculk from spreading into claims. This method fixes it.
     */
    @EventHandler(priority = EventPriority.LOWEST) public void onBlockSpread(@NotNull final BlockSpreadEvent event) {
        if(event.getSource().getType() == Material.SCULK_CATALYST){
            final Claim from = ClaimUtils.getClaimAt(event.getSource());
            final Claim to   = ClaimUtils.getClaimAt(event.getBlock());
            if(to != null && (from == null || from.getOwnerID() != to.getOwnerID())) event.setCancelled(true);
        }
    }



    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrepareItemEnchant(@NotNull final PrepareItemEnchantEvent event) {
        EnchantBlacklist.onPrepareItemEnchant(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnchantItem(@NotNull final EnchantItemEvent event){
        EnchantBlacklist.onEnchantItem(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrepareAnvil(@NotNull final PrepareAnvilEvent event){
        AnvilFilter.onPrepareAnvil(event);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPrepareGrindstone(@NotNull final PrepareGrindstoneEvent event){
        GrindstoneFilter.onPrepareGrindstone(event);
    }
}
