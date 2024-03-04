package org.uwu_snek.shadownight.items.dagger;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_Standard;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.utils.ResetPotionEffect;




public final class IM_IronDagger extends IM_Dagger_NormalRecipe {
    public IM_IronDagger() {
        super(
            "Iron Dagger",
            CustomItemId.IRON_DAGGER,
            3,
            0.25
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.IRON_INGOT;
    }
}
