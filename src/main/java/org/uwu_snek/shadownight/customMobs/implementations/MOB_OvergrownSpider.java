package org.uwu_snek.shadownight.customMobs.implementations;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight._generated._mob_presets._mob_preset_dungeons_overgrown_spider;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
import org.uwu_snek.shadownight.customMobs.StackableTransforms.*;
import org.uwu_snek.shadownight.utils.math.Easing;
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
        leg_0b.instantMoveAll(0, 0, -0.1f);
        leg_1b.instantMoveAll(0, 0, +0.1f);
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
        leg_0b.instantRotateAbsAll(0, 1, 0, -0.5f);
        leg_1b.instantRotateAbsAll(0, 1, 0, +0.5f);
        leg_2b.instantRotateAbsAll(0, 1, 0, -0.5f);
        leg_3b.instantRotateAbsAll(0, 1, 0, +0.5f);

        // Move right legs to the correct side
        leg_0b.instantRotateAbsAll(0, 1, 0, K.PIf);
        leg_0b.mirrorPosX();
        leg_1b.instantRotateAbsAll(0, 1, 0, K.PIf);
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
        }, 0, 1);

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
        final static float walkLegRaiseAngle = 0.2f;





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
            if(Math.abs(yawDiff) < 0.001) return; // Save CPU and prevent graphical problems
            final float maxTargetYaw = Func.clamp(yawDiff, -maxYawChange, maxYawChange);

            final float adjustedWalkLegRaiseAngle = walkLegRaiseAngle * (maxTargetYaw / maxYawChange);

            //TODO start from right or left leg depending on angle
            yaw += maxTargetYaw;
            Bone[][] _pair0 = {
                { leg_3b, leg_30, leg_31, leg_32, leg_3a },
                { leg_1b, leg_10, leg_11, leg_12, leg_1a },
            };
            Bone[][] _pair1 = {
                { leg_2b, leg_20, leg_21, leg_22, leg_2a },
                { leg_0b, leg_00, leg_01, leg_02, leg_0a },
            };
            if(maxTargetYaw <= 0) {
                final var tmp = _pair0;
                _pair0 = _pair1;
                _pair1 = tmp;
            }
            final Bone[][] pair0 = _pair0;
            final Bone[][] pair1 = _pair1;



            // First pair
            {
                pair0[0][1].addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, adjustedWalkLegRaiseAngle));
                pair0[1][1].addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, adjustedWalkLegRaiseAngle));
            }
            Scheduler.delay(() -> {
                pair0[0][0].addTransform(new ST_RotateRelAll(5, Easing::linear, 0, 1, 0, maxTargetYaw));
                pair0[1][0].addTransform(new ST_RotateRelAll(5, Easing::linear, 0, 1, 0, maxTargetYaw));
                head.addTransform(new ST_RotateRelAll(5, Easing::linear, 0, 1, 0, maxTargetYaw / 2));
            }, 1L);
            Scheduler.delay(() -> {
                pair0[0][1].addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -adjustedWalkLegRaiseAngle));
                pair0[1][1].addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -adjustedWalkLegRaiseAngle));
            }, walkCycleDuration / 2 + 1);


            // Second pair
            Scheduler.delay(() -> {
                    pair1[0][1].addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, adjustedWalkLegRaiseAngle));
                    pair1[1][1].addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, adjustedWalkLegRaiseAngle));
            }, walkCycleDuration / 2);
            Scheduler.delay(() -> {
                pair1[0][0].addTransform(new ST_RotateRelAll(5, Easing::linear, 0, 1, 0, maxTargetYaw));
                pair1[1][0].addTransform(new ST_RotateRelAll(5, Easing::linear, 0, 1, 0, maxTargetYaw));
                head.addTransform(new ST_RotateRelAll(5, Easing::linear, 0, 1, 0, maxTargetYaw / 2));
            }, walkCycleDuration / 2 + 1);
            Scheduler.delay(() -> {
                pair1[0][1].addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -adjustedWalkLegRaiseAngle));
                pair1[1][1].addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -adjustedWalkLegRaiseAngle));
            }, walkCycleDuration);
        }




        private void stand() {
            core.addTransform(new ST_MoveAll(5, Easing::linear, 0, raiseHeight, 0));

            // Segment 0
            leg_00.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle));
            leg_10.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle));
            leg_20.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle));
            leg_30.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle));

            // Segment 1 inverse
            leg_01.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle));
            leg_11.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle));
            leg_21.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle));
            leg_31.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle));

            // Armor inverse
            leg_0a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle / 2));
            leg_1a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle / 2));
            leg_2a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle / 2));
            leg_3a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle / 2));
        }




        private void sit() {
            core.addTransform(new ST_MoveAll(5, Easing::linear, 0, -raiseHeight, 0));

            // Segment 0
            leg_00.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle));
            leg_10.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle));
            leg_20.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle));
            leg_30.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle));

            // Segment 1 inverse
            leg_01.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle));
            leg_11.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle));
            leg_21.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle));
            leg_31.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle));

            // Armor inverse
            leg_0a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle / 2));
            leg_1a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle / 2));
            leg_2a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle / 2));
            leg_3a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle / 2));
        }




        private void sit_first() {
            core.addTransform(new ST_MoveAll(5, Easing::linear, 0, -raiseHeight_first, 0)); //TODO idk if this needs to have a "first" version

            // Segment 0
            leg_00.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle_first));
            leg_10.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle_first));
            leg_20.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle_first));
            leg_30.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, +raiseAngle_first));

            // Segment 1 inverse
            leg_01.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle_first));
            leg_11.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle_first));
            leg_21.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle_first));
            leg_31.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle_first));

            // Armor inverse
            leg_0a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle_first / 2));
            leg_1a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle_first / 2));
            leg_2a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle_first / 2));
            leg_3a.addTransform(new ST_RotateLocAll(5, Easing::linear, 0, 0, 1, -raiseAngle_first / 2));
        }
    }
}
