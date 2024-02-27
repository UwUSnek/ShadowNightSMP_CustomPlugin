package org.uwu_snek.shadownight.utils.blockdata;


import org.bukkit.Material;
import org.bukkit.block.data.type.Leaves;




public final class DataBuilderLeaves {
    private final Leaves data;
    public DataBuilderLeaves(Material type) {
        data = (Leaves)type.createBlockData();
    }
    public Leaves build() {
        return data;
    }




    @SuppressWarnings("unused") public DataBuilderLeaves setPersistent(final boolean n) {
        data.setPersistent(n);
        return this;
    }
    @SuppressWarnings("unused") public DataBuilderLeaves setWaterlogged(final boolean n) {
        data.setWaterlogged(n);
        return this;
    }
    @SuppressWarnings("unused") public DataBuilderLeaves setDistance(final int n) {
        data.setDistance(n);
        return this;
    }
}
