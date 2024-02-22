package org.shadownight.plugin.shadownight.utils.blockdata;


import org.bukkit.Material;
import org.bukkit.Tag;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;




public final class BlockProperty extends UtilityClass {
    /**
     * Identifies blocks that can be broken and/or replaced by destructive abilities.
     * @param material The material of the block
     * @return True if the block can be destroyed, false otherwise
     */
    public static boolean isDelicate(final @NotNull Material material) {
        return
            Tag.REPLACEABLE      .isTagged(material) ||
            Tag.CORAL_PLANTS     .isTagged(material) ||
            Tag.WALL_CORALS      .isTagged(material) ||
            Tag.CROPS            .isTagged(material) ||
            Tag.WOOL_CARPETS     .isTagged(material) || material == Material.MOSS_CARPET ||
            Tag.FLOWER_POTS      .isTagged(material) ||
            Tag.FLOWERS          .isTagged(material) ||
            Tag.PRESSURE_PLATES  .isTagged(material) ||
            Tag.RAILS            .isTagged(material) ||
            Tag.SAPLINGS         .isTagged(material) ||
            Tag.ALL_SIGNS        .isTagged(material) ||
            Tag.ALL_HANGING_SIGNS.isTagged(material) ||
            Tag.BUTTONS          .isTagged(material) ||
            Tag.CANDLES          .isTagged(material) ||

            material == Material.CAVE_AIR ||
            material == Material.VOID_AIR ||
            material == Material.WATER ||
            material == Material.LAVA ||
            material == Material.SNOW ||
            material == Material.SCULK_VEIN ||

            material == Material.WEEPING_VINES ||
            material == Material.TWISTING_VINES ||
            material == Material.KELP ||
            material == Material.WEEPING_VINES_PLANT ||
            material == Material.TWISTING_VINES_PLANT ||
            material == Material.KELP_PLANT ||
            material == Material.BROWN_MUSHROOM ||
            material == Material.RED_MUSHROOM ||
            material == Material.CRIMSON_FUNGUS ||
            material == Material.WARPED_FUNGUS ||
            material == Material.SMALL_DRIPLEAF ||
            material == Material.BIG_DRIPLEAF ||
            material == Material.SWEET_BERRY_BUSH ||
            material == Material.LILY_PAD ||

            material == Material.TORCH ||
            material == Material.SOUL_TORCH ||
            material == Material.WALL_TORCH ||
            material == Material.SOUL_WALL_TORCH ||

            material == Material.STRING ||
            material == Material.TURTLE_EGG ||
            material == Material.FROGSPAWN
        ;
    }


    /**
     * Identifies blocks that can be walked through without the need to break them, sneak or crouch.
     * @param material The material of the block
     * @return True if the block is walkable, false otherwise
     */
    public static boolean isWalkable(final @NotNull Material material) {
        return
            Tag.REPLACEABLE      .isTagged(material) ||
            Tag.CORAL_PLANTS     .isTagged(material) ||
            Tag.WALL_CORALS      .isTagged(material) ||
            Tag.CROPS            .isTagged(material) ||
            Tag.WOOL_CARPETS     .isTagged(material) || material == Material.MOSS_CARPET ||
            Tag.FLOWER_POTS      .isTagged(material) ||
            Tag.FLOWERS          .isTagged(material) ||
            Tag.PRESSURE_PLATES  .isTagged(material) ||
            Tag.RAILS            .isTagged(material) ||
            Tag.SAPLINGS         .isTagged(material) ||
            Tag.ALL_SIGNS        .isTagged(material) ||
            Tag.ALL_HANGING_SIGNS.isTagged(material) ||
            Tag.BUTTONS          .isTagged(material) ||
            Tag.CANDLES          .isTagged(material) ||

            material == Material.CAVE_AIR ||
            material == Material.VOID_AIR ||
            material == Material.WATER ||
            material == Material.LAVA ||
            material == Material.SNOW ||
            material == Material.SCULK_VEIN ||

            material == Material.WEEPING_VINES ||
            material == Material.TWISTING_VINES ||
            material == Material.KELP ||
            material == Material.WEEPING_VINES_PLANT ||
            material == Material.TWISTING_VINES_PLANT ||
            material == Material.KELP_PLANT ||
            material == Material.BROWN_MUSHROOM ||
            material == Material.RED_MUSHROOM ||
            material == Material.CRIMSON_FUNGUS ||
            material == Material.WARPED_FUNGUS ||
            material == Material.SMALL_DRIPLEAF ||
            material == Material.BIG_DRIPLEAF ||
            material == Material.SWEET_BERRY_BUSH ||
            material == Material.LILY_PAD ||
            material == Material.SUGAR_CANE ||

            material == Material.REDSTONE_WIRE ||
            material == Material.REDSTONE_TORCH ||
            material == Material.TORCH ||
            material == Material.SOUL_TORCH ||
            material == Material.REDSTONE_WALL_TORCH ||
            material == Material.WALL_TORCH ||
            material == Material.SOUL_WALL_TORCH ||

            material == Material.LADDER ||
            material == Material.STRING ||
            material == Material.TRIPWIRE_HOOK ||
            material == Material.TURTLE_EGG ||
            material == Material.FROGSPAWN ||
            material == Material.AMETHYST_CLUSTER ||
            material == Material.SMALL_AMETHYST_BUD ||
            material == Material.MEDIUM_AMETHYST_BUD ||
            material == Material.LARGE_AMETHYST_BUD ||

            material == Material.LEVER ||
            material == Material.REPEATER ||
            material == Material.COMPARATOR ||
            material == Material.LIGHT
        ;
    }
}
