package org.shadownight.plugin.shadownight.chatManager;


import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.channel.TextableRegularServerChannel;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.entity.webhook.WebhookBuilder;
import org.javacord.api.event.interaction.SlashCommandCreateEvent;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.interaction.SlashCommand;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.economy.Economy;
import org.shadownight.plugin.shadownight.utils.SkinRenderer;
import org.shadownight.plugin.shadownight.utils.utils;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.logging.Level;


public class DiscordBotManager {
    private static DiscordApi api;
    private static TextableRegularServerChannel bridgeChannel;
    private static final String bridgeChannelId = "1202610915694870558";
    private static final String testBridgeChannelId = "1202960128421138494";

    private static TextableRegularServerChannel commandsChannel;
    private final static String commandsChannelId = "1203121153124601917";
    private static SlashCommand commandProfile;
    private static final Color embedColor = new Color(206, 41, 216);


    public static void init(){
        // Read bot token from config files
        String token;
        try {
            File tokenFile = new File(ShadowNight.plugin.getDataFolder() + "/botToken");
            Scanner scanner = new Scanner(tokenFile);
            token = scanner.nextLine();
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Bot token file not found: \"" + e.getMessage() + "\"");
        }


        // Initialize API
        api = new DiscordApiBuilder()
            .setToken(token)
            .setAllIntents()
            .login().join()
        ;


        // Get output channel
        Optional<TextChannel> _bridgeChannel = api.getTextChannelById(new File(ShadowNight.plugin.getDataFolder() + "/.mainServer").exists() ? bridgeChannelId : testBridgeChannelId);
        if(_bridgeChannel.isEmpty()) throw new RuntimeException("An error occurred while trying to initialize the Discord Bot Manager: Channel not found");
        else bridgeChannel = (TextableRegularServerChannel) _bridgeChannel.get();


        // Delete old webhooks if present
        List<IncomingWebhook> hooks = bridgeChannel.getIncomingWebhooks().join();
        if(!hooks.isEmpty()) {
            utils.log(Level.INFO, "Deleting " + hooks.size() + " old webhooks...");
            for (IncomingWebhook hook : hooks) {
                hook.delete().join();
                utils.log(Level.INFO, "Deleted 1 webhook");
            }
        }


        // Add message listener
        api.addMessageCreateListener(DiscordBotManager::onMessageCreate);


        // Add profile command
        Optional<TextChannel> _commandsChannel = api.getTextChannelById(commandsChannelId);
        if(_commandsChannel.isEmpty()) throw new RuntimeException("An error occurred while trying to initialize the Discord Bot Manager: Channel not found");
        else commandsChannel = (TextableRegularServerChannel) _commandsChannel.get();

        ArrayList<SlashCommandOption> commandOptions = new ArrayList<>();
        commandOptions.add(SlashCommandOption.create(SlashCommandOptionType.STRING, "IGN", "The Minecraft username of the player", true));
        commandProfile = SlashCommand.with("profile", "Shows informations about a player", commandOptions)
            .setEnabledInDms(false)
            .createGlobal(api)
            .join()
        ;
        api.addSlashCommandCreateListener(DiscordBotManager::onSlashCommandCreate);
    }




    public static void sendBridgeMessage(String msg) {
        bridgeChannel.sendMessage(utils.stripColor(msg));
    }
    public static void sendBridgeMessage(Player player, String msg) {
        long latency;
        try {
            latency = api.measureRestLatency().get().toMillis();
            utils.log(Level.WARNING, "Discord Latency: " + latency + "ms");
        }
        catch(ExecutionException|InterruptedException e) {
            e.printStackTrace();
        }
        // Create webhook and send message, then delete it
        Bukkit.getScheduler().runTaskAsynchronously(ShadowNight.plugin, () -> {
            utils.log(Level.INFO, "starting \"" + msg + "\"...");

            long attempts = 0;
            long max_attempts = 3;
            long timeout = 5;
            do {
                ++attempts;
                IncomingWebhook webhook = null;
                CompletableFuture<IncomingWebhook> webhookFuture = new WebhookBuilder(bridgeChannel)
                    .setName("[" + utils.getGroupDisplayName(player) + "] " + player.getName())
                    .setAvatar(SkinRenderer.getRenderPropic(player))
                    .create().orTimeout(timeout, TimeUnit.SECONDS)
                ;
                try {
                    webhook = webhookFuture.join();
                    utils.log(Level.INFO, "logged \"" + msg + "\"");
                    webhook.sendMessage(utils.stripColor(msg));
                    webhook.delete().join();
                    break;
                } catch (CompletionException e) {
                    if(webhook != null) webhook.delete().join();
                    utils.log(Level.INFO, "\"" + msg + "\" timed out (" + attempts + "/" + max_attempts + ")");
                }
            } while (attempts < max_attempts);
        });
    }


    private static void onMessageCreate(MessageCreateEvent event){
        if(event.getChannel().getId() == bridgeChannel.getId()) {
            MessageAuthor author = event.getMessageAuthor();
            if(author.getWebhookId().isEmpty() && !author.isBotUser()) {
                Bukkit.broadcastMessage("§9§l[Discord]§9 " + author.getDisplayName() + ChatManager.playerMessageConnector + event.getMessageContent());
            }
        }
    }




    private static void onSlashCommandCreate(SlashCommandCreateEvent event){
        Optional<TextChannel> channel = event.getInteraction().getChannel();
        if(channel.isEmpty()) throw new RuntimeException("Interaction channel not found");
        if(event.getSlashCommandInteraction().getCommandName().equals(commandProfile.getName())) {
            if(channel.get().equals(commandsChannel)) {
                event.getInteraction()
                    .respondLater()
                    .thenAccept(interactionOriginalResponseUpdater -> {
                        String playerName = event.getSlashCommandInteraction().getOptionByName("IGN").get().getStringValue().get();
                        @SuppressWarnings("deprecation") // Using getOfflinePlayer the way it's meant to be used, no need to warn
                        OfflinePlayer player = Bukkit.getOfflinePlayer(playerName);


                        if (!player.hasPlayedBefore()) interactionOriginalResponseUpdater.addEmbed(new EmbedBuilder().setColor(embedColor)
                            .addField(player.getName() + "'s profile", "\"" + playerName + "\" has never played on Shadow Night!")
                        ).update();
                        else interactionOriginalResponseUpdater
                            .addEmbed(new EmbedBuilder()
                                .setColor(embedColor)
                                .addField(player.getName() + "'s profile", """
                                    - Rank: `%s`
                                    - Playtime: %s
                                    - Status: %s"""
                                .formatted(
                                    utils.getGroupDisplayNameOffline(player),
                                    utils.sToDuration(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, true),
                                    player.isOnline() ? "**Online**" : "_Offline_"
                                ))
                                .addField("─────────────────────────────────────────────────────────────", "")
                                .addInlineField("Player kills", "⚔️ " + player.getStatistic(Statistic.PLAYER_KILLS))
                                .addInlineField("Damage dealt", "💮 " + player.getStatistic(Statistic.DAMAGE_DEALT))
                                .addInlineField("Balance", "🪙 " + Economy.getBalance(player))
                                .addField("─────────────────────────────────────────────────────────────", "")
                                .setImage(SkinRenderer.getRenderFull(player))
                            )
                            .update()
                        ;
                    })
                ;
            }
            else event.getInteraction().createImmediateResponder().setContent("Please, use the #bot-commands channel").respond();
        }
        else event.getInteraction().createImmediateResponder().setContent("Sorry, this interaction failed.").respond();
    }
}
