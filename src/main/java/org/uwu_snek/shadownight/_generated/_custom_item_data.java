package org.uwu_snek.shadownight._generated;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.javatuples.Pair;
import org.bukkit.Material;
public final class _custom_item_data extends org.uwu_snek.shadownight.utils.UtilityClass {
    public static Pair<Material, Integer> getMaterialAndModel(@NotNull final _custom_item_id id) {
        if(id == _custom_item_id.GOLDEN_DAGGER) return Pair.with(Material.GOLDEN_SWORD, 10000);
        if(id == _custom_item_id.GOLDEN_SPEAR) return Pair.with(Material.GOLDEN_SWORD, 10001);
        if(id == _custom_item_id.STONE_DAGGER) return Pair.with(Material.STONE_SWORD, 10000);
        if(id == _custom_item_id.STONE_SPEAR) return Pair.with(Material.STONE_SWORD, 10001);
        if(id == _custom_item_id.IRON_SCYTHE) return Pair.with(Material.IRON_SWORD, 10000);
        if(id == _custom_item_id.IRON_DAGGER) return Pair.with(Material.IRON_SWORD, 10001);
        if(id == _custom_item_id.IRON_SPEAR) return Pair.with(Material.IRON_SWORD, 10002);
        if(id == _custom_item_id.DIAMOND_SCYTHE) return Pair.with(Material.DIAMOND_SWORD, 10000);
        if(id == _custom_item_id.DIAMOND_DAGGER) return Pair.with(Material.DIAMOND_SWORD, 10001);
        if(id == _custom_item_id.DIAMOND_SPEAR) return Pair.with(Material.DIAMOND_SWORD, 10002);
        if(id == _custom_item_id.KLAUE_SCYTHE) return Pair.with(Material.NETHERITE_SWORD, 10000);
        if(id == _custom_item_id.NETHERITE_SCYTHE) return Pair.with(Material.NETHERITE_SWORD, 10001);
        if(id == _custom_item_id.NETHERITE_DAGGER) return Pair.with(Material.NETHERITE_SWORD, 10002);
        if(id == _custom_item_id.NETHERITE_SPEAR) return Pair.with(Material.NETHERITE_SWORD, 10003);
        if(id == _custom_item_id.HELLFIRE_BOW) return Pair.with(Material.BOW, 10000);
        throw new RuntimeException("Invalid _custom_item_id \"" + id + "\"");
    }
}