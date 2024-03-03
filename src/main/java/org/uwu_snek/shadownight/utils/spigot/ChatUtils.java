package org.uwu_snek.shadownight.utils.spigot;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;

import java.util.LinkedHashSet;




public final class ChatUtils extends UtilityClass {
    public static final String serverPrefix = "§d§l[§5§lSN§d§l] §r";


    /**
     * Sends a message prefixed with the server prefix to a player.
     * @param player The player to send the message to
     * @param message The message string
     */
    @SuppressWarnings("unused")
    public static void sendMessage(final @NotNull Player player, final @NotNull String message) {
        player.sendMessage(serverPrefix + message);
    }

    /**
     * Sends an empty message to a player.
     * @param player The player to send the message to
     */
    @SuppressWarnings("unused")
    public static void newline(final @NotNull Player player) {
        player.sendMessage("");
    }

    /**
     * Sends a message containing a separator line to a player.
     * @param player The player to send the message to
     */
    @SuppressWarnings("unused")
    public static void separator(final @NotNull Player player) {
        player.sendMessage("§d§m                                                                               ");
    }




    /**
     * Returns the time duration in a readable format (days, hours, minutes, seconds)
     * @param s The time duration in seconds
     * @param useSpaces True to include spaces in the formatted string. e.g. "17d 15h 13m 5s" instead of "17d15h13m5s"
     * @return A string containing the formatted duration
     */
    @SuppressWarnings("unused")
    public static String sToDuration(final @NotNull Long s, final boolean useSpaces) {
        return sToDuration(s, useSpaces ? " " : "");
    }

    /**
     * Returns the time duration in a readable format (minutes, seconds, milliseconds)
     * @param ms The time duration in seconds
     * @param useSpaces True to include spaces in the formatted string. e.g. "13m 5s 591ms" instead of "13m5s591ms"
     * @return A string containing the formatted duration
     */
    @SuppressWarnings("unused")
    public static String msToDuration(final @NotNull Long ms, final boolean useSpaces) {
        return msToDuration(ms, useSpaces ? " " : "");
    }

    private static String sToDuration(final @NotNull Long s, final @NotNull String c) {
        return
            (s >= 86400 ? (s / 86400      + "d" + c) : "") +
            (s >= 3600  ? (s / 3600  % 24 + "h" + c) : "") +
            (s >= 60    ? (s / 60    % 60 + "m" + c) : "") +
            s                        % 60 + "s"
            ;
    }

    private static String msToDuration(final @NotNull Long m, final @NotNull String c) {
        return
            (m >= 60000 ? (m / 60000 % 60000 + "m" + c) : "") +
            (m >= 1000  ? (m / 1000  % 1000  + "s" + c) : "") +
            m                        % 1000  + "ms"
            ;
    }




    /**
     * Removes any character that is part of the Unicode Private Use Area from a string.
     * @param s The string to strip
     * @return The stripped string
     */
    @SuppressWarnings("unused")
    public static String stripPrivateCharacters(final @NotNull String s) {
        return s.replaceAll("[\uE000-\uF8FF]", "");
    }

    /**
     * Removes any standard color code and user-friendly color code from a string.
     * @param s The string to strip
     * @return The stripped string
     */
    @SuppressWarnings("unused")
    public static String stripColor(final @NotNull String s) {
        return s.replaceAll("[&§]([0-9a-fk-or])", "");
    }

    /**
     * Translates user-friendly color codes (&) to standard color codes (§).
     * @param s The string to translate
     * @return The translated string
     */
    @SuppressWarnings("unused")
    public static String translateColor(final @NotNull String s) {
        return s.replaceAll("&([0-9a-fk-or])", "§$1");
    }







    /**
     * Translates color codes (& and §) to a TextComponent that doesn't contain any legacy formatting code.
     * @param s The string to translate
     * @return The translated string as a TextComponent
     */
    @SuppressWarnings("unused")
    public static TextComponent translateColorToComponent(final @NotNull String s) {
        TextComponent output = Component.text("").decoration(TextDecoration.ITALIC, false);


        // For each part of the message
        final LinkedHashSet<TextDecoration> modifiers = new LinkedHashSet<>();
        TextColor color = TextColor.color(0xFFFFFF);
        for(String part : s.split("((?=[&§][0-9a-fk-or]))")){

            // Handle first part (no formatting code) separately
            if(!part.matches("[&§][0-9a-fk-or].*")) {
                output = output.append(Component.text(part).color(color));
                continue;
            }

            // Save code and string value
            char code = part.charAt(1);
            String value = part.substring(2);
            TextComponent r = Component.text(value);

            // Remove all modifiers if r code is used
            if(code == 'r') {
                modifiers.clear();
                color = TextColor.color(0xFFFFFF);
            }

            // Add new modifiers if specified
            else if(part.matches("[&§][k-o].*")) modifiers.add(switch (code) {
                case 'k' -> TextDecoration.OBFUSCATED;
                case 'l' -> TextDecoration.BOLD;
                case 'm' -> TextDecoration.STRIKETHROUGH;
                case 'n' -> TextDecoration.UNDERLINED;
                case 'o' -> TextDecoration.ITALIC;
                default  -> throw new RuntimeException("idk what happened");
            });

            // Set color if specified
            else if(part.matches("[&§][0-9a-f].*")) {
                modifiers.clear();
                color = TextColor.color(switch (code) {
                    case '0' -> 0x000000;
                    case '1' -> 0x0000AA;
                    case '2' -> 0x00AA00;
                    case '3' -> 0x00AAAA;
                    case '4' -> 0xAA0000;
                    case '5' -> 0xAA00AA;
                    case '6' -> 0xFFAA00;
                    case '7' -> 0xAAAAAA;
                    case '8' -> 0x555555;
                    case '9' -> 0x5555FF;
                    case 'a' -> 0x55FF55;
                    case 'b' -> 0x55FFFF;
                    case 'c' -> 0xFF5555;
                    case 'd' -> 0xFF55FF;
                    case 'e' -> 0xFFFF55;
                    case 'f' -> 0xFFFFFF;
                    default  -> throw new RuntimeException("idk what happened");
                });
            }


            // Add colo and all saved modifiers
            r = r.color(color);
            for(TextDecoration m : modifiers) {
                r = r.decorate(m);
            }

            // Append formatted part to output
            output = output.append(r);
        }


        return output;
    }
}
