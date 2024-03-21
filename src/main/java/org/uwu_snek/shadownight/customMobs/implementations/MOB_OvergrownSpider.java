package org.uwu_snek.shadownight.customMobs.implementations;

import org.uwu_snek.shadownight._generated._mob_presets._mob_preset_dungeons_overgrown_spider;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;

import java.util.HashMap;




public class MOB_OvergrownSpider extends _mob_preset_dungeons_overgrown_spider {
    public HashMap<String, DisplayBone> bones_test = new HashMap<>();
    public static MOB_OvergrownSpider testMob;
    private final Bone leg_1b;
    private final Bone leg_10;
    private final Bone leg_11;
    private final Bone leg_12;
    private final Bone leg_1a;

    private final Bone leg_2b;
    private final Bone leg_20;
    private final Bone leg_21;
    private final Bone leg_22;
    private final Bone leg_2a;

    private final Bone leg_3b;
    private final Bone leg_30;
    private final Bone leg_31;
    private final Bone leg_32;
    private final Bone leg_3a;


    public MOB_OvergrownSpider() {
        super();
        testMob = this;

        //! Leg name numbers start from the right-left one and continue clockwise
        //! The first digit indicates the leg, while the second indicates the segment index
        //! "b" indicates the base, "a" indicates the armor part

        // Create leg copies
        core.addChild(leg_1b = leg_0b.createDeepCopy());
        leg_0b.move(0, 0, -0.1f);
        leg_1b.move(0, 0, +0.1f);
        core.addChild(leg_2b = leg_1b.createDeepCopy());
        core.addChild(leg_3b = leg_0b.createDeepCopy());

        // Setup handler members
        leg_12 = (leg_11 = (leg_10 = leg_1b.getChildren().get(0)).getChildren().get(0)).getChildren().get(0);
        leg_22 = (leg_21 = (leg_20 = leg_2b.getChildren().get(0)).getChildren().get(0)).getChildren().get(0);
        leg_32 = (leg_31 = (leg_30 = leg_3b.getChildren().get(0)).getChildren().get(0)).getChildren().get(0);
        leg_1a = leg_10.getChildren().get(1); //FIXME make index deterministic or use a different search method
        leg_2a = leg_20.getChildren().get(1); //FIXME make index deterministic or use a different search method
        leg_3a = leg_30.getChildren().get(1); //FIXME make index deterministic or use a different search method

        // Set leg direction
        leg_0b.rotate(+0.5f, 0, 1, 0);
        leg_1b.rotate(-0.5f, 0, 1, 0);
        leg_2b.rotate(+0.5f, 0, 1, 0);
        leg_3b.rotate(-0.5f, 0, 1, 0);

        // Move right legs to the correct side
        leg_0b.rotate(PI, 0, 1, 0);
        leg_0b.mirrorPosX();
        leg_1b.rotate(PI, 0, 1, 0);
        leg_1b.mirrorPosX();


        bones_test.put("head", (DisplayBone)head);
        bones_test.put("core", (DisplayBone)core);

        bones_test.put("leg_0b", (DisplayBone)leg_0b);
        bones_test.put("leg_0a", (DisplayBone)leg_0a);
        bones_test.put("leg_00", (DisplayBone)leg_00);
        bones_test.put("leg_01", (DisplayBone)leg_01);
        bones_test.put("leg_02", (DisplayBone)leg_02);

        bones_test.put("leg_1b", (DisplayBone)leg_1b);
        bones_test.put("leg_1a", (DisplayBone)leg_1a);
        bones_test.put("leg_10", (DisplayBone)leg_10);
        bones_test.put("leg_11", (DisplayBone)leg_11);
        bones_test.put("leg_12", (DisplayBone)leg_12);

        bones_test.put("leg_2b", (DisplayBone)leg_2b);
        bones_test.put("leg_2a", (DisplayBone)leg_2a);
        bones_test.put("leg_20", (DisplayBone)leg_20);
        bones_test.put("leg_21", (DisplayBone)leg_21);
        bones_test.put("leg_22", (DisplayBone)leg_22);

        bones_test.put("leg_3b", (DisplayBone)leg_3b);
        bones_test.put("leg_3a", (DisplayBone)leg_3a);
        bones_test.put("leg_30", (DisplayBone)leg_30);
        bones_test.put("leg_31", (DisplayBone)leg_31);
        bones_test.put("leg_32", (DisplayBone)leg_32);

        //Scheduler.delay(() -> Animate.step(), 20L);
        Scheduler.delay(Animate::stand, 20L);
    }




    private class Animate_CLASS {
        final static float raiseHeight = 0.3125f;

        private void step(){
            core.moveSelf(0, 0, 0);
        }

        private void stand() {
            core.move(0, raiseHeight, 0);

            // Segment 0
            leg_00.rotateLocal(+PI / 6, 0, 0, 1);
            leg_10.rotateLocal(+PI / 6, 0, 0, 1);
            leg_20.rotateLocal(+PI / 6, 0, 0, 1);
            leg_30.rotateLocal(+PI / 6, 0, 0, 1);

            // Segment 1 inverse
            leg_01.rotateLocal(-PI / 6, 0, 0, 1);
            leg_11.rotateLocal(-PI / 6, 0, 0, 1);
            leg_21.rotateLocal(-PI / 6, 0, 0, 1);
            leg_31.rotateLocal(-PI / 6, 0, 0, 1);

            // Armor inverse
            leg_0a.rotateLocal(-PI / 12, 0, 0, 1);
            leg_1a.rotateLocal(-PI / 12, 0, 0, 1);
            leg_2a.rotateLocal(-PI / 12, 0, 0, 1);
            leg_3a.rotateLocal(-PI / 12, 0, 0, 1);
        }
    }
    private final Animate_CLASS Animate = new Animate_CLASS();
}
