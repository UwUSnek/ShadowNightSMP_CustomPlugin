package org.shadownight.plugin.shadownight.dungeons;


import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.World;
import org.bukkit.WorldType;
import org.shadownight.plugin.shadownight.ShadowNight;

public class Dungeon {
    static final MVWorldManager worldManager = ShadowNight.mvcore.getMVWorldManager();


    public Dungeon() {
        worldManager.addWorld(
            "test_dungeon",
            World.Environment.NORMAL,
            null,
            WorldType.NORMAL,
            false,
            "VoidGenerator"
        );
    }
}
