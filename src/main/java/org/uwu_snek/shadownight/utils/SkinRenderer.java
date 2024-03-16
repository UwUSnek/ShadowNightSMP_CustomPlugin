package org.uwu_snek.shadownight.utils;

import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.ShadowNight;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;


public final class SkinRenderer extends UtilityClass {
    public enum RenderType {
        PROPIC,
        FULL
    }
    private static String cachePath;
    private static final HashMap<RenderType, HashMap<UUID, BufferedImage>> runtimeCache = new HashMap<>();


    /**
     * Initializes the skin renderer. This must be called exactly once before using any method of this class
     */
    public static void init(){
        cachePath = ShadowNight.plugin.getDataFolder() + "/skinRenders/";
        try {
            for(RenderType renderType : RenderType.values()) {
                runtimeCache.put(renderType, new HashMap<>());
                Files.createDirectories(Paths.get(cachePath + renderType.name()));
            }
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns the URL of the render based on the render type.
     * This method doesn't cache renders.
     * @param player The player to render
     * @param renderType The type of render
     * @return The generated URL
     */
    @SuppressWarnings("unused") public static String getRenderUrl(final @NotNull OfflinePlayer player, final @NotNull RenderType renderType){
        final UUID uuid = player.getUniqueId();
        final String type = switch (renderType) {
            case PROPIC -> "full?cameraPosition={\"x\":\"20\",\"y\":\"25\",\"z\":\"-35\"}&cameraFocalPoint={\"x\":\"0\",\"y\":\"46\",\"z\":\"0\"}";
            case FULL   -> "full";
        };
        return "https://starlightskins.lunareclipse.studio/render/ultimate/" + uuid + "/" + type;
    }



    /**
     * Creates a new skin render and returns its URI.
     * Renders are cached to improve performance.
     * @param player The player to render
     * @param renderType The type of render
     * @return The URI of the generated render
     */
    @SuppressWarnings("unused") public static String getRenderUri(final @NotNull OfflinePlayer player, final @NotNull RenderType renderType){
        final String type = getRenderUrl(player, renderType);
        final UUID uuid = player.getUniqueId();
        final String filePath = cachePath + renderType.name() + "/" + uuid + ".png";
        final File file = new File(filePath);

        // If file cache doesn't exist, create a new render, save it and load the data
        if (!file.exists()) {
            saveImage("https://starlightskins.lunareclipse.studio/skin-render/ultimate/" + uuid + "/" + type, filePath);
        }
        return filePath;
    }




    /**
     * Creates a new skin render and returns it as an image.
     * Renders are cached to improve performance.
     * @param player The player to render
     * @param renderType The type of render
     * @return The generated render image
     */
    @SuppressWarnings("unused") public static BufferedImage getRender(final @NotNull OfflinePlayer player, final @NotNull RenderType renderType){
        final String type = getRenderUrl(player, renderType);
        final UUID uuid = player.getUniqueId();
        final HashMap<UUID, BufferedImage> typeRuntimeCache = runtimeCache.get(renderType);
        BufferedImage playerRuntimeCache = typeRuntimeCache.get(uuid);


        // Load from runtime cache if it exists
        if (playerRuntimeCache == null) {
            final String filePath = cachePath + renderType.name() + "/" + uuid + ".png";
            final File file = new File(filePath);

            // If file cache doesn't exist, create a new render, save it and load the data
            if (!file.exists()) {
                playerRuntimeCache = saveImage("https://starlightskins.lunareclipse.studio/skin-render/ultimate/" + uuid + "/" + type, filePath);
            }
            // If it does, load the data from the file
            else {
                try {
                    playerRuntimeCache = ImageIO.read(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Return the data
        return playerRuntimeCache;
    }









    private static BufferedImage saveImage(final @NotNull String imageUrl, final @NotNull String destinationFile) {
        try {
            final URL url = new URL(imageUrl);
            final InputStream inputStream = url.openStream();
            final OutputStream outputFile = new FileOutputStream(destinationFile);

            final byte[] buffer = new byte[2048];
            int length;


            // Save image to file
            while ((length = inputStream.read(buffer)) != -1) {
                outputFile.write(buffer, 0, length);
            }
            inputStream.close();
            outputFile.close();

            // Save file to memory
            return ImageIO.read(new File(destinationFile));
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
