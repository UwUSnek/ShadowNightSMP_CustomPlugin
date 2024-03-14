package org.uwu_snek.shadownight.customMobs.implementations;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.CustomMob;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public class TestMob extends CustomMob {
    Bone top =  new DisplayBone(1, 1.1f, 1.1f);
    Bone l =    new DisplayBone(1, 1.1f, 1.1f);
    Bone r =    new DisplayBone(1, 1.1f, 1.1f);
    Bone lTop = new DisplayBone(1, 1.1f, 1.1f);
    Bone rTop = new DisplayBone(1, 1.1f, 1.1f);

    public TestMob() {
        super();
        root.addChild(top);

        top.addChild(r);
        r.addChild(rTop);

        top.addChild(l);
        l.addChild(lTop);


        Scheduler.delay(() -> { root.move(0, 0, -2); }, 40);
        Scheduler.delay(() -> { l.move(1, 1, 0); }, 42);
        Scheduler.delay(() -> { r.move(-1, 1, 0); }, 44);
        Scheduler.delay(() -> { lTop.move(0, 1, 0); }, 46);
        Scheduler.delay(() -> { rTop.move(0, 1, 0); }, 48);

        Scheduler.delay(() -> { root.move(2, 0, 0); }, 40);
    }
}
