// Generated by UwU_Snek's silly little Python script



package org.uwu_snek.shadownight._generated;
public enum _mob_part_type {
    DUNGEONS_OVERGROWN_SPIDER_CORE(20000, 0),
    DUNGEONS_OVERGROWN_SPIDER_HEAD(20001, 0),
    DUNGEONS_OVERGROWN_SPIDER_LEG_0B(20002, 1),
    DUNGEONS_OVERGROWN_SPIDER_LEG_00(20003, 0),
    DUNGEONS_OVERGROWN_SPIDER_LEG_01(20004, 0),
    DUNGEONS_OVERGROWN_SPIDER_LEG_02(20005, 0),
    DUNGEONS_OVERGROWN_SPIDER_LEG_0A(20006, 1),
    DEBUG_MOB_MAIN(20007, 1),
    ;
    private final int modelData;
    private final int childIndex;

    _mob_part_type(final int _modelData, final int _childIndex) {
        this.modelData = _modelData;
        this.childIndex = _childIndex;
    }
    public int getCustomModelData() {
        return modelData;
    }
    public int getChildIndex() {
        return childIndex;
    }
}