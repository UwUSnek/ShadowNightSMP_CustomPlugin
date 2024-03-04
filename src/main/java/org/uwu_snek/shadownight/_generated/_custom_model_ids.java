package org.uwu_snek.shadownight._generated;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.javatuples.Pair;
import org.bukkit.Material;
public final class _custom_model_ids extends org.uwu_snek.shadownight.utils.UtilityClass {
    public static Pair<Material, Integer> getMaterialAndModel(@NotNull final CustomItemId id) {
        if(id == CustomItemId.GOLDEN_DAGGER) return Pair.with(Material.GOLDEN_SWORD, 10000);
        if(id == CustomItemId.GOLDEN_SPEAR) return Pair.with(Material.GOLDEN_SWORD, 10001);
        if(id == CustomItemId.STONE_DAGGER) return Pair.with(Material.STONE_SWORD, 10000);
        if(id == CustomItemId.STONE_SPEAR) return Pair.with(Material.STONE_SWORD, 10001);
        if(id == CustomItemId.IRON_SCYTHE) return Pair.with(Material.IRON_SWORD, 10000);
        if(id == CustomItemId.IRON_DAGGER) return Pair.with(Material.IRON_SWORD, 10001);
        if(id == CustomItemId.IRON_SPEAR) return Pair.with(Material.IRON_SWORD, 10002);
        if(id == CustomItemId.DIAMOND_SCYTHE) return Pair.with(Material.DIAMOND_SWORD, 10000);
        if(id == CustomItemId.DIAMOND_DAGGER) return Pair.with(Material.DIAMOND_SWORD, 10001);
        if(id == CustomItemId.DIAMOND_SPEAR) return Pair.with(Material.DIAMOND_SWORD, 10002);
        if(id == CustomItemId.KLAUE_SCYTHE) return Pair.with(Material.NETHERITE_SWORD, 10000);
        if(id == CustomItemId.NETHERITE_SCYTHE) return Pair.with(Material.NETHERITE_SWORD, 10001);
        if(id == CustomItemId.NETHERITE_DAGGER) return Pair.with(Material.NETHERITE_SWORD, 10002);
        if(id == CustomItemId.NETHERITE_SPEAR) return Pair.with(Material.NETHERITE_SWORD, 10003);
        if(id == CustomItemId.HELLFIRE_BOW) return Pair.with(Material.BOW, 10000);
        throw new RuntimeException("Invalid CustomItemId \"" + id + "\"");
    }
}