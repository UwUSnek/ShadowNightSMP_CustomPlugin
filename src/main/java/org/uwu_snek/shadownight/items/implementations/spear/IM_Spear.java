package org.uwu_snek.shadownight.items.implementations.spear;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_LineArea;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_ReachArea;
import org.uwu_snek.shadownight.items.Ability;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.items.IM;




public abstract class IM_Spear extends IM {
    private static final ATK_ReachArea abilityAttack = new ATK_ReachArea(5);

    public IM_Spear(final @NotNull String _displayName, final @NotNull _custom_item_id _customItemId, final double _hitDamage, final double _atkSpeed) {
        super(
            _displayName,
            _customItemId,
            new ATK_LineArea(8, 1, false),
            _hitDamage,
            1.5d,
            _atkSpeed,
            0.25d,
            true
        );

        Ability abilityHit = new Ability(true, 2d, IM_Spear::hitAbility);
        setAbilities(
            null,
            null,
            abilityHit,
            abilityHit
        );
    }




    static private void hitAbility(final @NotNull Player player, final @NotNull ItemStack item) {
        abilityAttack.execute(player, null, player.getLocation(), item);
    }
}
