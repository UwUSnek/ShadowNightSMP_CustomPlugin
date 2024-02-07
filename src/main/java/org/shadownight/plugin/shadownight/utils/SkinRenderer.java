package org.shadownight.plugin.shadownight.utils;

import org.bukkit.OfflinePlayer;
import org.shadownight.plugin.shadownight.ShadowNight;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;


public class SkinRenderer {
    public enum RenderType {
        PROPIC,
        FULL
    }
    private static String cachePath;
    private static final HashMap<RenderType, HashMap<UUID, BufferedImage>> runtimeCache = new HashMap<>();



    public static void init(){
        cachePath = ShadowNight.plugin.getDataFolder() + "/skinRenders/";
        try {
            for(RenderType renderType : RenderType.values()) {
                runtimeCache.put(renderType, new HashMap<>());
                Files.createDirectories(Paths.get(cachePath + renderType.name()));
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }




    private static BufferedImage getRender(OfflinePlayer player, String type, RenderType renderType){
        UUID uuid = player.getUniqueId();
        HashMap<UUID, BufferedImage> typeRuntimeCache = runtimeCache.get(renderType);
        BufferedImage playerRuntimeCache = typeRuntimeCache.get(uuid);


        // Load from runtime cache if it exists
        if (playerRuntimeCache == null) {
            String filePath = cachePath + renderType.name() + "/" + uuid + ".png";
            File file = new File(filePath);

            // If file cache doesn't exist, create a new render, save it and load the data
            if (!file.exists()) {
                playerRuntimeCache = saveImage("https://starlightskins.lunareclipse.studio/skin-render/ultimate/" + uuid + "/" + type, filePath);
            }
            // If it does, load the data from the file
            else {
                try {
                    playerRuntimeCache = ImageIO.read(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        // Return the data
        return playerRuntimeCache;
    }


    public static BufferedImage getRenderPropic(OfflinePlayer player) {
        return getRender(player, "full?cameraPosition={\"x\":\"20\",\"y\":\"25\",\"z\":\"-35\"}&cameraFocalPoint={\"x\":\"0\",\"y\":\"46\",\"z\":\"0\"}", RenderType.PROPIC);
    }
    public static BufferedImage getRenderFull(OfflinePlayer player) {
        return getRender(player, "full", RenderType.FULL);
    }





    private static BufferedImage saveImage(String imageUrl, String destinationFile) {
        try {
            URL url = new URL(imageUrl);
            InputStream inputStream = url.openStream();
            OutputStream outputFile = new FileOutputStream(destinationFile);

            byte[] buffer = new byte[2048];
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
            e.printStackTrace();
            return null;
        }
    }
}
