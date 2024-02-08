package org.shadownight.plugin.shadownight.chatManager;


import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;


public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getChannel().getId().equals(DiscordBotManager.bridgeChannel.getId())) {
            User author = event.getAuthor();
            if(!author.isBot()) {
                Bukkit.broadcastMessage("§9§l[Discord]§9 " + author.getEffectiveName() + ChatManager.playerMessageConnector + event.getMessage().getContentStripped());
            }
        }
    }
}
