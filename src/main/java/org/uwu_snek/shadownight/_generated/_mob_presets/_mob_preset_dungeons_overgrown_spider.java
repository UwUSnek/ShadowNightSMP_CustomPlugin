// Generated by UwU_Snek's silly little Python script



package org.uwu_snek.shadownight._generated._mob_presets;
import org.uwu_snek.shadownight.customMobs.MOB;
import org.uwu_snek.shadownight._generated._mob_part_type;
import org.uwu_snek.shadownight.customMobs.Bone;
import org.uwu_snek.shadownight.customMobs.DisplayBone;
public abstract class _mob_preset_dungeons_overgrown_spider extends MOB {
    protected Bone core = new DisplayBone(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_CORE, 0.5f, 0.5f);
    protected Bone head = new DisplayBone(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_HEAD, 0.5f, 0.5f);
    protected Bone leg_0b = new DisplayBone(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_0B, 0.5f, 0.5f);
    protected Bone leg_00 = new DisplayBone(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_00, 0.5f, 0.5f);
    protected Bone leg_01 = new DisplayBone(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_01, 0.5f, 0.5f);
    protected Bone leg_02 = new DisplayBone(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_02, 0.5f, 0.5f);
    protected Bone leg_0a = new DisplayBone(_mob_part_type.DUNGEONS_OVERGROWN_SPIDER_LEG_0A, 0.5f, 0.5f);

    public _mob_preset_dungeons_overgrown_spider(final double movementSpeed) {
        super(movementSpeed);
        root.addChild(core);
        core.addChild(head);
        core.addChild(leg_0b);
        leg_0b.addChild(leg_00);
        leg_00.addChild(leg_01);
        leg_01.addChild(leg_02);
        leg_00.addChild(leg_0a);
        core.moveSelf(0.0f, -0.21875f, 0.0f);
        head.moveSelf(0.0f, 0.0f, 0.0f);
        leg_0b.moveSelf(0.3125f, -0.3125f, 0.0f);
        leg_00.moveSelf(0.59375f, -0.3125f, 0.0f);
        leg_01.moveSelf(1.0f, 0.59375f, 0.0f);
        leg_02.moveSelf(1.265625f, 0.09375f, 0.0f);
        leg_0a.moveSelf(1.0f, 0.59375f, 0.0f);
    }
}
