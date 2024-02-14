package org.shadownight.plugin.shadownight.dungeons.shaders;


import org.bukkit.Material;

import java.util.Random;


public abstract class SHD_GeneratorShader {
    protected static Random rnd = new Random();
    public Material targetMaterial;

    public SHD_GeneratorShader(Material _targetMaterial) {
        targetMaterial = _targetMaterial;
    }
    public abstract Material exec(int x, int y, int z, int gridSize);
}
