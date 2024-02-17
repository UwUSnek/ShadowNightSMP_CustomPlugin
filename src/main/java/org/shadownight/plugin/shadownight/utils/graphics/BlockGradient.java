package org.shadownight.plugin.shadownight.utils.graphics;


import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.math.Func;

import java.util.ArrayList;


public final class BlockGradient implements Rnd {
    private final ArrayList<BlockPattern> w = new ArrayList<>();



    /**
     * Create the block gradient.
     * @param blocks A list of Pairs each containing the weight(Integer) and either a BlockPattern or a BlockData.
     */
    @SafeVarargs
    public BlockGradient(@NotNull final Pair<Integer, ?>... blocks) {
        for(Pair<?, ?> b : blocks) {
            if (b.getValue0() instanceof Integer n) {
                if(     b.getValue1() instanceof BlockPattern p) for (int i = 0; i < n; ++i) w.add(p);
                else if(b.getValue1() instanceof BlockData    d) for (int i = 0; i < n; ++i) w.add(new BlockPattern(Pair.with(1f, d)));
                else _throw(b);
            }
            else _throw(b);
        }
    }
    private static void _throw(final Object value) {
        throw new RuntimeException("Cannot create BlockGradient: \"" + value.getClass().getName() + "\" is not a valid Object type.");
    }


    /**
     * Generate the block based on the weight of the blocks configured in this instance.
     * @return The generated block
     */
    public BlockData get(final float n) {
        double scaled_n = Func.clamp(w.size() * n, 0, w.size());       //       0 to 1            -->   0 to max
        double target_n = scaled_n - 0.5f;                             //       0 to max          -->   -0.5 to max-0.5
        if(target_n <= 0) return w.get(0).get();                       // Return if too low to be interpolated
        if(target_n >= w.size() - 1) return w.get(w.size() - 1).get(); // Return if too high to be interpolated

        int base_material = (int)target_n;
        int alt_material  = base_material + 1;
        double chance = target_n - base_material;
        return w.get(rnd.nextFloat() > chance ? base_material : alt_material).get();
    }
}
