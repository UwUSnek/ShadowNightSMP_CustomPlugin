package org.shadownight.plugin.shadownight.utils.data;


import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.data.Orientable;
import org.jetbrains.annotations.NotNull;




public final class DataBuilderOrientable {
    private final Orientable data;
    public DataBuilderOrientable(Material type) {
        data = (Orientable)type.createBlockData();
    }
    public Orientable build() {
        return data;
    }




    public DataBuilderOrientable setFacing(@NotNull final Axis n) {
        data.setAxis(n);
        return this;
    }
}
