package org.shadownight.plugin.shadownight.utils.blockdata;


import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Rotatable;
import org.jetbrains.annotations.NotNull;


public final class DataBuilderRotatable {
    private final Rotatable data;
    public DataBuilderRotatable(Material type) {
        data = (Rotatable)type.createBlockData();
    }
    public Rotatable build() {
        return data;
    }




    @SuppressWarnings("unused") public DataBuilderRotatable setRotation(@NotNull final BlockFace n) {
        data.setRotation(n);
        return this;
    }
}
