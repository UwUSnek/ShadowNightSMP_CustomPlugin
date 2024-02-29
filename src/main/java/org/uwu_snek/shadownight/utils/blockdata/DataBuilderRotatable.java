package org.uwu_snek.shadownight.utils.blockdata;


import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.jetbrains.annotations.NotNull;


public final class DataBuilderRotatable {
    private final Rotatable data;
    @SuppressWarnings("unused") public DataBuilderRotatable(Material type) {
        data = (Rotatable)type.createBlockData();
    }

    @SuppressWarnings("unused") public Rotatable build() {
        return data;
    }




    @SuppressWarnings("unused") public DataBuilderRotatable setRotation(final @NotNull BlockFace n) {
        data.setRotation(n);
        return this;
    }
}
