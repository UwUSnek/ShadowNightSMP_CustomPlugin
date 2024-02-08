package org.shadownight.plugin.shadownight.chatManager;




import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.SkinRenderer;
import org.shadownight.plugin.shadownight.utils.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;


public class DiscordBotManager {
    private static JDA jda;
    public static TextChannel bridgeChannel;
    private static final String bridgeChannelId = "1202610915694870558";
    private static final String testBridgeChannelId = "1202960128421138494";

    public static TextChannel commandsChannel;
    private final static String testCommandsChannelId = "1205224977284997171";
    private final static String commandsChannelId = "1203121153124601917";
    //private static SlashCommand commandProfile;
    private static final Color embedColor = new Color(206, 41, 216);

    private static Webhook webhook;
    private static IncomingWebhookClient webhookClient;


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
        try {
            jda = JDABuilder
                .createDefault(token)
                .setActivity(Activity.customStatus("Helping UwU_Snek fix her code (17 hours in)"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build()
                .awaitReady()
            ;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        // Get output channels
        boolean isMainServer = new File(ShadowNight.plugin.getDataFolder() + "/.mainServer").exists();
        bridgeChannel   = jda.getChannelById(TextChannel.class, isMainServer ? bridgeChannelId   : testBridgeChannelId);
        commandsChannel = jda.getChannelById(TextChannel.class, isMainServer ? commandsChannelId : testCommandsChannelId);


        // Delete old webhooks if present
        List<Webhook> hooks = bridgeChannel.retrieveWebhooks().complete();
        if(!hooks.isEmpty()) {
            utils.log(Level.INFO, "Deleting " + hooks.size() + " old webhooks...");
            for (Webhook hook : hooks) {
                hook.delete().complete();
                utils.log(Level.INFO, "Deleted 1 webhook");
            }
        }

        // Add message listener
        jda.addEventListener(new MessageListener());


/*
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
 */
        // Create webhook + client
        webhook = bridgeChannel.createWebhook("Survival chat Bridge")
            .setAvatar(Icon.from(utils.imageToByteArray(new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB))))
            .complete()
        ;
        webhookClient = WebhookClient.createClient(jda, webhook.getUrl());
    }







    public static void sendBridgeMessage(String msg) {
        bridgeChannel.sendMessage(utils.stripColor(msg)).queue();
        utils.log(Level.INFO, "logged server message \"" + msg + "\"");
    }


    public static void sendBridgeMessage(Player player, String msg) {
        Bukkit.getScheduler().runTaskAsynchronously(ShadowNight.plugin, () -> {
            webhookClient.sendMessage(utils.stripColor(msg))
                .setUsername("[" + utils.getGroupDisplayName(player) + "] " + player.getName())
                .setAvatarUrl(SkinRenderer.getRendererUrl(player, SkinRenderer.RenderType.PROPIC))
                .complete()
            ;
            utils.log(Level.INFO, "logged \"" + msg + "\"");
        });
    }


    //private static void onMessageCreate(MessageCreateEvent event){
    //    if(event.getChannel().getId() == bridgeChannel.getId()) {
    //        MessageAuthor author = event.getMessageAuthor();
    //        if(author.getWebhookId().isEmpty() && !author.isBotUser()) {
    //            Bukkit.broadcastMessage("§9§l[Discord]§9 " + author.getDisplayName() + ChatManager.playerMessageConnector + event.getMessageContent());
    //        }
    //    }
    //}



/*
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
*/
}
