package org.shadownight.plugin.shadownight.utils.graphics;

import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.Rnd;

import java.util.ArrayList;


/**
 * A class that can represent a block pattern using Materials and weight values.
 * Blocks can be generated from an instance using the get() method.
 * Creating new instances every time is inefficient. Save the instances outside of loops or use static members.
 */
public final class BlockPattern implements Rnd {
    private final ArrayList<Pair<Float, BlockData>> w = new ArrayList<>();


    /**
     * Create the block pattern.
     * @param blocks A list of Pairs each containing the weight and BlockData of a given block.
     */
    @SafeVarargs
    public BlockPattern(@NotNull final Pair<Float, BlockData>... blocks) {
        float tot = 0f;
        for (Pair<Float, BlockData> block : blocks) {
            tot += block.getValue0();
        }

        float prev = 0;
        for (Pair<Float, BlockData> block : blocks) {
            float n = block.getValue0() / tot;
            w.add(Pair.with(n + prev, block.getValue1()));
            prev += n;
        }
    }




    /**
     * Generate a random block based on the weight of the blocks configured in this instance.
     * @return The generated block
     */
    public BlockData get() {
        float r = rnd.nextFloat();
        for(int i = 0; i < w.size() - 1; ++i) { // Skip the last weight as it would always be "r < ~1.0f" and float precision could cause problems
            if(r < w.get(i).getValue0()) return w.get(i).getValue1();
        }
        return w.get(w.size() - 1).getValue1();
    }
}

