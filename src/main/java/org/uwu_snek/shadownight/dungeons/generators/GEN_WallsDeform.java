package org.uwu_snek.shadownight.dungeons.generators;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector2i;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.graphics.PerlinNoise3D;
import org.uwu_snek.shadownight.utils.containers.RegionBlueprint;
import org.uwu_snek.shadownight.utils.containers.BlueprintData;
import org.uwu_snek.shadownight.utils.math.Func;


public final class GEN_WallsDeform extends UtilityClass {
    /**
     * Deforms existing walls.
     * @param buffer The data buffer
     * @param ft The thickness of the floor
     * @param h The height of the walls
     */
    public static void start(final @NotNull RegionBlueprint buffer, final int ft, final int h){
        final boolean[][][] tmp = new boolean[buffer.x][buffer.y][buffer.z]; // Defaults to false

        // Create modified copy
        for(int i = 1; i < buffer.x - 1; ++i) for(int j = ft; j < buffer.y; ++j) for(int k = 1; k < buffer.z - 1; ++k) {
            if (buffer.get(i, j, k) == BlueprintData.WALL) {
                Vector2i shift = new Vector2i(
                    (buffer.get(i + 1, j, k) == BlueprintData.AIR ? -1 : 0) + (buffer.get(i - 1, j, k) == BlueprintData.AIR ? 1 : 0),
                    (buffer.get(i, j, k + 1) == BlueprintData.AIR ? -1 : 0) + (buffer.get(i, j, k - 1) == BlueprintData.AIR ? 1 : 0)
                );


                // Calculate noise values
                final double noise1 = PerlinNoise3D.compute(i, j, k, 32); // Base noise layer
                final double noise2 = PerlinNoise3D.compute(i, j, k, 6); // Fine noise layer
                final int scaledNoise = Func.clampMin((int) (Math.sqrt(Func.linearIntSafe(0.5, noise1, noise2) * 16 * ((float) (j - ft) / (h / 2f))) - 0.5), 0); // The number of blocks that have to be removed

                // Carve straight walls
                if(shift.x != 0 || shift.y != 0) {
                    if (shift.x != 0) {
                        final int target = i + shift.x * scaledNoise; // The block in the current axis that the wall has to be carved until
                        for (int i2 = i; i2 != target; i2 += shift.x) {
                            tmp[Func.clamp(i2, 0, buffer.x - 1)][j][k] = true;
                        }
                    }
                    if (shift.y != 0) {
                        final int target = k + shift.y * scaledNoise; // The block in the current axis that the wall has to be carved until
                        for (int k2 = k; k2 != target; k2 += shift.y) {
                            tmp[i][j][Func.clamp(k2, 0, buffer.z - 1)] = true;
                        }
                    }
                }

                // Carve corners
                else {
                    if(buffer.get(i + 1, j, k + 1) == BlueprintData.AIR) shift.add(new Vector2i(-1, -1));
                    if(buffer.get(i - 1, j, k + 1) == BlueprintData.AIR) shift.add(new Vector2i( 1, -1));
                    if(buffer.get(i + 1, j, k - 1) == BlueprintData.AIR) shift.add(new Vector2i(-1,  1));
                    if(buffer.get(i - 1, j, k - 1) == BlueprintData.AIR) shift.add(new Vector2i( 1,  1));
                    if(shift.x != 0) { // shift.y != 0 covered by the x condition
                        final Vector2i target = new Vector2i(i, k).add(new Vector2i(shift).mul(scaledNoise)); // The block that the wall has to be carved until
                        for (int i2 = i; i2 != target.x; i2 += shift.x) for (int k2 = k; k2 != target.y; k2 += shift.y) {
                            tmp[Func.clamp(i2, 0, buffer.x - 1)][j][Func.clamp(k2, 0, buffer.z - 1)] = true;
                        }
                    }
                }
            }
        }

        // Paste copy back into the original buffer
        for(int i = 0; i < buffer.x; ++i) for(int j = ft; j < buffer.y; ++j) for(int k = 0; k < buffer.z; ++k) {
            if(tmp[i][j][k]) buffer.set(i, j, k, BlueprintData.AIR);
        }
    }
}
