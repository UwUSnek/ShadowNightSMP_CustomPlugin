package org.uwu_snek.shadownight.items;


import org.uwu_snek.shadownight.utils.utils;

import java.nio.charset.StandardCharsets;

public enum CustomItemId {
    IRON_SCYTHE(),
    DIAMOND_SCYTHE(),
    NETHERITE_SCYTHE(),
    KLAUE_SCYTHE(),

    GOLDEN_DAGGER(),
    STONE_DAGGER(),
    IRON_DAGGER(),
    DIAMOND_DAGGER(),
    NETHERITE_DAGGER(),

    GOLDEN_SPEAR(),
    STONE_SPEAR(),
    IRON_SPEAR(),
    DIAMOND_SPEAR(),
    NETHERITE_SPEAR(),

    HELLFIRE_BOW(),
    ;


    private final long value;
    CustomItemId() {
        this.value = utils.createHash64(this.name().getBytes(StandardCharsets.UTF_8));
    }
    public long getNumericalValue() {
        return value;
    }
}
