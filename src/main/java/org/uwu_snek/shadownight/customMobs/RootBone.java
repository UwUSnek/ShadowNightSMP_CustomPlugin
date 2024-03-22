package org.uwu_snek.shadownight.customMobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;




public final class RootBone extends Bone {
    public RootBone() {
        super();
    }

    @Override
    protected Vector3f getAbsPos(){ //FIXME cache abs pos to not recalculate it every single time
        return new Vector3f(origin).add(locPos);
    }


    @Override
    public void flushUpdates(){
        for(Bone c : children) c.flushUpdates();
    }
}
