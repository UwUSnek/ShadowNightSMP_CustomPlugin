package org.uwu_snek.shadownight.customMobs;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.util.Transformation;
import org.jetbrains.annotations.NotNull;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;

import java.util.ArrayList;




public class Bone {
    protected Vector3f locPos = new Vector3f(0, 0, 0); // The location of this bone's origin relative to its parent
    protected Vector3f absPos = new Vector3f(0, 0, 0); // The location of this bone's origin relative to the root bone


    private final ArrayList<Bone> children = new ArrayList<>();
    public Bone(){ }


    public void summon(final @NotNull Location location, final @NotNull Entity mount){
        for(Bone b : children) b.summon(location, mount);
    }




    public void addChild(@NotNull Bone bone) {
        children.add(bone);
        //bone.parent = this;
    }



    public final void move(final float x, final float y, final float z) {
        move(new Vector3f(x, y, z));
    }
    public void move(final @NotNull Vector3f v) {
        locPos.add(v);
        absPos.add(v);
        for(Bone c : children) c.move(v);
    }
}
