package org.uwu_snek.shadownight.customMobs.implementations;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.CustomMob;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public class TestMob extends CustomMob {
    Bone top =  new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);
    Bone l =    new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);
    Bone r =    new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);
    Bone lTop = new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);
    Bone rTop = new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);

    public TestMob() {
        super();
        root.addChild(top);

        top.addChild(r);
        r.addChild(rTop);

        top.addChild(l);
        l.addChild(lTop);


        Scheduler.delay(() -> { root.move(4, 2, 0, -2); }, 20);
        Scheduler.delay(() -> {    l.move(4, -1, 1, 0); }, 28);
        Scheduler.delay(() -> {    r.move(4, 1, 1, 0); }, 36);
        Scheduler.delay(() -> { lTop.move(4, -1, 1, 0); }, 44);
        Scheduler.delay(() -> { rTop.move(4, 1, 1, 0); }, 52);

        Scheduler.delay(() -> { root.rotate(20, -(float)Math.PI / 2, 0, 1, 0); }, 60);

        Scheduler.delay(() -> { l.rotate(10,   (float)Math.PI * 0.25f, 0, 1, 0); }, 90);
        Scheduler.delay(() -> { r.rotate(10,  -(float)Math.PI * 0.25f, 0, 1, 0); }, 90);

        Scheduler.delay(() -> { l.rotate(10,   -(float)Math.PI * 0.25f, 0, 1, 0); }, 110);
        Scheduler.delay(() -> { r.rotate(10,  (float)Math.PI * 0.25f, 0, 1, 0); }, 110);

        Scheduler.delay(() -> { l.rotate(10,   (float)Math.PI * 0.25f, 0, 1, 0); }, 130);
        Scheduler.delay(() -> { r.rotate(10,  -(float)Math.PI * 0.25f, 0, 1, 0); }, 130);

        Scheduler.delay(() -> { l.rotate(10,   -(float)Math.PI * 0.25f, 0, 1, 0); }, 150);
        Scheduler.delay(() -> { r.rotate(10,  (float)Math.PI * 0.25f, 0, 1, 0); }, 150);

        //Scheduler.delay(() -> { r.rotate(10,  (float)Math.PI * 0.25f, 0, 1, 0); }, 80);
        //Scheduler.delay(() -> { r.rotate(10,  (float)Math.PI * 0.25f, 0, 1, 0); }, 90);
    }
}
