package org.uwu_snek.shadownight.utils;

import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.ShadowNight;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;




public final class utils extends UtilityClass {
    public synchronized static void log(final @NotNull Level level, final @NotNull String message) { //TODO remove synchronized
        ShadowNight.plugin.getLogger().log(level, message);
    }



    public static byte[] imageToByteArray(final @NotNull BufferedImage image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", stream);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return stream.toByteArray();
        // ByteArrayOutputStreams don't need to be closed (the documentation says so)
    }


    /**
     * Creates a 64-bit hash code from an array of bytes.
     * @param data The data to create the hash code from
     * @return The hash code
     */
    public static long createHash64(final byte @NotNull [] data) {
        long h = 0xBB40E64DA205B064L;
        final long hmult = 7664345821815920749L;
        final long[] ht = createLookupTable();
        for (int len = data.length, i = 0; i < len; i++) {
            h = (h * hmult) ^ ht[data[i] & 0xff];
        }
        return h;
    }

    private static long[] createLookupTable() {
        final long[] byteTable = new long[256];
        long h = 0x544B2FBACAAF1684L;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 31; j++) {
                h = (h >>> 7) ^ h;
                h = (h << 11) ^ h;
                h = (h >>> 10) ^ h;
            }
            byteTable[i] = h;
        }
        return byteTable;
    }




    public enum MoonPhase {
        FULL,
        GIBBOUS,
        QUARTER,
        CRESCENT,
        NEW
    }
    public static int getMoonPhaseValue(final @NotNull World world) {
        int days = (int)world.getFullTime() / 24000;
        return days % 8;
    }
    public static MoonPhase getMoonPhase(final @NotNull World world) {
        int phase = getMoonPhaseValue(world);
        return switch (phase) {
            case 0 -> MoonPhase.FULL;
            case 1, 7 -> MoonPhase.GIBBOUS;
            case 2, 6 -> MoonPhase.QUARTER;
            case 3, 5 -> MoonPhase.CRESCENT;
            case 4 -> MoonPhase.NEW;
            default -> throw new IllegalStateException("Unexpected value: " + phase);
        };
    }

    public static double getRegionalDifficulty(final @NotNull Difficulty difficulty, final @NotNull Location location) {
        if (difficulty == Difficulty.PEACEFUL) return 0;
        World world = location.getWorld();
        if(world == null) throw new RuntimeException("Location's world is null");


        // Calculate time of day
        double daytimeFactor;
        long fullDaytime = world.getFullTime();
        /**/ if (fullDaytime > 63 * 24000L) daytimeFactor = 0.25d;
        else if (fullDaytime <  3 * 24000L) daytimeFactor = 0d;
        else daytimeFactor = ((double)fullDaytime - 72000L) / 5760000;

        // Calculate chunk factor
        long inhabitedTime = location.getChunk().getInhabitedTime();
        double chunkFactor = (inhabitedTime > 50000L) ? 1d : (double)inhabitedTime / 3600000;
        if (difficulty == Difficulty.NORMAL || difficulty == Difficulty.EASY) chunkFactor *= 0.75;

        // Calculate moon phase
        int moonPhase = getMoonPhaseValue(world);
        chunkFactor += Math.min((double)moonPhase / 4, daytimeFactor);


        // Calculate the final difficulty
        if (difficulty == Difficulty.EASY) chunkFactor *= 0.5;
        double regionalDifficulty = 0.75 + daytimeFactor + chunkFactor;
        if (difficulty == Difficulty.NORMAL) regionalDifficulty *= 2;
        if (difficulty ==   Difficulty.HARD) regionalDifficulty *= 3;

        // Return value
        return regionalDifficulty;
    }
}