package org.uwu_snek.shadownight.customMobs.implementations;

import org.bukkit.util.Transformation;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.MOB;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
import org.uwu_snek.shadownight.customMobs.StackableTransforms.ST_MoveAll;
import org.uwu_snek.shadownight.utils.math.Easing;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;

import java.util.HashMap;




public class MOB_Debug extends MOB {
    public HashMap<String, DisplayBone> bones_test = new HashMap<>();
    public static MOB_Debug testMob;

    Bone center = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);
    Bone r0     = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);
    Bone r1     = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);
    Bone r2     = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);
    Bone r3     = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);
    Bone r4     = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);
    Bone l0     = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);
    Bone l1     = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);
    Bone l2     = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);
    Bone l3     = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);
    Bone l4     = new DisplayBone(_mob_part_type.DEBUG_MOB_MAIN, 1.02f, 1.02f);

    public MOB_Debug() {
        super(0);

        bones_test.put("center", (DisplayBone)center);
        bones_test.put("r0",     (DisplayBone)r0);
        bones_test.put("r1",     (DisplayBone)r1);
        bones_test.put("r2",     (DisplayBone)r2);
        bones_test.put("r3",     (DisplayBone)r3);
        bones_test.put("r4",     (DisplayBone)r4);
        bones_test.put("l0",     (DisplayBone)l0);
        bones_test.put("l1",     (DisplayBone)l1);
        bones_test.put("l2",     (DisplayBone)l2);
        bones_test.put("l3",     (DisplayBone)l3);
        bones_test.put("l4",     (DisplayBone)l4);

        testMob = this;
        root.addChild(center);
        center.addChild(r0);
        r0.addChild(r1);
        r1.addChild(r2);
        r2.addChild(r3);
        r3.addChild(r4);
        center.addChild(l0);
        l0.addChild(l1);
        l1.addChild(l2);
        l2.addChild(l3);
        l3.addChild(l4);


        Scheduler.delay(() -> { root.addTransform(new ST_MoveAll(5, Easing::sineIn, 1, 0, -1)); }, 20);

        Scheduler.delay(() -> { r0.addTransform(new ST_MoveAll(5, Easing::sineIn, 1, 0, 2)); }, 30);
        Scheduler.delay(() -> { r1.addTransform(new ST_MoveAll(5, Easing::sineIn, 1, 0, 0)); }, 30);
        Scheduler.delay(() -> { r2.addTransform(new ST_MoveAll(5, Easing::sineIn, 1, 0, 0)); }, 30);
        Scheduler.delay(() -> { r3.addTransform(new ST_MoveAll(5, Easing::sineIn, 1, 0, 0)); }, 30);
        Scheduler.delay(() -> { r4.addTransform(new ST_MoveAll(5, Easing::sineIn, 1, 0, 0)); }, 30);

        Scheduler.delay(() -> { l0.addTransform(new ST_MoveAll(5, Easing::sineIn, -1, 0, -2)); }, 30);
        Scheduler.delay(() -> { l1.addTransform(new ST_MoveAll(5, Easing::sineIn, -1, 0, 0)); }, 30);
        Scheduler.delay(() -> { l2.addTransform(new ST_MoveAll(5, Easing::sineIn, -1, 0, 0)); }, 30);
        Scheduler.delay(() -> { l3.addTransform(new ST_MoveAll(5, Easing::sineIn, -1, 0, 0)); }, 30);
        Scheduler.delay(() -> { l4.addTransform(new ST_MoveAll(5, Easing::sineIn, -1, 0, 0)); }, 30);


/*
        Scheduler.delay(() -> {
            r0.rotateRelative(-0.5f, 0, 1, 0);
            l0.rotateLocal(-0.5f, 0, 1, 0);
        }, 50);
        Scheduler.delay(() -> {
            r1.rotateRelative(-0.5f, 1, 0, 0);
            l1.rotateLocal(-0.5f, 1, 0, 0);
        }, 70);
        Scheduler.delay(() -> {
            r2.rotateRelative(-0.5f, 0, 0, 1);
            l2.rotateLocal(-0.5f, 0, 0, 1);
        }, 90);
*/
/*
        Scheduler.delay(() -> { center.rotate((float)Math.PI * 0.1f, 0, 1, 1); }, 40);
        Scheduler.delay(() -> {     r0.rotate((float)Math.PI * 0.1f, 0, 1, 1); }, 40);
        Scheduler.delay(() -> {     r1.rotate((float)Math.PI * 0.1f, 0, 1, 1); }, 40);
        Scheduler.delay(() -> {     r2.rotate((float)Math.PI * 0.1f, 0, 1, 1); }, 40);
        Scheduler.delay(() -> {     r3.rotate((float)Math.PI * 0.1f, 0, 1, 1); }, 40);
        Scheduler.delay(() -> {     r4.rotate((float)Math.PI * 0.1f, 0, 1, 1); }, 40);

        Scheduler.delay(() -> { center.rotate((float)Math.PI * 0.1f, 0, 1, 1); }, 40);

        Scheduler.delay(() -> { center.rotate((float)Math.PI * -0.1f, 0, 1, 1); }, 70);
        Scheduler.delay(() -> {     r0.rotate((float)Math.PI * -0.1f, 0, 1, 1); }, 70);
        Scheduler.delay(() -> {     r1.rotate((float)Math.PI * -0.1f, 0, 1, 1); }, 70);
        Scheduler.delay(() -> {     r2.rotate((float)Math.PI * -0.1f, 0, 1, 1); }, 70);
        Scheduler.delay(() -> {     r3.rotate((float)Math.PI * -0.1f, 0, 1, 1); }, 70);
        Scheduler.delay(() -> {     r4.rotate((float)Math.PI * -0.1f, 0, 1, 1); }, 70);

        Scheduler.delay(() -> { center.rotate((float)Math.PI * -0.1f, 0, 1, 1); }, 70);

        Scheduler.delay(() -> { center.rotate((float)Math.PI * 0.25f, 0, 0, 1); }, 90);
        Scheduler.delay(() -> { r2.rotate((float)Math.PI * 0.25f, 0, 0, 1); }, 110);
        Scheduler.delay(() -> { center.rotate((float)Math.PI * 0.25f, 0, 0, 1); }, 130);
    */
    }
}
