package org.uwu_snek.shadownight.customItems.implementations.dagger;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_Standard;
import org.uwu_snek.shadownight.customItems.itemFilter.decorators.Decorator;
import org.uwu_snek.shadownight.customItems.Ability;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.customItems.IM_MeleeWeapon;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public abstract class IM_Dagger extends IM_MeleeWeapon {
    private static final int speedDuration = 4;

    public IM_Dagger(final @NotNull String _displayName, final @NotNull _custom_item_id _customItemId, final double _hitDamage, final double _atkSpeed) {
        super(
            _displayName,
            _customItemId,
            new ATK_Standard(),
            _hitDamage,
            0.5d,
            _atkSpeed,
            0.05d,
            false
        );

        Ability abilitySpeed = new Ability(
            "Speed Boost",
            "Gain " + Decorator.formatValue("+" + 100 + "%") + " walking speed for " + Decorator.formatValue(speedDuration + "s") + ".",
            true,
            6d,
            (player, item) -> speedAbility(player)
        );
        setAbilities(
            null,
            null,
            abilitySpeed,
            abilitySpeed
        );
    }




    static private void speedAbility(final @NotNull Player player) {
        player.setWalkSpeed(0.4f);
        Scheduler.delay(() -> player.setWalkSpeed(0.2f), 20L * speedDuration);
    }
}
