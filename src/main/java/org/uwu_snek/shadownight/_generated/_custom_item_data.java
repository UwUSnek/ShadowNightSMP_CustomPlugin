package org.uwu_snek.shadownight._generated;
import org.jetbrains.annotations.NotNull;
import org.javatuples.Pair;
import org.bukkit.Material;
public final class _custom_item_data extends org.uwu_snek.shadownight.utils.UtilityClass {
    public static Pair<Material, Integer> getMaterialAndModel(@NotNull final _custom_item_id id) {
        return switch(id) {
            case GOLDEN_DAGGER -> Pair.with(Material.GOLDEN_SWORD, 10000);
            case GOLDEN_SPEAR -> Pair.with(Material.GOLDEN_SWORD, 10001);
            case STONE_DAGGER -> Pair.with(Material.STONE_SWORD, 10000);
            case STONE_SPEAR -> Pair.with(Material.STONE_SWORD, 10001);
            case IRON_SCYTHE -> Pair.with(Material.IRON_SWORD, 10000);
            case IRON_DAGGER -> Pair.with(Material.IRON_SWORD, 10001);
            case IRON_SPEAR -> Pair.with(Material.IRON_SWORD, 10002);
            case DIAMOND_SCYTHE -> Pair.with(Material.DIAMOND_SWORD, 10000);
            case DIAMOND_DAGGER -> Pair.with(Material.DIAMOND_SWORD, 10001);
            case DIAMOND_SPEAR -> Pair.with(Material.DIAMOND_SWORD, 10002);
            case KLAUE_SCYTHE -> Pair.with(Material.NETHERITE_SWORD, 10000);
            case NETHERITE_SCYTHE -> Pair.with(Material.NETHERITE_SWORD, 10001);
            case NETHERITE_DAGGER -> Pair.with(Material.NETHERITE_SWORD, 10002);
            case NETHERITE_SPEAR -> Pair.with(Material.NETHERITE_SWORD, 10003);
            case HELLFIRE_BOW -> Pair.with(Material.BOW, 10000);
        };
    }
}