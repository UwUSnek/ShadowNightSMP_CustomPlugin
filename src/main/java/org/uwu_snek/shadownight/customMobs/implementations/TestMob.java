package org.uwu_snek.shadownight.customMobs.implementations;

import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.CustomMob;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public class TestMob extends CustomMob {
    Bone center = new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);
    Bone r0     = new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);
    Bone r1     = new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);
    Bone r2     = new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);
    Bone r3     = new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);
    Bone r4     = new DisplayBone(_mob_part_type.TEST_PART, 1.1f, 1.1f);

    public TestMob() {
        super();
        root.addChild(center);
        center.addChild(r0);
        r0.addChild(r1);
        r1.addChild(r2);
        r2.addChild(r3);
        r3.addChild(r4);


        Scheduler.delay(() -> { root.move(4, 1, 0, -1); }, 20);
        Scheduler.delay(() -> { r0.move(4, 1, 0, 0); }, 30);
        Scheduler.delay(() -> { r1.move(4, 1, 0, 0); }, 30);
        Scheduler.delay(() -> { r2.move(4, 1, 0, 0); }, 30);
        Scheduler.delay(() -> { r3.move(4, 1, 0, 0); }, 30);
        Scheduler.delay(() -> { r4.move(4, 1, 0, 0); }, 30);


        //Scheduler.delay(() -> { r2.rotate(10,  -(float)Math.PI * 0.25f, 0, 1, 0); }, 50);
        //Scheduler.delay(() -> { r2.rotate(10,  (float)Math.PI * 0.25f, 0, 0, 1); }, 50);

        //Scheduler.delay(() -> { center.rotate(10,  (float)Math.PI * 0.25f, 0, 1, 1); }, 50);


        Scheduler.delay(() -> { center.rotate(10,  (float)Math.PI * 0.1f, 0, 1, 1); }, 80);
        Scheduler.delay(() -> {     r0.rotate(10,  (float)Math.PI * 0.1f, 0, 1, 1); }, 80);
        Scheduler.delay(() -> {     r1.rotate(10,  (float)Math.PI * 0.1f, 0, 1, 1); }, 80);
        Scheduler.delay(() -> {     r2.rotate(10,  (float)Math.PI * 0.1f, 0, 1, 1); }, 80);
        Scheduler.delay(() -> {     r3.rotate(10,  (float)Math.PI * 0.1f, 0, 1, 1); }, 80);
        Scheduler.delay(() -> {     r4.rotate(10,  (float)Math.PI * 0.1f, 0, 1, 1); }, 80);

        //Scheduler.delay(() -> { center.rotate(20,  (float)Math.PI * 0.4f, 0, 1, 0); }, 100);

        Scheduler.delay(() -> { center.rotate(10,  (float)Math.PI * -0.1f, 0, 1, 1); }, 120);
        Scheduler.delay(() -> {     r0.rotate(10,  (float)Math.PI * -0.1f, 0, 1, 1); }, 120);
        Scheduler.delay(() -> {     r1.rotate(10,  (float)Math.PI * -0.1f, 0, 1, 1); }, 120);
        Scheduler.delay(() -> {     r2.rotate(10,  (float)Math.PI * -0.1f, 0, 1, 1); }, 120);
        Scheduler.delay(() -> {     r3.rotate(10,  (float)Math.PI * -0.1f, 0, 1, 1); }, 120);
        Scheduler.delay(() -> {     r4.rotate(10,  (float)Math.PI * -0.1f, 0, 1, 1); }, 120);
    }
}
