package org.shadownight.plugin.shadownight.items;


import com.google.common.hash.HashCode;
import org.shadownight.plugin.shadownight.utils.utils;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.logging.Level;

public enum CustomItemId {
    IRON_SCYTHE(),
    DIAMOND_SCYTHE(),
    NETHERITE_SCYTHE(),
    KLAUE_SCYTHE(),
    ;


    private final long value;
    CustomItemId() {
        this.value = utils.createHash64(this.name().getBytes(StandardCharsets.UTF_8));
    }
    public long getValue() {
        return value;
    }
}
