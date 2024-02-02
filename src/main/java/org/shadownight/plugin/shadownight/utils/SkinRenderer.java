package org.shadownight.plugin.shadownight.utils;

import org.bukkit.entity.Player;
import org.shadownight.plugin.shadownight.ShadowNight;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;


public class SkinRenderer {
    private static String cachePath;
    private static HashMap<UUID, Byte[]> runtimeCachePropic;    //TODO cache and make getRender functions return a byte array
    private static HashMap<UUID, Byte[]> runtimeCacheFull;      //TODO cache and make getRender functions return a byte array

    public static void init(){
        cachePath = ShadowNight.plugin.getDataFolder() + "/skinRenders/";
        try {
            Files.createDirectories(Paths.get(cachePath + "propic"));
            Files.createDirectories(Paths.get(cachePath + "full"));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }




    private static File getRender(Player player, String type, String typeName){
        UUID uuid = player.getUniqueId();
        String filePath = cachePath + typeName + "/" + uuid + ".png";

        File output = new File(filePath);
        if(output.exists()) return output;
        else {
            saveImage("https://starlightskins.lunareclipse.studio/skin-render/ultimate/" + uuid + "/" + type, filePath);
            return new File(filePath);
        }
    }


    public static File getRenderPropic(Player player) {
        return getRender(player, "full?cameraPosition={\"x\":\"20\",\"y\":\"25\",\"z\":\"-35\"}&cameraFocalPoint={\"x\":\"0\",\"y\":\"46\",\"z\":\"0\"}", "propic");
    }
    public static File getRenderFull(Player player) {
        return getRender(player, "full", "full");
    }





    public static void saveImage(String imageUrl, String destinationFile) {
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
