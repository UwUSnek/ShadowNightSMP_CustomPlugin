package org.shadownight.plugin.shadownight.utils.data;


import org.bukkit.Material;
import org.bukkit.block.data.type.Slab;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.logging.Level;


public final class DataBuilderSlab {
    private final Slab data;
    public DataBuilderSlab(Material type) {
        data = (Slab)type.createBlockData();
    }
    public Slab build() {
        return data;
    }




    public DataBuilderSlab setWaterlogged(final boolean n) {
        data.setWaterlogged(n);
        return this;
    }
    public DataBuilderSlab setType(@NotNull final Slab.Type n) {
        data.setType(n);
        return this;
    }
}
