package org.uwu_snek.shadownight.customMobs;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



//TODO add onSpawn and onDeath callbacks
public class CustomMob {
    private final EntityType baseEntityType;
    private final boolean keepBaseEntityAi;

    protected Entity baseEntity = null;
    protected Bone root = null;


    public CustomMob(final @Nullable EntityType baseEntityType, final boolean keepBaseEntityAi){
        this.baseEntityType = baseEntityType;
        this.keepBaseEntityAi = keepBaseEntityAi;
    }

    public void summon(final @NotNull Location spawnLocation) {
        // Reset rotation and center root bone to block center
        final Location location = spawnLocation.clone().setDirection(new Vector(1, 0, 0)).add(0, 0.5, 0);

        // Spawn the base entity and the model skeleton into the world
        baseEntity = location.getWorld().spawnEntity(location, baseEntityType == null ? EntityType.INTERACTION : baseEntityType);
        if(!keepBaseEntityAi && baseEntity instanceof LivingEntity e) e.setAI(false);
        root.summon(location);
    }
}
