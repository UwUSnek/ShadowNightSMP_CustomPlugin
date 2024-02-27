package org.uwu_snek.shadownight.utils.spigot;


import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;



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
}
