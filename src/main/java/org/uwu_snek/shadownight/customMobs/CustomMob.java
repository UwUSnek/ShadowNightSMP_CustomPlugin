package org.uwu_snek.shadownight.customMobs;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.management.Attribute;




//TODO add onSpawn and onDeath callbacks
public class CustomMob {
    private final EntityType baseEntityType;
    protected Bone root;

    protected Interaction hitbox;
    protected double hitboxWidth;
    protected double hitboxHeight;

    protected Entity baseEntity = null;

    public CustomMob(final @Nullable EntityType baseEntityType){
        this.baseEntityType = baseEntityType;
        root = new Bone();
    }

    public void summon(final @NotNull Location spawnLocation) {
        // Reset rotation and center root bone to block center
        final Location location = spawnLocation.clone().setDirection(new Vector(0, 0, -1)).add(0, 0.5, 0);

        // Spawn the base entity and the model skeleton into the world
        baseEntity = location.getWorld().spawnEntity(location, baseEntityType == null ? EntityType.INTERACTION : baseEntityType, false);
        if(baseEntity instanceof LivingEntity e) {
            e.setAI(false); // Remove Vanilla AI
            e.setSilent(true);
            //TODO set max hp
            //e.setHealth(9000);
            e.setCanPickupItems(false);
            e.setInvisible(true);
            e.setMaximumAir(999999999);
            e.setPersistent(true);
            e.setPortalCooldown(999999999);
        }
        root.summon(location, baseEntity);                       // Summon bones
        root.move(0, -(float)baseEntity.getHeight(), 0);      // Move root to floor height

        hitbox = (Interaction)location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        hitbox.setInteractionWidth(5);
        hitbox.setInteractionHeight(10);
        baseEntity.addPassenger(hitbox);
    }
}
