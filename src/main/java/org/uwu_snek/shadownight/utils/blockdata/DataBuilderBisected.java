package org.uwu_snek.shadownight.utils.blockdata;


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




    @SuppressWarnings("unused") public DataBuilderBisected setHalf(final @NotNull Bisected.Half n) {
        data.setHalf(n);
        return this;
    }
}
