package org.uwu_snek.shadownight.items.implementations.spear;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_LineArea;
import org.uwu_snek.shadownight.items.Ability;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.IM;




public abstract class IM_Spear extends IM {
    public IM_Spear(final @NotNull String _displayName, final @NotNull CustomItemId _customItemId, final double _hitDamage, final double _atkSpeed) {
        super(
            _displayName,
            _customItemId,
            new ATK_LineArea(8, 1, false),
            _hitDamage,
            1d,
            _atkSpeed,
            0.25d
        );

        Ability abilityAttack = new Ability(true, 0d, (player, item) -> attack.execute(player, null, player.getLocation(), item));
        Ability abilityHit    = new Ability(true, 0d, IM_Spear::hitAbility);
        setAbilities(
            abilityAttack,
            abilityAttack,
            abilityHit,
            abilityHit
        );
    }




    static private void hitAbility(final @NotNull Player player, final @NotNull ItemStack item) {
        Location playerPos = player.getLocation();
        Vector playerDirection = playerPos.getDirection();

        player.sendMessage("debug: used rclick ability");
    }
}
