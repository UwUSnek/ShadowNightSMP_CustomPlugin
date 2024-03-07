package org.uwu_snek.shadownight.qol;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;




public class StarterKit extends UtilityClass {
    private static final String kit_prefix = "§c[Starter Kit] §r";


    /**
     * Gives the starter kit to the player <player>.
     * @param player The target player
     */
    public static void give(final Player player){
        final PlayerInventory inv = player.getInventory();
        final Component lore0 = Component.text("§cNotice: this item is part of the starter kit.");
        final Component lore1 = Component.text("§cIt will be deleted if you remove it from your inventory or die");

        ItemStack i1 = ItemUtils.createItemStack(Material.IRON_CHESTPLATE, 1, kit_prefix + "Iron Chestplate",  lore0, lore1);
        ItemStack i2 = ItemUtils.createItemStack(Material.STONE_AXE,       1,  kit_prefix + "Stone Axe",       lore0, lore1);
        ItemStack i3 = ItemUtils.createItemStack(Material.STONE_PICKAXE,   1,  kit_prefix + "Stone Pickaxe",   lore0, lore1);
        ItemStack i4 = ItemUtils.createItemStack(Material.BREAD,           32, kit_prefix + "Bread",           lore0, lore1);

        ItemUtils.makeVolatile(i1);
        ItemUtils.makeVolatile(i2);
        ItemUtils.makeVolatile(i3);
        ItemUtils.makeVolatile(i4);

        inv.setChestplate(i1);
        inv.addItem(i2);
        inv.addItem(i3);
        inv.addItem(i4);
    }


    public static void onJoin(final @NotNull Player player) {
        if(!player.hasPlayedBefore()) give(player);
    }
    public static void onRespawn(final @NotNull PlayerRespawnEvent event) {
        if(event.getPlayer().getInventory().isEmpty() && event.getRespawnReason() == PlayerRespawnEvent.RespawnReason.DEATH) give(event.getPlayer());
    }
}


