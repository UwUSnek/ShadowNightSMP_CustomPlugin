package org.uwu_snek.shadownight.customMobs;

import org.bukkit.Location;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Mob;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.math.Func;
import org.uwu_snek.shadownight.utils.math.K;
import org.uwu_snek.shadownight.utils.utils;

import java.util.LinkedList;
import java.util.logging.Level;




//TODO add onSpawn and onDeath callbacks
public class MOB {
    public static LinkedList<MOB> aliveCustomMobs = new LinkedList<>();

    protected Bone root;
    protected Mob mount;
    protected float yaw = 0;

    public final static int walkCycleDuration = 8; // The duration of a walk cycle in ticks //! Must be even and >= 2 //TODO make this per-mob
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
        aliveCustomMobs.add(this);
    }


    public void spawn(final @NotNull Location spawnLocation) {
        // Make the spawn location face North and reset pitch
        final Location location = spawnLocation.clone().setDirection(new Vector(0, 0, 1));

        // Spawn the mount entity //! Baby turtle has the smallest hitbox
        mount = (Mob)location.getWorld().spawnEntity(location, EntityType.TURTLE, false);
        mount.setInvisible(true);
        mount.setSilent(true);
        mount.setAware(false);
        mount.setPersistent(true);
        mount.setCanPickupItems(false);
        mount.setPortalCooldown(Integer.MAX_VALUE);
        ((Ageable)mount).setAge(Integer.MIN_VALUE);
        mount.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 254, false, false));

        // Spawn skeleton
        root.instantMoveAll(0, -(float)mount.getHeight(), 0);
        root.spawn(mount); // Spawn bones
    }


    public void tick(){
        root.tick();
    }




    /**
     * Calculates the angle of the location <target> around the Y-Axis centered at the location <source>.
     * The angle goes from -PI to +PI, with 0 representing South.
     * @param source The location to calculate the target's angle from
     * @param target The target location
     * @return The angle expressed in radians
     */
    protected static float getTargetYaw(final @NotNull Location source, final @NotNull Location target){
        final Vector targetDirection = target.clone().subtract(source).toVector();
        return -((float)Math.atan2(targetDirection.getZ(), targetDirection.getX()) - K.PIf / 2);
        //! Subtract 90° to account for Minecraft's yaw 0 being at [0, 1] instead of the standard [1, 0]
        //! Invert angle. Math.atan2 calculates it counter-clockwise, Minecraft's is clockwise
        //! Convert to float. java.lang.Math doesn't have a float version of atan2
    }
}