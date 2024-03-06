package org.uwu_snek.shadownight._generated;
import org.uwu_snek.shadownight.utils.utils;
import java.nio.charset.StandardCharsets;
public enum _custom_item_id {
    GOLDEN_DAGGER(),
    GOLDEN_SPEAR(),
    STONE_DAGGER(),
    STONE_SPEAR(),
    IRON_SCYTHE(),
    IRON_DAGGER(),
    IRON_SPEAR(),
    DIAMOND_SCYTHE(),
    DIAMOND_DAGGER(),
    DIAMOND_SPEAR(),
    KLAUE_SCYTHE(),
    NETHERITE_SCYTHE(),
    NETHERITE_DAGGER(),
    NETHERITE_SPEAR(),
    HELLFIRE_BOW();
    private final long value;
    _custom_item_id() {
        this.value = utils.createHash64(this.name().getBytes(StandardCharsets.UTF_8));
    }
    public long getNumericalValue() {
        return value;
    }
}