package org.shadownight.plugin.shadownight.utils;

import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;




public final class utils extends UtilityClass {
    public static void log(final @NotNull Level level, final @NotNull String message) {
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
}