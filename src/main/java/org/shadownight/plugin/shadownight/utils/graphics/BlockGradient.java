package org.shadownight.plugin.shadownight.utils.graphics;


import org.bukkit.Material;
import org.javatuples.Pair;
import org.shadownight.plugin.shadownight.utils.Rnd;
import org.shadownight.plugin.shadownight.utils.math.Func;

import java.util.ArrayList;


public final class BlockGradient extends Rnd {
    private final ArrayList<Object> w = new ArrayList<>();


    /**
     * Create the block gradient
     * @param blocks A list of tuples each containing the weight and material of a given entry.
     *               The Second value must be either a Material or a BlockPattern
     */
    @SafeVarargs
    public BlockGradient(Pair<Integer, Object>... blocks) {
        for(Pair<Integer, Object> block : blocks) {
            for(int i = 0; i < block.getValue0(); ++i) w.add(block.getValue1());
        }
    }


    /**
     * Generate the block based on the weight of the blocks configured in this instance
     * @return The generated block
     */
    public Material get(float n) {
        double scaled_n = Func.clampMax(w.size() * n, w.size()); //       0 to 1            -->   0 to max
        double target_n = scaled_n - 0.5f;                       //       0 to max          -->   -0.5 to max-0.5
        if(target_n <= 0) return compute(0);                       // Return if too low to be interpolated
        if(target_n >= w.size() - 1) return compute(w.size() - 1); // Return if too high to be interpolated

        int base_material = (int)target_n;
        int alt_material  = base_material + 1;
        double chance = target_n - base_material;
        return compute(rnd.nextFloat() > chance ? base_material : alt_material);
    }

    private Material compute(int i) {
        Object value = w.get(i);
        if(value instanceof Material r) return r;
        if(value instanceof BlockPattern r) return r.get();
        else throw new RuntimeException("Block gradient created using invalid Object type \"" + value.getClass().getName() + "\"");
    }
}
