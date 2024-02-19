package org.shadownight.plugin.shadownight.utils.blockdata;


import org.bukkit.Material;
import org.bukkit.block.data.type.Slab;
import org.jetbrains.annotations.NotNull;


public final class DataBuilderSlab {
    private final Slab data;
    public DataBuilderSlab(Material type) {
        data = (Slab)type.createBlockData();
    }
    public Slab build() {
        return data;
    }




    @SuppressWarnings("unused") public DataBuilderSlab setWaterlogged(final boolean n) {
        data.setWaterlogged(n);
        return this;
    }
    @SuppressWarnings("unused") public DataBuilderSlab setType(@NotNull final Slab.Type n) {
        data.setType(n);
        return this;
    }
}
