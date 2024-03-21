package org.uwu_snek.shadownight.customMobs;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;




//TODO add onSpawn and onDeath callbacks
public class MOB {
    protected final static float PI = (float)Math.PI;
    public final static int stepDuration = 5;

    protected Bone root;
    protected Interaction mount;

    protected double movementSpeed;
    protected double movementAmountStep;

    /**
     * Creates a new custom mob (without spawning it)
     * @param movementSpeed The movement speed of the mob expressed in blocks/s
     */
    public MOB(final double movementSpeed) {
        this.movementSpeed = movementSpeed;
        this.movementAmountStep = movementSpeed / (20d / stepDuration);
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
