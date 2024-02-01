package org.shadownight.plugin.shadownight.ChatManager;


import net.luckperms.api.LuckPerms;
import org.bukkit.entity.Player;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.channel.TextableRegularServerChannel;
import org.javacord.api.entity.intent.Intent;
import org.javacord.api.entity.webhook.IncomingWebhook;
import org.javacord.api.entity.webhook.Webhook;
import org.javacord.api.entity.webhook.WebhookBuilder;
import org.javacord.api.event.channel.server.text.WebhooksUpdateEvent;
import org.javacord.api.listener.channel.server.text.WebhooksUpdateListener;
import org.javacord.api.listener.message.MessageCreateListener;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.utils;

import java.io.Console;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;


public class DiscordBotManager {
    private static DiscordApi api;
    private static TextableRegularServerChannel channel;



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
            //.addIntents(Intent.MESSAGE_CONTENT)
            .setAllIntents()
            .login().join()
        ;


        // Get output channel
        Optional<TextChannel> _channel = api.getTextChannelById("1202610915694870558");
        if(_channel.isEmpty()) throw new RuntimeException("An error occurred while trying to initialize the Discord Bot Manager: Channel not found");
        else {
            channel = (TextableRegularServerChannel) _channel.get();
        }
    }




    public static void sendBridgeMessage(String msg) {
        channel.sendMessage(utils.stripColor(msg));
    }
    public static void sendBridgeMessage(Player player, String msg) {
        String groupDisplayName = ShadowNight.lpApi.getGroupManager().getGroup(ShadowNight.lpApi.getPlayerAdapter(Player.class).getUser(player).getCachedData().getMetaData().getPrimaryGroup()).getDisplayName();
        IncomingWebhook webhook = new WebhookBuilder(channel)
            .setName("[" + groupDisplayName + "] " + player.getName())
            .create().join()
        ;
        webhook.sendMessage(utils.stripColor(msg));
        webhook.delete();
    }
}
