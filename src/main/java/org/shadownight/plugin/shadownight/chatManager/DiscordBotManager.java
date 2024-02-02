package org.shadownight.plugin.shadownight.chatManager;


import org.bukkit.entity.Player;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.channel.TextableRegularServerChannel;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.WebhookBuilder;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.SkinRenderer;
import org.shadownight.plugin.shadownight.utils.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;


public class DiscordBotManager {
    private static DiscordApi api;
    private static TextableRegularServerChannel channel;
    private static final String bridgeId = "1202610915694870558";
    private static final String testBridgeId = "1202960128421138494";



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
        Optional<TextChannel> _channel = api.getTextChannelById(ShadowNight.plugin.getServer().getIp().equals(utils.serverIp) ? bridgeId : testBridgeId);
        if(_channel.isEmpty()) throw new RuntimeException("An error occurred while trying to initialize the Discord Bot Manager: Channel not found");
        else {
            channel = (TextableRegularServerChannel) _channel.get();
        }
    }




    public static void sendBridgeMessage(String msg) {
        channel.sendMessage(utils.stripColor(msg));
    }
    public static void sendBridgeMessage(Player player, String msg) {
        // Get group display name (why is this so complex omg)
        String groupDisplayName =
            Objects.requireNonNull(ShadowNight.lpApi.getGroupManager().getGroup(
                Objects.requireNonNull(ShadowNight.lpApi.getPlayerAdapter(Player.class)
                    .getUser(player)
                    .getCachedData()
                    .getMetaData()
                    .getPrimaryGroup()
                    , "Luckperms primary group id is null")
            ), "Luckperms group object is null").getDisplayName()
        ;


        // Create webhook and send message, then delete it
        IncomingWebhook webhook = new WebhookBuilder(channel)
            .setName("[" + groupDisplayName + "] " + player.getName())
            .setAvatar(SkinRenderer.getRenderPropic(player))
            .create().join()
        ;
        webhook.sendMessage(utils.stripColor(msg));
        webhook.delete();
    }
}
