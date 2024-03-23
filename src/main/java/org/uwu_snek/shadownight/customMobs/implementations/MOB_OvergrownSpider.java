package org.uwu_snek.shadownight.customMobs.implementations;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight._generated._mob_presets._mob_preset_dungeons_overgrown_spider;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
import org.uwu_snek.shadownight.utils.math.Func;
import org.uwu_snek.shadownight.utils.math.K;
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
        super(2);
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
        leg_10 = leg_1b.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_00);
        leg_20 = leg_2b.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_00);
        leg_30 = leg_3b.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_00);

        leg_11 = leg_10.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_01);
        leg_21 = leg_20.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_01);
        leg_31 = leg_30.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_01);

        leg_12 = leg_11.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_02);
        leg_22 = leg_21.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_02);
        leg_32 = leg_31.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_02);

        leg_1a = leg_10.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_0A);
        leg_2a = leg_20.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_0A);
        leg_3a = leg_30.getChild(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_0A);

        // Set leg direction
        leg_0b.rotate(-0.5f, 0, 1, 0);
        leg_1b.rotate(+0.5f, 0, 1, 0);
        leg_2b.rotate(-0.5f, 0, 1, 0);
        leg_3b.rotate(+0.5f, 0, 1, 0);

        // Move right legs to the correct side
        leg_0b.rotate(K.PIf, 0, 1, 0);
        leg_0b.mirrorPosX();
        leg_1b.rotate(K.PIf, 0, 1, 0);
        leg_1b.mirrorPosX();

        {
            Animate.sit_first();
            core.flushUpdates();
        }

        Scheduler.delay(() -> {
            Animate.stand();
            core.flushUpdates();
        }, 20L);




        bones_test.put("core",   (DisplayBone)core);
        bones_test.put("head",   (DisplayBone)head);

        bones_test.put("leg_0b", (DisplayBone)leg_1b);
        bones_test.put("leg_00", (DisplayBone)leg_10);
        bones_test.put("leg_01", (DisplayBone)leg_11);
        bones_test.put("leg_02", (DisplayBone)leg_12);
        bones_test.put("leg_0a", (DisplayBone)leg_1a);

        bones_test.put("leg_1b", (DisplayBone)leg_1b);
        bones_test.put("leg_10", (DisplayBone)leg_10);
        bones_test.put("leg_11", (DisplayBone)leg_11);
        bones_test.put("leg_12", (DisplayBone)leg_12);
        bones_test.put("leg_1a", (DisplayBone)leg_1a);

        bones_test.put("leg_2b", (DisplayBone)leg_2b);
        bones_test.put("leg_20", (DisplayBone)leg_20);
        bones_test.put("leg_21", (DisplayBone)leg_21);
        bones_test.put("leg_22", (DisplayBone)leg_22);
        bones_test.put("leg_2a", (DisplayBone)leg_2a);

        bones_test.put("leg_3b", (DisplayBone)leg_3b);
        bones_test.put("leg_30", (DisplayBone)leg_30);
        bones_test.put("leg_31", (DisplayBone)leg_31);
        bones_test.put("leg_32", (DisplayBone)leg_32);
        bones_test.put("leg_3a", (DisplayBone)leg_3a);
    }




    public void target(Player player){
        // Animation loop //TODO make this external
        Scheduler.loop(() -> {
            /*
            final Location targetPos = player.getLocation();
            float targetYaw = getTargetYaw(mount.getLocation(), targetPos);

            core.setRotation(targetYaw, 0, 1, 0);
            */
        }, 0, DisplayBone.stepDuration);

        // Walking cycle
        Scheduler.loop(() -> {
            final Location targetPos = player.getLocation();
            float targetYaw = getTargetYaw(mount.getLocation(), targetPos);

            Animate.turnTowards(targetYaw);


        }, 0, walkCycleDuration);
    }








    private final Animate_CLASS Animate = new Animate_CLASS();
    private class Animate_CLASS {
        final static float raiseHeight_first = 0.15625f;
        final static float raiseHeight       = 0.3125f + raiseHeight_first;
        final static float raiseAngle_first  = K.PIf / 12;
        final static float raiseAngle        = K.PIf / 6 + raiseAngle_first;

        final static float maxYawChange = K.PIf / 4;




        private void step(){
            //core.moveSelf(0, 0, 0); //FIXME move mount instead of core
            //core.flushUpdates();
        }


        /**
         * Makes the spider turn towards a given yaw value, handling all the animations and updates.
         * This will make it turn at most Animation.maxYawChange radians
         * @param targetYaw The target yaw value
         */
        private void turnTowards(final float targetYaw){
            final float yawDiff = Func.getAngleDifference(yaw, targetYaw);
            final float maxTargetYaw = Func.clamp(yawDiff, -maxYawChange, maxYawChange);

            //TODO start from right or left leg depending on angle
            yaw += maxTargetYaw;

            {
                leg_3b.rotateRelative(maxTargetYaw, 0, 1, 0);   leg_3b.flushUpdates();
                leg_1b.rotateRelative(maxTargetYaw, 0, 1, 0);   leg_1b.flushUpdates();
                head.rotateRelative(maxTargetYaw / 2, 0, 1, 0); head.flushUpdates();
            }

            Scheduler.delay(() -> {
                leg_2b.rotateRelative(maxTargetYaw, 0, 1, 0);   leg_2b.flushUpdates();
                leg_0b.rotateRelative(maxTargetYaw, 0, 1, 0);   leg_0b.flushUpdates();
                head.rotateRelative(maxTargetYaw / 2, 0, 1, 0); head.flushUpdates();
            }, walkCycleDuration / 2);
        }




        private void stand() {
            core.move(0, raiseHeight, 0);

            // Segment 0
            leg_00.rotateLocal(-raiseAngle, 0, 0, 1);
            leg_10.rotateLocal(-raiseAngle, 0, 0, 1);
            leg_20.rotateLocal(-raiseAngle, 0, 0, 1);
            leg_30.rotateLocal(-raiseAngle, 0, 0, 1);
            leg_00.flushUpdates();

            // Segment 1 inverse
            leg_01.rotateLocal(+raiseAngle, 0, 0, 1);
            leg_11.rotateLocal(+raiseAngle, 0, 0, 1);
            leg_21.rotateLocal(+raiseAngle, 0, 0, 1);
            leg_31.rotateLocal(+raiseAngle, 0, 0, 1);
            leg_01.flushUpdates();

            // Armor inverse
            leg_0a.rotateLocal(+raiseAngle / 2, 0, 0, 1);
            leg_1a.rotateLocal(+raiseAngle / 2, 0, 0, 1);
            leg_2a.rotateLocal(+raiseAngle / 2, 0, 0, 1);
            leg_3a.rotateLocal(+raiseAngle / 2, 0, 0, 1);
            leg_0a.flushUpdates();
        }




        private void sit() {
            core.move(0, -raiseHeight, 0);

            // Segment 0
            leg_00.rotateLocal(+raiseAngle, 0, 0, 1);
            leg_10.rotateLocal(+raiseAngle, 0, 0, 1);
            leg_20.rotateLocal(+raiseAngle, 0, 0, 1);
            leg_30.rotateLocal(+raiseAngle, 0, 0, 1);
            leg_00.flushUpdates();

            // Segment 1 inverse
            leg_01.rotateLocal(-raiseAngle, 0, 0, 1);
            leg_11.rotateLocal(-raiseAngle, 0, 0, 1);
            leg_21.rotateLocal(-raiseAngle, 0, 0, 1);
            leg_31.rotateLocal(-raiseAngle, 0, 0, 1);
            leg_01.flushUpdates();

            // Armor inverse
            leg_0a.rotateLocal(-raiseAngle / 2, 0, 0, 1);
            leg_1a.rotateLocal(-raiseAngle / 2, 0, 0, 1);
            leg_2a.rotateLocal(-raiseAngle / 2, 0, 0, 1);
            leg_3a.rotateLocal(-raiseAngle / 2, 0, 0, 1);
            leg_0a.flushUpdates();
        }




        private void sit_first() {
            core.move(0, -raiseHeight_first, 0); //TODO idk if this needs to have a "first" version

            // Segment 0
            leg_00.rotateLocal(+raiseAngle_first, 0, 0, 1);
            leg_10.rotateLocal(+raiseAngle_first, 0, 0, 1);
            leg_20.rotateLocal(+raiseAngle_first, 0, 0, 1);
            leg_30.rotateLocal(+raiseAngle_first, 0, 0, 1);
            leg_00.flushUpdates();

            // Segment 1 inverse
            leg_01.rotateLocal(-raiseAngle_first, 0, 0, 1);
            leg_11.rotateLocal(-raiseAngle_first, 0, 0, 1);
            leg_21.rotateLocal(-raiseAngle_first, 0, 0, 1);
            leg_31.rotateLocal(-raiseAngle_first, 0, 0, 1);
            leg_01.flushUpdates();

            // Armor inverse
            leg_0a.rotateLocal(-raiseAngle_first / 2, 0, 0, 1);
            leg_1a.rotateLocal(-raiseAngle_first / 2, 0, 0, 1);
            leg_2a.rotateLocal(-raiseAngle_first / 2, 0, 0, 1);
            leg_3a.rotateLocal(-raiseAngle_first / 2, 0, 0, 1);
            leg_0a.flushUpdates();
        }
    }
}
