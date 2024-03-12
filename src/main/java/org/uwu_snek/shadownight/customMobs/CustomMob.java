package org.uwu_snek.shadownight.customMobs;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



//TODO add onSpawn and onDeath callbacks
public class CustomMob {
    private final EntityType baseEntityType;
    private final boolean keepBaseEntityAi;

    protected Entity baseEntity = null;
    protected final Bone root;


    public CustomMob(final @Nullable EntityType baseEntityType, final boolean keepBaseEntityAi, final @NotNull Bone root){
        this.baseEntityType = baseEntityType;
        this.keepBaseEntityAi = keepBaseEntityAi;
        this.root = root;
    }

    public void summon(final @NotNull Location spawnLocation) {
        baseEntity = spawnLocation.getWorld().spawnEntity(spawnLocation, baseEntityType == null ? EntityType.INTERACTION : baseEntityType);
        if(!keepBaseEntityAi && baseEntity instanceof LivingEntity e) e.setAI(false);
        root.summon(spawnLocation);
    }
}
