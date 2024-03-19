package org.uwu_snek.shadownight.customMobs.implementations;

import org.uwu_snek.shadownight._generated._mob_presets._mob_preset_dungeons_overgrown_spider;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.DisplayBone;

import java.util.HashMap;




public class MOB_OvergrownSpider extends _mob_preset_dungeons_overgrown_spider {
    public HashMap<String, DisplayBone> bones_test = new HashMap<>();
    public static MOB_OvergrownSpider testMob;
    private final Bone leg_10;
    private final Bone leg_11;
    private final Bone leg_12;

    //private final Bone leg_20;
    //private final Bone leg_21;
    //private final Bone leg_22;

    //private final Bone leg_30;
    //private final Bone leg_31;
    //private final Bone leg_32;


    public MOB_OvergrownSpider() {
        super();
        testMob = this;

        //! Leg name numbers start from the right-left one and continue clockwise
        //! The first digit indicates the leg, while the second indicates the segment index

        root.addChild(leg_10 = leg_00.createDeepCopy());
        leg_11 = leg_10.getChildren().get(0);
        leg_12 = leg_11.getChildren().get(0);

        //root.addChild(leg_20 = leg_00.createDeepCopy());
        //leg_21 = leg_20.getChildren().get(0);
        //leg_22 = leg_21.getChildren().get(0);

        //root.addChild(leg_30 = leg_00.createDeepCopy());
        //leg_31 = leg_30.getChildren().get(0);
        //leg_32 = leg_31.getChildren().get(0);

        leg_00.rotate(20, -0.8f, 0, 1, 0);
        leg_10.rotate(20, +0.8f, 0, 1, 0);
        //leg_20.rotate(20, -0.8f, 0, 1, 0);
        //leg_30.rotate(20, +0.8f, 0, 1, 0);

        //leg_20.rotate(20, (float)Math.PI, 0, 1, 0);
        //leg_30.rotate(20, (float)Math.PI, 0, 1, 0);


        bones_test.put("head", (DisplayBone)head);
        bones_test.put("leg_00", (DisplayBone)leg_00);
        bones_test.put("leg_01", (DisplayBone)leg_01);
        bones_test.put("leg_02", (DisplayBone)leg_02);
        bones_test.put("leg_10", (DisplayBone)leg_10);
        bones_test.put("leg_11", (DisplayBone)leg_11);
        bones_test.put("leg_12", (DisplayBone)leg_12);
        //bones_test.put("leg_20", (DisplayBone)leg_20);
        //bones_test.put("leg_21", (DisplayBone)leg_21);
        //bones_test.put("leg_22", (DisplayBone)leg_22);
        //bones_test.put("leg_30", (DisplayBone)leg_30);
        //bones_test.put("leg_31", (DisplayBone)leg_31);
        //bones_test.put("leg_32", (DisplayBone)leg_32);
    }
}
