package org.uwu_snek.shadownight.customItems;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.utils.UtilityClass;

import java.util.AbstractMap;
import java.util.Map;




public final class VanillaProvider extends UtilityClass {
    private static final Map<Material, Double> vanillaDamage = Map.ofEntries(
        new AbstractMap.SimpleEntry<>(Material.WOODEN_SWORD,      4d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_SWORD,      4d),
        new AbstractMap.SimpleEntry<>(Material.STONE_SWORD,       5d),
        new AbstractMap.SimpleEntry<>(Material.IRON_SWORD,        6d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_SWORD,     7d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_SWORD,   8d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_AXE,        7d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_AXE,        7d),
        new AbstractMap.SimpleEntry<>(Material.STONE_AXE,         9d),
        new AbstractMap.SimpleEntry<>(Material.IRON_AXE,          9d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_AXE,       9d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_AXE,     10d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_PICKAXE,    2d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_PICKAXE,    2d),
        new AbstractMap.SimpleEntry<>(Material.STONE_PICKAXE,     3d),
        new AbstractMap.SimpleEntry<>(Material.IRON_PICKAXE,      4d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_PICKAXE,   5d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_PICKAXE, 6d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_SHOVEL,     2.5d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_SHOVEL,     2.5d),
        new AbstractMap.SimpleEntry<>(Material.STONE_SHOVEL,      3.5d),
        new AbstractMap.SimpleEntry<>(Material.IRON_SHOVEL,       4.5d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_SHOVEL,    5.5d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_SHOVEL,  6.5d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_HOE,        1d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_HOE,        1d),
        new AbstractMap.SimpleEntry<>(Material.STONE_HOE,         1d),
        new AbstractMap.SimpleEntry<>(Material.IRON_HOE,          1d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_HOE,       1d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_HOE,     1d),

        new AbstractMap.SimpleEntry<>(Material.TRIDENT,           9d)
    );


    private static final Map<Material, Double> vanillaAts = Map.ofEntries(
        new AbstractMap.SimpleEntry<>(Material.WOODEN_SWORD,      0.625d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_SWORD,      0.625d),
        new AbstractMap.SimpleEntry<>(Material.STONE_SWORD,       0.625d),
        new AbstractMap.SimpleEntry<>(Material.IRON_SWORD,        0.625d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_SWORD,     0.625d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_SWORD,   0.625d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_AXE,        1.25d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_AXE,        1d),
        new AbstractMap.SimpleEntry<>(Material.STONE_AXE,         1.25d),
        new AbstractMap.SimpleEntry<>(Material.IRON_AXE,          1d / 0.9d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_AXE,       1d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_AXE,     1d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_PICKAXE,    1d / 1.2d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_PICKAXE,    1d / 1.2d),
        new AbstractMap.SimpleEntry<>(Material.STONE_PICKAXE,     1d / 1.2d),
        new AbstractMap.SimpleEntry<>(Material.IRON_PICKAXE,      1d / 1.2d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_PICKAXE,   1d / 1.2d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_PICKAXE, 1d / 1.2d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_SHOVEL,     1d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_SHOVEL,     1d),
        new AbstractMap.SimpleEntry<>(Material.STONE_SHOVEL,      1d),
        new AbstractMap.SimpleEntry<>(Material.IRON_SHOVEL,       1d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_SHOVEL,    1d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_SHOVEL,  1d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_HOE,        1d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_HOE,        1d),
        new AbstractMap.SimpleEntry<>(Material.STONE_HOE,         0.5d),
        new AbstractMap.SimpleEntry<>(Material.IRON_HOE,          1d / 3d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_HOE,       0.25d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_HOE,     0.25d),

        new AbstractMap.SimpleEntry<>(Material.TRIDENT,           1d / 1.1d)
    );


    /**
     * Retrieves the default Vanilla damage of an item.
     * @param material The item material
     * @return The damage, or 1 if the item isn't a weapon or tool
     */
    public static double getDamage(final @NotNull Material material) {
        final Double n = vanillaDamage.get(material);
        return n == null ? 1 : n;
    }

    /**
     * Retrieves the default Vanilla attack speed of an item, expressed in seconds between attacks.
     * @param material The item material
     * @return The attack speed, or the default 0.25s if the item isn't a weapon or tool.
     */
    public static double getAts(final @NotNull Material material) {
        final Double n = vanillaAts.get(material);
        return n == null ? 0.25 : n;
    }
}
