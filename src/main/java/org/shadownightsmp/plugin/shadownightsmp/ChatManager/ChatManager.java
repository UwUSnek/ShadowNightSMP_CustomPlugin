package org.shadownightsmp.plugin.shadownightsmp.ChatManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.shadownightsmp.plugin.shadownightsmp.utils.utils;

import static java.lang.Character.toLowerCase;

public class ChatManager {
    /* add this to hover event
    - '%player_name%'
    - '&7World: &b%world%'
    - '&7Rank: %luckperms_prefix%'
     */
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
    static private String checkWord(String msg, String word) {
        msg = utils.stripColor(msg);
        String msg_clean = msg.replaceAll("[ _\\-\t]", " ").replaceAll("[|│]", "i").replaceAll("1", "i").replaceAll("0", "o").replaceAll("3", "e").replaceAll("4", "a");


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


    // Returns true if the message doesn't contain any of the blocked words.
    // If it does, a warning is sent to the player and false is returned
    static public boolean checkBlockedWords(Player player, String msg) {
        for (String word : blockedWords) {
            String offendingMessage = checkWord(msg, word);
            if(offendingMessage != null) {
                utils.sendMessage(player, "§c§lYour message has not been sent because it contained a blocked word. Please, be respectful in chat!");
                utils.sendMessage(player, offendingMessage);
                return false;
            }
        }
        return true;
    }


    // Sends a dm, also checks that target is not null. Sends an error to the player if that's the case
    public static void sendDm(Player player, Player target, String msg) {
        if (target == null) player.sendMessage("§cThe player you are trying to message is offline!");
        else if (ChatManager.checkBlockedWords(player, msg)) {
            CMD_r.lastDmFrom.put(target.getName(), player.getName());
            String strippedMsg = utils.stripColor(msg);
            player.sendMessage("⬅ §dTo " + utils.stripColor(utils.getFancyName(target)) + ": " + strippedMsg);
            target.sendMessage("➡ §dFrom " + utils.stripColor(utils.getFancyName(player)) + ": " + strippedMsg);
        }
    }


    static public void processPlayerMessage(AsyncPlayerChatEvent event) {
        event.setCancelled(true);
        String msg = event.getMessage();

        Player player = event.getPlayer();
        String targetName = CMD_msg.openDms.get(player.getName());
        if (targetName == null) {
            msg = event.getPlayer().hasPermission("group.vip") ? utils.translateColor(msg) : utils.stripColor(msg);
            if (checkBlockedWords(player, msg)) Bukkit.broadcastMessage(utils.getFancyName(event.getPlayer()) + " §8➟ §r" + msg);
        }
        // Blocked words and null target are checked by sendDm
        else sendDm(player, Bukkit.getPlayer(targetName), utils.stripColor(msg));
    }
}
