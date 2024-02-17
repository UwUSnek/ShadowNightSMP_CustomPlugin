package org.shadownight.plugin.shadownight.utils.data;


import org.bukkit.Axis;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.Orientable;
import org.bukkit.block.data.Rotatable;
import org.bukkit.block.data.type.Leaves;
import org.jetbrains.annotations.NotNull;




public final class DataBuilderDeepslate {
    private final Orientable data;
    public DataBuilderDeepslate(Material type) {
        data = (Orientable)type.createBlockData();
    }
    public Orientable build() {
        return data;
    }




    public DataBuilderDeepslate setFacing(@NotNull final Axis n) {
        data.setAxis(n);
        return this;
    }
}
