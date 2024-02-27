package org.uwu_snek.shadownight.chatManager.discord;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.internal.interactions.CommandDataImpl;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight.utils.SkinRenderer;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.uwu_snek.shadownight.utils.spigot.PlayerUtils;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;
import org.uwu_snek.shadownight.utils.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;
import java.util.logging.Level;


public final class BotManager extends UtilityClass {
    public static TextChannel bridgeChannel;
    private static final String bridgeChannelId = "1202610915694870558";
    private static final String testBridgeChannelId = "1202960128421138494";

    public static TextChannel commandsChannel;
    public final static String testCommandsChannelId = "1205224977284997171";
    public final static String commandsChannelId = "1203121153124601917";
    public static final Color embedColor = new Color(206, 41, 216);

    private static IncomingWebhookClient webhookClient;


    /**
     * Initializes the bot manager.
     */
    public static void init() {
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
        JDA jda;
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
        final boolean isMainServer = new File(ShadowNight.plugin.getDataFolder() + "/.mainServer").exists();
        bridgeChannel = jda.getChannelById(TextChannel.class, isMainServer ? bridgeChannelId : testBridgeChannelId);
        commandsChannel = jda.getChannelById(TextChannel.class, isMainServer ? commandsChannelId : testCommandsChannelId);


        // Delete old webhooks if present
        List<Webhook> hooks = bridgeChannel.retrieveWebhooks().complete();
        if (!hooks.isEmpty()) {
            utils.log(Level.INFO, "Deleting " + hooks.size() + " old webhook" + (hooks.size() == 1 ? "" : "s") + "...");
            for (Webhook hook : hooks) {
                hook.delete().complete();
                utils.log(Level.INFO, "Deleted 1 webhook");
            }
        }


        // Add message listener and register commands
        jda.addEventListener(new MessageListener());
        jda.upsertCommand(new CommandDataImpl("profile", "Shows informations about a player").addOption(OptionType.STRING, "ign", "The Minecraft username of the player", true)).complete();


        // Create webhook + client
        webhookClient = WebhookClient.createClient(jda, bridgeChannel.createWebhook("Survival chat Bridge")
            .setAvatar(Icon.from(utils.imageToByteArray(new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB))))
            .complete()
            .getUrl()
        );
    }


    /**
     * Sends a system message to the bridge channel.
     * @param msg The message string
     */
    public static void sendBridgeMessage(final @NotNull String msg) {
        bridgeChannel.sendMessage(ChatUtils.stripColor(msg)).queue();
        utils.log(Level.INFO, "logged server message \"" + msg + "\"");
    }


    /**
     * Sends a player message to the bridge channel
     * @param player The player that sent the message
     * @param msg The message string
     */
    public static void sendBridgeMessage(final @NotNull Player player, final @NotNull String msg) {
        Scheduler.runAsync(() -> {
            webhookClient.sendMessage(ChatUtils.stripColor(msg))
                .setUsername("[" + PlayerUtils.getGroupDisplayName(player) + "] " + player.getName())
                .setAvatarUrl(SkinRenderer.getRenderUrl(player, SkinRenderer.RenderType.PROPIC))
                .complete()
            ;
            utils.log(Level.INFO, "logged \"" + msg + "\"");
        });
    }
}
