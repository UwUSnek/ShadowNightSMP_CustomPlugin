package org.shadownight.plugin.shadownight.utils;


import org.bukkit.Material;

import java.util.Random;



public class GetRandom {
    private static Random rnd = new Random();


    public static Material block(Material... blocks) {
        return blocks[rnd.nextInt(0, blocks.length)];
    }

    public static Material blockWeighted(Material... blocks) {
        return Material.STONE;
    }
}
