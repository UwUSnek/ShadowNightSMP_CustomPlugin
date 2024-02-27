package org.uwu_snek.shadownight.chatManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.chatManager.discord.BotManager;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.ChatUtils;
import org.uwu_snek.shadownight.utils.spigot.PlayerUtils;

import static java.lang.Character.toLowerCase;

public final class ChatManager extends UtilityClass {
    public static final String playerMessageConnector = " §8➟§f ";
    /* add this to hover event
    - '%player_name%'
    - '&7World: &b%world%'
    - '&7Rank: %luckperms_prefix%'
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static final String[] blockedWords = new String[]{
        "nigger", "niggers", "niger", "nigers", "nigga", "niggas",
        "cunt", "cunts",
        "whore", "whores", "bitch", "slut", "sluts",
        "retard", "retards", "faggot", "faggots",
        "cock", "cocks", "dick", "dicks", "penis", "penises",

        "wixxer", "wixer", "wichser",
        "nutte", "hurensohn",
    };



    // Returns the message with the offending part highlighted in red if it contains the word <word>
    // Returns null if it doesn't
    @SuppressWarnings("SpellCheckingInspection")
    static private String checkWord(@NotNull String msg, @NotNull final String word) {
        msg = ChatUtils.stripColor(msg);
        final String msg_clean = msg.replaceAll("[ _\\-\t]", " ").replaceAll("[|│]", "i").replaceAll("1", "i").replaceAll("0", "o").replaceAll("3", "e").replaceAll("4", "a");


        try {
            //  text wwwww w orddddd text       word
            //  ^^^^^^                          ^
            for (int i = 0; i < msg_clean.length(); ++i) {
                int repeatedFirst = 0;      // Account for the last ++ done when detecting the 2nd letter of the blocked word
                int repeatedLast = 0;       // This one already starts 1 after
                int spaces = 0;
                boolean spaceBefore;        // defaults to = false
                boolean spaceAfter;         // defaults to = false

                int j = 0;
                int local_i = 0;
                int start, end;


                //  text wwwww w orddddd text       word
                //       ^^^^^^^^-                  ^-
                // Find repeated first characters, if any
                spaceBefore = i < 1 || msg_clean.charAt(i - 1) == ' ';
                if(!(toLowerCase(msg_clean.charAt(i)) == toLowerCase(word.charAt(0)))) continue;
                while (toLowerCase(msg_clean.charAt(i)) == toLowerCase(word.charAt(0))) {
                    ++i; ++local_i;
                    ++repeatedFirst;
                }
                ++j;
                if(local_i > 0 && (repeatedFirst == 0)) continue;




                //  text wwwww w orddddd text       word
                //               ^^-                 ^^-
                // Find normal word ignoring spaces
                start = i - 1;
                while (msg_clean.charAt(i) == ' ') {
                    ++i;
                    spaceBefore = true;
                }
                while (j < word.length() - 1 && toLowerCase(msg_clean.charAt(i)) == toLowerCase(word.charAt(j))) {
                    ++i;
                    ++j;
                    while (i < msg_clean.length() && msg_clean.charAt(i) == ' ') {
                        ++i;
                        ++spaces;
                    }
                }
                if(j < word.length() - 1) continue;
                end = i + 1;


                //  text wwwww w orddddd text       word
                //                 ^^^^^-              ^
                // Find repeated last characters, if any
                while (i < msg_clean.length() && toLowerCase(msg_clean.charAt(i)) == toLowerCase(word.charAt(j))) {
                    ++i;
                    ++repeatedLast;
                }
                spaceAfter = i == msg_clean.length() || msg_clean.charAt(i) == ' ';


                // Check results
                //return !(((spaceBefore && repeatedFirst > 1) || (spaceAfter && repeatedLast > 1)) || ((spaceBefore  && repeatedFirst <= 1) && (spaceAfter && repeatedLast <= 1)) || (spaces == 0 && (spaceBefore || spaceAfter)));
                if(repeatedFirst == 0 || repeatedLast == 0 || (!spaceAfter && !spaceBefore) || (!spaceAfter && spaces > 0 && repeatedFirst <= 1) || (!spaceBefore && spaces > 0 && repeatedLast <= 1)){
                    return null;
                }
                else return new StringBuilder(msg).insert(end, "§f").insert(start, "§c").insert(0, "\"§f").append("\"").toString();
            }
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
        return null;
    }




    /**
     * Check if a string contains blocked words.
     * Sends a warning message to the player if it does.
     * @param player The player that sent the message
     * @param msg The message string
     * @return True if the string doesn't contain any blocked word, false otherwise
     */
    public static boolean checkBlockedWords(@NotNull final Player player, @NotNull final String msg) {
        for (String word : blockedWords) {
            final String offendingMessage = checkWord(msg, word);
            if(offendingMessage != null) {
                ChatUtils.sendMessage(player, "§c§lYour message has not been sent because it contained a blocked word. Please, be respectful in chat!");
                ChatUtils.sendMessage(player, offendingMessage);
                return false;
            }
        }
        return true;
    }



    /**
     * Sends a direct message to a player.
     * @param player The player that sent the message
     * @param target The player that has to receive the message.
     *               If this is null, an error message is sent to <player>
     * @param msg The message string
     */
    public static void sendDm(final @NotNull Player player, @Nullable final Player target, final @NotNull String msg) {
        if (target == null) player.sendMessage("§cThe player you are trying to message is offline!");
        else if (ChatManager.checkBlockedWords(player, msg)) {
            CMD_r.lastDmFrom.put(target.getName(), player.getName());
            String strippedMsg = ChatUtils.stripPrivateCharacters(ChatUtils.stripColor(msg));
            player.sendMessage("⬅ §dTo " + target.getName() + ": " + strippedMsg);
            target.sendMessage("➡ §dFrom " + player.getName() + ": " + strippedMsg);
        }
    }


    /**
     * Manages a player's message and either blocks it or sends it to a chat channel depending on its contents and on the player's state.
     * @param event The chat event
     */
    public static void processPlayerMessage(final @NotNull AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        final String msg = event.getMessage();

        final Player player = event.getPlayer();
        final String targetName = CMD_msg.openDms.get(player.getName());
        if (targetName == null) {
            final String strippedMsg = ChatUtils.stripPrivateCharacters(event.getPlayer().hasPermission("group.vip") ? ChatUtils.translateColor(msg) : ChatUtils.stripColor(msg));
            if (checkBlockedWords(player, strippedMsg)) {
                Bukkit.broadcastMessage(PlayerUtils.getFancyName(event.getPlayer()) + playerMessageConnector + strippedMsg);
                BotManager.sendBridgeMessage(event.getPlayer(), strippedMsg);
            }
        }
        // Blocked words and null target are checked by sendDm
        else sendDm(player, Bukkit.getPlayer(targetName), ChatUtils.stripColor(msg));
    }
}
