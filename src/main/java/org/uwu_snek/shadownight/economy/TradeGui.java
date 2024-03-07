package org.uwu_snek.shadownight.economy;


import net.kyori.adventure.text.Component;
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
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight.utils.SignInput;
import org.uwu_snek.shadownight.utils.Timer;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.spigot.PlayerUtils;

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


    /**
     * Creates a new Trade GUI that allows 2 players to trade items and coins.
     * @param _player The first player
     * @param target The second player
     */
    public TradeGui(final @NotNull Player _player, final @NotNull Player target){
        player = _player;
        //noinspection deprecation
        inv = Bukkit.createInventory(null, 54, "§rYou §l⮀§r " + target.getName());
        inv.setItem(buttonAddCoins,       ItemUtils.createItemStack(Material.YELLOW_STAINED_GLASS_PANE, 1, "§eClick to add coins"));
        inv.setItem(buttonAddClaimBlocks, ItemUtils.createItemStack(Material.GREEN_STAINED_GLASS_PANE,  1, "§2Click to add claim blocks"));
        inv.setItem(22,                   ItemUtils.createItemStack(Material.BLACK_STAINED_GLASS_PANE,  1, "§r"));
        inv.setItem(31,                   ItemUtils.createItemStack(Material.BLACK_STAINED_GLASS_PANE,  1, "§r"));
    }

    /**
     * Initializes the Trade GUI and sets it's linked GUI.
     * Must be called after creating the instance.
     * @param _linkedGui The GUI instance of the other player
     */
    public void TradeGuiInit(final @NotNull TradeGui _linkedGui) {
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


    /**
     * Toggles the confirmation state of the other player.
     */
    public void toggleTarget(){
        targetHasAccepted = !targetHasAccepted;
        updateStatus();
    }

    /**
     * Toggles the confirmation state of the current player.
     */
    private void togglePlayer(){
        playerHasAccepted = !playerHasAccepted;
        updateStatus();
    }


    /**
     * Updates the remaining time display.
     */
    public void updateConfirmationNameTimer() {
        final ItemMeta meta = Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Item meta is null").getItemMeta();
        if(meta != null) meta.displayName(Component.text("§7Click to accept the trade! (" + timer.getTimeLeft() + "s)"));
        Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Button item is null").setItemMeta(meta);
    }

    /**
     * Updates the text that tells the player if they have confirmed or can do so.
     */
    public void updateConfirmationName() {
        final ItemMeta meta = Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Item meta is null").getItemMeta();
        if(meta != null) meta.displayName(Component.text(playerHasAccepted ? "§aYou accepted the trade. Click to cancel." : "§dClick to accept the trade!"));
        Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Button item is null").setItemMeta(meta);
    }

    /**
     * Updates the list of given and received items.
     */
    public void updateConfirmationLore() {
        final ItemMeta meta = Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Item meta is null").getItemMeta();
        if(meta != null) {
            ArrayList<Component> lore = new ArrayList<>();

            for (ItemStack item : items) lore.add(Component.text("§c-" + item.getAmount() + "x§f " + ItemUtils.getPlainItemName(item)));
            lore.add(Component.empty());
            for (ItemStack item : linkedGui.items) lore.add(Component.text("§a+" + item.getAmount() + "x§f " + ItemUtils.getPlainItemName(item)));

            meta.lore(lore);
            Objects.requireNonNull(inv.getItem(buttonPlayerAccept), "Button item is null").setItemMeta(meta);
        }
    }


    /**
     * Updates the list of given items for the current player.
     */
    private void updatePlayerItemList() {
        for (int i = 0; i < items.size(); ++i) {
            inv.setItem(i % 4 + 9 * (i / 4), items.get(i));
        }
        for (int i = items.size(); i < 4 * 6; ++i) {
            inv.setItem(i % 4 + 9 * (i / 4), null);
        }
        timer.start();
    }
    /**
     * Updates the list of received items for the current player.
     */
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
     * Adds an item to the list of given items.
     * @param item the item to add. Coin  items are allowed and the metadata is set by the SignInput.
     */
    public void selectItem(final @NotNull ItemStack item){
        if(items.size() == 24) {
            ChatUtils.sendMessage(player, "§cYou can only trade up to 24 items!");
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

    /**
     * Removes an item from the list of given items.
     * @param slot The clicked slot
     */
    public void retrieveItem(final int slot){
        // Retrieve item stack
        final int i = slot % 9 + 4 * (slot / 9);
        final ItemStack item = items.get(i);

        // Give item back to the player
        final PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta(), "Item meta is null").getPersistentDataContainer();
        final Long n = container.get(coin_key, PersistentDataType.LONG);
        if(n != null) {
            Economy.addToBalance(player, n);
        }
        else player.getInventory().addItem(item);

        // Remove form item list and update the GUI
        items.remove(i);
        updatePlayerItemList();
        linkedGui.updateTargetItemList();
        updateConfirmationLore();
        linkedGui.updateConfirmationLore();
    }


    /**
     * Gives an item list to the player <_player>.
     * @param _player The player to give the items to
     * @param _items The list of items
     */
    private void giveItems(final @NotNull Player _player, final @NotNull Vector<ItemStack> _items) {
        final Inventory playerInv = _player.getInventory();
        final World world = _player.getWorld();
        for(ItemStack item : _items){
            PersistentDataContainer container = Objects.requireNonNull(item.getItemMeta(), "Item meta is null").getPersistentDataContainer();
            Long n = container.get(coin_key, PersistentDataType.LONG);
            if(n != null) {
                Economy.addToBalance(_player, n);
            }
            else {
                if (playerInv.firstEmpty() == -1) world.dropItem(_player.getLocation(), item);
                else playerInv.addItem(item);
            }
        }
    }


    /**
     * Completes the trade for the current player.
     */
    public void completeTrade(){
        alreadyClosed = true;
        giveItems(player, linkedGui.items);
        player.closeInventory();
        ChatUtils.sendMessage(player, "§aTrade completed with " + PlayerUtils.getFancyName(linkedGui.player) + "§a!");
    }


    /**
     * Cancels the trade and closes the GUI for both players.
     */
    public void cancelTrade() {
        alreadyClosed = true;
        giveItems(player, items);

        linkedGui.alreadyClosed = true;
        giveItems(linkedGui.player, linkedGui.items);
        linkedGui.player.closeInventory();
    }


    /**
     * Updates the confirmation status GUI for the current player.
     * completeTrade is called when both players confirm.
     */
    private void updateStatus(){
        if(playerHasAccepted && targetHasAccepted) {
            // The other player is managed by its own completion call after its target gets toggled
            completeTrade();
        }
        else {
            if (playerHasAccepted) inv.setItem(buttonPlayerAccept, ItemUtils.createItemStack(Material.LIME_STAINED_GLASS_PANE, 1, ""));
            else                   inv.setItem(buttonPlayerAccept, ItemUtils.createItemStack(Material.RED_STAINED_GLASS_PANE,  1, ""));

            if(timer != null && timer.getTimeLeft() > 0) updateConfirmationNameTimer();
            else updateConfirmationName();
            updateConfirmationLore();

            if (targetHasAccepted) inv.setItem(buttonTargetAccept, ItemUtils.createItemStack(Material.LIME_STAINED_GLASS_PANE, 1, "§r" +             linkedGui.player.getName() + "§a accepted the trade."));
            else                   inv.setItem(buttonTargetAccept, ItemUtils.createItemStack(Material.RED_STAINED_GLASS_PANE,  1, "§rWaiting for " + linkedGui.player.getName() + "§r to accept..."));
        }
    }


    /**
     * Opens this GUI for the current player.
     */
    public void openInventory() {
        player.openInventory(inv);
    }


    /**
     * Opens the coin input GUI and adds the entered number as a coin item to the item list.
     */
    private void readCoinInput(){
        readingInput = true;

        SignInput menu = new SignInput(
            true,
            new Component[]{
                Component.text(""),
                Component.text("§6§l^"),
                Component.text("§6Insert the number"),
                Component.text("§6of coins")
            },
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
                    player.sendMessage("\"§c" + ChatUtils.stripColor(lines[0]) + "\" is not a number!");
                    return false;
                }
            },
            () -> {
                if(Economy.getBalance(player) >= inputTmp) {
                    // Add item
                    ItemStack coinItem = ItemUtils.createItemStack(Material.GOLD_NUGGET, 1, "§6" + inputTmp + " Coins");
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
                    ChatUtils.sendMessage(player, "§cYou don't have that many coins!");
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
            ChatUtils.sendMessage(player, "§cYou cancelled the trade with " + PlayerUtils.getFancyName(linkedGui.player) + "§c.");
            ChatUtils.sendMessage(linkedGui.player, PlayerUtils.getFancyName(player) + "§c has cancelled the trade.");
        }
    }


    // Handle click events
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!event.getInventory().equals(inv)) return;
        event.setCancelled(true);
        int clickedSlot = event.getRawSlot();


        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem != null && !clickedItem.getType().isAir()) {
            // Manage item selection
            if (clickedSlot >= inv.getSize()) {
                if (ItemUtils.isVolatile(clickedItem)) clickedItem.setAmount(0);
                else selectItem(clickedItem);
            }

            // Manage item retrieval
            else if (clickedSlot % 9 < 4) {
                retrieveItem(clickedSlot);
            }

            // Manage button clicks
            else switch (clickedSlot) {
                case buttonAddCoins: {
                    readCoinInput();
                    break;
                }
                case buttonAddClaimBlocks: {
                    player.sendMessage("§cThis feature has not been implemented yet!");
                    break;
                }
                case buttonPlayerAccept: {
                    if (timer.getTimeLeft() == 0) {
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