package org.uwu_snek.shadownight.items.implementations.spear;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_LineArea;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.IM;




public final class IM_StoneSpear extends IM_Spear_NormalRecipe {
    public IM_StoneSpear() {
        super(
            "Stone Spear",
            CustomItemId.STONE_SPEAR,
            5,
            0.625 // Swords default
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.COBBLESTONE;
    }
}
