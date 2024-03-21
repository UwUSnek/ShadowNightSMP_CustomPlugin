package org.uwu_snek.shadownight.customMobs;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;




//TODO add onSpawn and onDeath callbacks
public class MOB {
    protected final static float PI = (float)Math.PI;

    protected Bone root;
    protected Interaction mount;


    public MOB() {
        root = new RootBone();
    }

    public void spawn(final @NotNull Location spawnLocation) {
        // Make the spawn location face North and reset pitch
        final Location location = spawnLocation.clone().setDirection(new Vector(0, 0, 1));

        // Spawn the mount entity and the model skeleton into the world
        mount = (Interaction)location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        mount.setInteractionWidth(0);
        mount.setInteractionHeight(0);
        root.spawn(mount); // Spawn bones
    }
}
