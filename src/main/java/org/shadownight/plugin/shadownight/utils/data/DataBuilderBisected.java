package org.shadownight.plugin.shadownight.utils.data;


import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.data.Bisected;
import org.jetbrains.annotations.NotNull;


public final class DataBuilderBisected {
    private final Bisected data;
    public DataBuilderBisected(Material type) {
        data = (Bisected)type.createBlockData();
    }
    public Bisected build() {
        return data;
    }




    public DataBuilderBisected setHalf(@NotNull final Bisected.Half n) {
        data.setHalf(n);
        return this;
    }
}
