package org.uwu_snek.shadownight.customMobs.implementations;

import org.uwu_snek.shadownight._generated._mob_presets._mob_preset_dungeons_overgrown_spider;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.DisplayBone;

import java.util.HashMap;




public class MOB_OvergrownSpider extends _mob_preset_dungeons_overgrown_spider {
    public HashMap<String, DisplayBone> bones_test = new HashMap<>();
    public static MOB_OvergrownSpider testMob;
    private final Bone leg_bl0;
    //private final Bone leg_fr0;
    //private final Bone leg_br0;

    public MOB_OvergrownSpider() {
        super();
        root.addChild(leg_bl0 = leg_fl0.createDeepCopy());
        //leg_fl0.rotate(20, +0.8f, 0, 1, 0);
        //leg_bl0.rotate(20, -0.8f, 0, 1, 0);

        testMob = this;
        bones_test.put("leg_fl0", (DisplayBone)leg_fl0);
        bones_test.put("leg_bl0", (DisplayBone)leg_bl0);
        //leg_fr0 = leg_fl0.createDeepCopy();
        //leg_br0 = leg_bl0.createDeepCopy();
    }
}
