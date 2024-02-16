package org.shadownight.plugin.shadownight.chatManager.discord;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.shadownight.plugin.shadownight.chatManager.ChatManager;
import org.shadownight.plugin.shadownight.economy.Economy;
import org.shadownight.plugin.shadownight.utils.SkinRenderer;
import org.shadownight.plugin.shadownight.utils.spigot.Chat;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.Objects;
import java.util.logging.Level;

import static org.shadownight.plugin.shadownight.chatManager.discord.BotManager.*;


public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getChannel().getId().equals(BotManager.bridgeChannel.getId())) {
            User author = event.getAuthor();
            if(!author.isBot()) {
                Bukkit.broadcastMessage("§9§l[Discord]§9 " + author.getEffectiveName() + ChatManager.playerMessageConnector + event.getMessage().getContentStripped());
            }
        }
    }





    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        utils.log(Level.INFO, "Detected slash command");


        // Send the user to the right channel if they sent the command in a different one
        if(!Objects.equals(event.getChannelId(), commandsChannelId) && !Objects.equals(event.getChannelId(), testCommandsChannelId)) {
            event.deferReply().queue(); // Tell discord we received the command
            utils.log(Level.INFO, "Slash command was sent in the wrong channel");
            event.getHook().sendMessage("Please, use the <#" + commandsChannelId + "> channel.").queue();
        }


        // Manage info command
        else if (Objects.equals(event.getChannelId(), commandsChannel.getId())) {
            event.deferReply().queue(); // Tell discord we received the command
            utils.log(Level.INFO, "Managing profile slash command...");
            if (event.getName().equals("profile")) {
                String ign = Objects.requireNonNull(event.getOption("ign"), "ign option is null").getAsString();
                @SuppressWarnings("deprecation") // Using getOfflinePlayer the way it's meant to be used, no need to warn
                OfflinePlayer player = Bukkit.getOfflinePlayer(ign);


                if (!player.hasPlayedBefore()) {
                    event.getHook().sendMessage(MessageCreateData.fromEmbeds(new EmbedBuilder()
                        .setColor(embedColor)
                        .addField("Player not found", "The player \"" + ign + "\" doesn't exist or has never player on Shadow Night.", false)
                        .build()
                    )).queue();
                }
                else{
                    event.getHook().sendMessage(MessageCreateData.fromEmbeds(new EmbedBuilder()
                        .setColor(embedColor)
                        .addField(player.getName() + "'s profile", """
                            - Rank: `%s`
                            - Playtime: %s
                            - Status: %s"""
                            .formatted(
                                utils.getGroupDisplayNameOffline(player),
                                Chat.sToDuration(player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20L, true),
                                player.isOnline() ? "**Online**" : "_Offline_"
                            ), false)
                        .addField("───────────────────────────────────────────────────────", "", false)
                        .addField("Player kills", "⚔️ " + player.getStatistic(Statistic.PLAYER_KILLS), true)
                        .addField("Damage dealt", "💮 " + player.getStatistic(Statistic.DAMAGE_DEALT), true)
                        .addField("Balance", "🪙 " + Economy.getBalance(player), true)
                        .addField("───────────────────────────────────────────────────────", "", false)
                        .setThumbnail(SkinRenderer.getRenderUrl(player, SkinRenderer.RenderType.FULL))
                        .build()
                    )).queue();
                }
            }
        }
    }
}