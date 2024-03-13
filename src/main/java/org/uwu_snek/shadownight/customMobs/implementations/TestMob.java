package org.uwu_snek.shadownight.customMobs.implementations;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.CustomMob;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public class TestMob extends CustomMob {
    Bone top = new DisplayBone(1);
    Bone l =   new DisplayBone(1);
    Bone r =   new DisplayBone(1);

    public TestMob() {
        super(EntityType.HUSK, true);
        root.addChild(top);
        top.addChild(r);
        top.addChild(l);

        Scheduler.delay(() -> { root.move(2, 0, 0); }, 40);
    }
}
