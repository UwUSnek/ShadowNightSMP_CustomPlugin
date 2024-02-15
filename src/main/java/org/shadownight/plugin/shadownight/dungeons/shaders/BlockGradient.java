package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;
import org.javatuples.Pair;
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
        float scaled_n = Math.min((int)(w.size() * n), w.size() - 1);
        int b = (int)scaled_n;

        float p = scaled_n - b;
        if(p > 0.75) return rnd.nextFloat() < (p - 0.5) * 4 ? w.get(b) : w.get(Math.max(b + 1, w.size() - 1));
        if(p < 0.25) return rnd.nextFloat() > p         * 4 ? w.get(b) : w.get(Math.max(0, b - 1));
        else return w.get(b);
    }
}
