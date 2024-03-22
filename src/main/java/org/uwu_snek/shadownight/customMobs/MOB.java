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
    protected float yaw = 0;

    public final static int walkCycleDuration = 10; // The duration of a walk cycle in ticks
    protected double walkingSpeed;                  // The walking speed of the mob in blocks/s
    protected double walkCycleMoveAmount;           // The number of blocks the mob should move each walk cycle

    /**
     * Creates a new custom mob (without spawning it)
     * @param movementSpeed The movement speed of the mob expressed in blocks/s
     */
    public MOB(final double movementSpeed) {
        this.walkingSpeed = movementSpeed;
        this.walkCycleMoveAmount = movementSpeed / (20d / walkCycleDuration);
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


    /**
     * Calculates the angle of the location <target> around the Y-Axis centered at the location <source>,
     * With 0 being North and increasing as the target moves clockwise (looking up) around the axis location.
     * @param source The location to calculate the target's angle from
     * @param target The target location
     * @return The angle expressed in radians
     */
    protected static float getTargetYaw(final @NotNull Location source, final @NotNull Location target){
        final Vector targetDirection = target.clone().subtract(source).toVector();
        return (float)-(Math.atan2(targetDirection.getZ(), targetDirection.getX()) - PI / 2);
        //! Subtract 90° to account Minecraft's rotation 0 being at [0, 1] instead of the standard [1, 0]
        //! Invert angle. Math.atan2 calculates it counter-clockwise, Minecraft's is clockwise
        //! Convert to float. java.lang.Math doesn't have a float version of atan2
    }
}
