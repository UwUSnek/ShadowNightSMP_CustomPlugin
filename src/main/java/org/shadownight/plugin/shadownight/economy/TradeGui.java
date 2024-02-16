package org.shadownight.plugin.shadownight.economy;


import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.shadownight.plugin.shadownight.qol.StarterKit;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.SignInput;
import org.shadownight.plugin.shadownight.utils.Timer;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;




public final class TradeGui implements Listener {
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
    public boolean alreadyClosed = false;
    private boolean readingInput = false;

    private long inputTmp = 0;
    private final NamespacedKey coin_key = new NamespacedKey(ShadowNight.plugin, "coin_value");


    public TradeGui(Player _player, Player target){
        player = _player;
        inv = Bukkit.createInventory(null, 54, "§rYou §l⮀§r " + target.getName());
        inv.setItem(buttonAddCoins,       utils.createItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1, "§eClick to add coins"));
        inv.setItem(buttonAddClaimBlocks, utils.createItemStack(Material.GREEN_STAINED_GLASS_PANE,  1, "§2Click to add claim blocks"));
        inv.setItem(22,                   utils.createItemStack(Material.BLACK_STAINED_GLASS_PANE,  1, "§r"));
        inv.setItem(31,                   utils.createItemStack(Material.BLACK_STAINED_GLASS_PANE,  1, "§r"));
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
        ItemMeta meta = Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Item meta is null").getItemMeta();
        if(meta != null) meta.setDisplayName("§7Click to accept the trade! (" + timer.getTimeLeft() + "s)");
        Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Button item is null").setItemMeta(meta);
    }
    public void updateConfirmationName() {
        ItemMeta meta = Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Item meta is null").getItemMeta();
        if(meta != null) meta.setDisplayName(playerHasAccepted ? "§aYou accepted the trade. Click to cancel." : "§dClick to accept the trade!");
        Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Button item is null").setItemMeta(meta);
    }

    public void updateConfirmationLore() {
        ItemMeta meta = Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Item meta is null").getItemMeta();
        if(meta != null) {
            ArrayList<String> lore = new ArrayList<>();

            for (ItemStack item : items) lore.add("§c-" + item.getAmount() + "x§f " + utils.getItemName(item));
            lore.add("");
            for (ItemStack item : linkedGui.items) lore.add("§a+" + item.getAmount() + "x§f " + utils.getItemName(item));

            meta.setLore(lore);
            Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Button item is null").setItemMeta(meta);
        }
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


    /**
     * Adds an item to the item list
     * @param item the item to add. Coin  items are allowed and the metadata is set by the SignInput.
     */
    public void selectItem(ItemStack item){
        if(items.size() == 24) {
            Chat.sendMessage(player, "§cYou can only trade up to 24 items!");
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
        // Retrieve item stack
        int i = slot % 9 + 4 * (slot / 9);
        ItemStack item = items.get(i);

        // Give item back to the player
        PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta(), "Item meta is null").getPersistentDataContainer();
        if(container.has(coin_key, PersistentDataType.LONG)) {
            Economy.addToBalance(player, container.get(coin_key, PersistentDataType.LONG));
        }
        else player.getInventory().addItem(item);

        // Remove form item list and update the GUI
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
            PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta(), "Item meta is null").getPersistentDataContainer();
            if(container.has(coin_key, PersistentDataType.LONG)) {
                Economy.addToBalance(_player, container.get(coin_key, PersistentDataType.LONG));
            }
            else {
                if (playerInv.firstEmpty() == -1) world.dropItem(_player.getLocation(), item);
                else playerInv.addItem(item);
            }
        }
    }

    //! Executes twice and manages the current player
    public void completeTrade(){
        alreadyClosed = true;
        giveItems(player, linkedGui.items);
        player.closeInventory();
        Chat.sendMessage(player, "§aTrade completed with " + utils.getFancyName(linkedGui.player) + "§a!");
    }

    //! Executes once and manages both players
    public void cancelTrade() {
        alreadyClosed = true;
        giveItems(player, items);

        linkedGui.alreadyClosed = true;
        giveItems(linkedGui.player, linkedGui.items);
        linkedGui.player.closeInventory();
    }


    private void updateStatus(){
        if(playerHasAccepted && targetHasAccepted) {
            // The other player is managed by its own completion call after its target gets toggled
            completeTrade();
        }
        else {
            if (playerHasAccepted) inv.setItem(buttonPlayerAccept, utils.createItemStack(Material.LIME_STAINED_GLASS_PANE, 1, ""));
            else                   inv.setItem(buttonPlayerAccept, utils.createItemStack(Material.RED_STAINED_GLASS_PANE,  1, ""));

            if(timer != null && timer.getTimeLeft() > 0) updateConfirmationNameTimer();
            else updateConfirmationName();
            updateConfirmationLore();

            if (targetHasAccepted) inv.setItem(buttonTargetAccept, utils.createItemStack(Material.LIME_STAINED_GLASS_PANE, 1, "§r" +             linkedGui.player.getName() + "§a accepted the trade."));
            else                   inv.setItem(buttonTargetAccept, utils.createItemStack(Material.RED_STAINED_GLASS_PANE,  1, "§rWaiting for " + linkedGui.player.getName() + "§r to accept..."));
        }
    }






    public void openInventory() {
        player.openInventory(inv);
    }




    private void readCoinInput(){
        readingInput = true;

        SignInput menu = new SignInput(
            true,
            new String[]{ "", "§6§l^", "§6Insert the number", "§6of coins" },
            (player, lines) -> {
                try {
                    long value = (long) Double.parseDouble(lines[0]);
                    if(value <= 0) {
                        player.sendMessage("§cYou cannot trade less than 1 coin!");
                        return false;
                    }
                    else {
                        inputTmp = value;
                        return true;
                    }
                }
                catch(NumberFormatException e) {
                    player.sendMessage("\"§c" + Chat.stripColor(lines[0]) + "\" is not a number!");
                    return false;
                }
            },
            () -> {
                if(Economy.getBalance(player) >= inputTmp) {
                    // Add item
                    ItemStack coinItem = utils.createItemStack(Material.GOLD_NUGGET, 1, "§6" + inputTmp + " Coins");
                    ItemMeta meta = coinItem.getItemMeta();
                    Objects.requireNonNull(meta, "Item meta is null").getPersistentDataContainer().set(coin_key, PersistentDataType.LONG, inputTmp);
                    coinItem.setItemMeta(meta);

                    // Remove coins from player
                    Economy.removeFromBalance(player, inputTmp);

                    // Re-open the trade GUI and add the new item
                    openInventory();
                    readingInput = false;
                    selectItem(coinItem);
                }
                else {
                    Chat.sendMessage(player, "§cYou don't have that many coins!");
                    // Re-open the trade GUI and add the new item
                    openInventory();
                    readingInput = false;
                }
            }
        );

        menu.open(player);
    }








    // Handle disconnects ad inventory close events
    @EventHandler
    public void onGuiClose(final InventoryCloseEvent event){
        if(!readingInput && !alreadyClosed && event.getInventory() == inv) {
            cancelTrade();
            Chat.sendMessage(player, "§cYou cancelled the trade with " + utils.getFancyName(linkedGui.player) + "§c.");
            Chat.sendMessage(linkedGui.player, utils.getFancyName(player) + "§c has cancelled the trade.");
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
                if(StarterKit.isBlacklisted(clickedItem)) clickedItem.setAmount(0);
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