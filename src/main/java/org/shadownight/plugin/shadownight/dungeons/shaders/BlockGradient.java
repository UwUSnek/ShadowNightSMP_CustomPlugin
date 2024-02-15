package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.shadownight.plugin.shadownight.utils.Rnd;

import java.util.ArrayList;
import java.util.Arrays;


public class BlockGradient extends Rnd {
    private final ArrayList<Material> w;


    /**
     * Create the block gradient
     * @param blocks A list of tuples each containing the weight and the block of a given Material
     */
    public BlockGradient(Material... blocks) {
        w = new ArrayList<>(Arrays.asList(blocks));
    }


    /**
     * Generate the block based on the weight of the blocks configured in this instance
     * @return The generated block
     */
    public Material get(float n) {
        /*
        float scaled_n = Math.min(w.size() * n, w.size() - 0.5f);
        int b = (int)scaled_n;

        float p = scaled_n - b;
        if(p > 0.5) return rnd.nextFloat() * 0.5 > (p - 0.5) ? w.get(b) : w.get(Math.max(b + 1, w.size() - 1));
        if(p < 0.5) return rnd.nextFloat() * 0.5 < p         ? w.get(b) : w.get(Math.max(0, b - 1));
        else return w.get(b);
        */

        float scaled_n = Math.min(w.size() * n, w.size());       //       0 to 1            -->   0 to max
        float target_n = scaled_n - 0.5f;                        //       0 to max          -->   -0.5 to max-0.5
        if(target_n <= 0) return w.get(0);                       // Return if too low to be interpolated
        if(target_n >= w.size() - 1) return w.get(w.size() - 1); // Return if too high to be interpolated

        int base_material = (int)target_n;
        int alt_material  = base_material + 1;
        float chance = target_n - base_material;
        return w.get(rnd.nextFloat() > chance ? base_material : alt_material);
    }
}
