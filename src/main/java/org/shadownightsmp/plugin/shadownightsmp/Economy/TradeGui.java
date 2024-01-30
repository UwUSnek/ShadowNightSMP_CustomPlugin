package org.shadownightsmp.plugin.shadownightsmp.Economy;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.shadownightsmp.plugin.shadownightsmp.QOL.StarterKit;
import org.shadownightsmp.plugin.shadownightsmp.ShadowNightSMP;
import org.shadownightsmp.plugin.shadownightsmp.utils.SignInput;
import org.shadownightsmp.plugin.shadownightsmp.utils.Timer;
import org.shadownightsmp.plugin.shadownightsmp.utils.utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;




public class TradeGui implements Listener {
    private static final int buttonAddCoins = 4;
    private static final int buttonAddClaimBlocks = 13;
    private static final int buttonPlayerAccept = 40;
    private static final int buttonTargetAccept = 49;

    private final Inventory inv;
    private final Vector<ItemStack> items = new Vector<>();

    private boolean playerHasAccepted = false;
    private boolean targetHasAccepted = false;
    private TradeGui linkedGui = null;
    private final Player player;
    private Timer timer;
    public boolean closed = false;
    private boolean input = false;


    public TradeGui(Player _player, Player target){
        player = _player;
        inv = Bukkit.createInventory(null, 54, "§rYou §l⮀§r " + target.getName());
        inv.setItem(buttonAddCoins,       createGuiItem(Material.YELLOW_STAINED_GLASS_PANE, "§eClick to add coins", ""));
        inv.setItem(buttonAddClaimBlocks, createGuiItem(Material.GREEN_STAINED_GLASS_PANE,  "§2Click to add claim blocks", ""));
        inv.setItem(22,                   createGuiItem(Material.BLACK_STAINED_GLASS_PANE,  "§r", ""));
        inv.setItem(31,                   createGuiItem(Material.BLACK_STAINED_GLASS_PANE,  "§r", ""));
    }
    public void TradeGuiInit(TradeGui _linkedGui) {
        linkedGui = _linkedGui;
        updateStatus();
        timer = new Timer(
            3,
            () -> {
                linkedGui.targetHasAccepted = false;
                linkedGui.updateStatus();
                playerHasAccepted = false;
                updateStatus();
            },
            this::updateConfirmationNameTimer,
            this::updateConfirmationName
        );
    }






    public void toggleTarget(){
        targetHasAccepted = !targetHasAccepted;
        updateStatus();
    }
    private void togglePlayer(){
        playerHasAccepted = !playerHasAccepted;
        updateStatus();
    }




    public void updateConfirmationNameTimer() {
        ItemMeta meta = inv.getItem(buttonPlayerAccept).getItemMeta();
        meta.setDisplayName("§7Click to accept the trade! (" + timer.getTimeLeft() + "s)");
        inv.getItem(buttonPlayerAccept).setItemMeta(meta);
    }
    public void updateConfirmationName() {
        ItemMeta meta = inv.getItem(buttonPlayerAccept).getItemMeta();
        meta.setDisplayName(playerHasAccepted ? "§aYou accepted the trade. Click to cancel." : "§dClick to accept the trade!");
        inv.getItem(buttonPlayerAccept).setItemMeta(meta);
    }

    public void updateConfirmationLore() {
        ItemMeta meta = inv.getItem(buttonPlayerAccept).getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        for (ItemStack item : items)           lore.add("§c-" + item.getAmount() + "x§f " + utils.getItemName(item)); lore.add("");
        for (ItemStack item : linkedGui.items) lore.add("§a+" + item.getAmount() + "x§f " + utils.getItemName(item));

        meta.setLore(lore);
        inv.getItem(buttonPlayerAccept).setItemMeta(meta);
    }




    private void updatePlayerItemList() {
        for (int i = 0; i < items.size(); ++i) {
            inv.setItem(i % 4 + 9 * (i / 4), items.get(i));
        }
        for (int i = items.size(); i < 4 * 6; ++i) {
            inv.setItem(i % 4 + 9 * (i / 4), null);
        }
        timer.start();
    }
    public void updateTargetItemList(){
        for (int i = 0; i < linkedGui.items.size(); ++i) {
            inv.setItem(i % 4 + 9 * (i / 4) + 5, linkedGui.items.get(i));
        }
        for (int i = linkedGui.items.size(); i < 4 * 6; ++i) {
            inv.setItem(i % 4 + 9 * (i / 4) + 5, null);
        }
        timer.start();
    }



    public void selectItem(ItemStack item){
        if(items.size() == 24) {
            utils.sendMessage(player, "§cYou can only trade up to 24 items!");
        }
        else {
            items.add(new ItemStack(item));
            item.setAmount(0);
            updatePlayerItemList();
            linkedGui.updateTargetItemList();
            updateConfirmationLore();
            linkedGui.updateConfirmationLore();
        }
    }
    public void retrieveItem(int slot){
        int i = slot % 9 + 4 * (slot / 9);
        player.getInventory().addItem(items.get(i));
        items.remove(i);
        updatePlayerItemList();
        linkedGui.updateTargetItemList();
        updateConfirmationLore();
        linkedGui.updateConfirmationLore();
    }






    private void giveItems(Player _player, Vector<ItemStack> _items) {
        Inventory playerInv = _player.getInventory();
        World world = _player.getWorld();
        for(ItemStack item : _items){
            if(playerInv.firstEmpty() == -1) world.dropItem(_player.getLocation(), item);
            else playerInv.addItem(item);
        }
    }

    //! Executes twice and manages the current player
    public void completeTrade(){
        closed = true;
        giveItems(player, linkedGui.items);
        player.closeInventory();
        utils.sendMessage(player, "§aTrade completed with " + utils.getFancyName(linkedGui.player) + "§a!");
    }

    //! Executes once and manages both players
    public void cancelTrade() {
        closed = true;
        giveItems(player, items);

        linkedGui.closed = true;
        giveItems(linkedGui.player, linkedGui.items);
        linkedGui.player.closeInventory();
    }


    private void updateStatus(){
        if(playerHasAccepted && targetHasAccepted) {
            // The other player is managed by its own completion call after its target gets toggled
            completeTrade();
        }
        else {
            if (playerHasAccepted) inv.setItem(buttonPlayerAccept, createGuiItem(Material.LIME_STAINED_GLASS_PANE, "", ""));
            else                   inv.setItem(buttonPlayerAccept, createGuiItem(Material.RED_STAINED_GLASS_PANE, "", ""));

            if(timer != null && timer.getTimeLeft() > 0) updateConfirmationNameTimer();
            else updateConfirmationName();
            updateConfirmationLore();

            if (targetHasAccepted) inv.setItem(buttonTargetAccept, createGuiItem(Material.LIME_STAINED_GLASS_PANE, "§r" + utils.getFancyName(linkedGui.player) + "§a accepted the trade.", ""));
            else                   inv.setItem(buttonTargetAccept, createGuiItem(Material.RED_STAINED_GLASS_PANE, "§rWaiting for " + utils.getFancyName(linkedGui.player) + "§r to accept...", ""));
        }
    }



    // Creates an item with a custom name and lore
    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);

        return item;
    }



    public void openInventory() {
        player.openInventory(inv);
    }




    private void readCoinInput(){
        //SignInput sign = new SignInput();
        input = true;

        //sign.open(player);


        //SignInput.Menu menu = ShadowNightSMP.signInput.newMenu()
        //.reopenIfFail(true)
        //.response((player, lines) -> {
        //    long inputValue;
        //    try {
        //        inputValue = Long.parseLong(lines[0]);
        //        player.sendMessage("Detected \"" + inputValue + "\"");
        //        return true;
        //    }
        //    catch(NumberFormatException e) {
        //        player.sendMessage(lines[0] + " is not a number!");
        //        return false;
        //    }
        //});


        SignInput menu = new SignInput(
            true,
            (player, lines) -> {
                long inputValue;
                try {
                    inputValue = Long.parseLong(lines[0]);
                    player.sendMessage("Detected \"" + inputValue + "\"");
                    return true;
                }
                catch(NumberFormatException e) {
                    player.sendMessage(lines[0] + " is not a number!");
                    return false;
                }
            }
        );

        menu.open(player);
    }








    // Handle disconnects ad inventory close events
    @EventHandler
    public void onGuiClose(final InventoryCloseEvent event){
        if(!input && !closed && event.getInventory() == inv) {
            cancelTrade();
            utils.sendMessage(player, "§cYou cancelled the trade with " + utils.getFancyName(linkedGui.player) + "§c.");
            utils.sendMessage(linkedGui.player, utils.getFancyName(player) + "§c has cancelled the trade.");
        }
    }


    // Handle click events
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!event.getInventory().equals(inv)) return;
        event.setCancelled(true);
        int clickedSlot = event.getRawSlot();



        final ItemStack clickedItem = event.getCurrentItem();
        if(clickedItem != null && !clickedItem.getType().isAir()) {
            // Manage item selection
            if (clickedSlot >= inv.getSize()) {
                if(StarterKit.isBLacklisted(clickedItem)) clickedItem.setAmount(0);
                else selectItem(clickedItem);
            }

            // Manage item retrieval
            else if (clickedSlot % 9 < 4) {
                retrieveItem(clickedSlot);
            }

            // Manage button clicks
            else switch (clickedSlot) {
                    case buttonAddCoins:  {
                        readCoinInput();
                        break;
                    }
                    case buttonAddClaimBlocks: {
                        player.sendMessage("§cThis feature has not been implemented yet!");
                        break;
                    }
                    case buttonPlayerAccept: {
                        if(timer.getTimeLeft() == 0) {
                            linkedGui.toggleTarget();
                            togglePlayer();
                        }
                        break;
                    }
                }
        }
    }




    // Cancel drag events
    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        if (event.getInventory().equals(inv))  event.setCancelled(true);
    }
}