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
    protected Bone root;
    protected Interaction mount;


    public CustomMob() {
        root = new Bone();
    }

    public void summon(final @NotNull Location spawnLocation) {
        // Reset rotation
        final Location location = spawnLocation.clone().setDirection(new Vector(0, 0, -1));

        // Spawn the mount entity and the model skeleton into the world
        mount = (Interaction)location.getWorld().spawnEntity(location, EntityType.INTERACTION);
        mount.setInteractionWidth(0);
        mount.setInteractionHeight(0);
        root.summon(location, mount);                       // Summon bones
        //root.move(0, -(float)baseEntity.getHeight(), 0);      // Move root to floor height
    }
}
