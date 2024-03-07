package org.uwu_snek.shadownight.items.implementations.dagger;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_Standard;
import org.uwu_snek.shadownight.items.Ability;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public abstract class IM_Dagger extends IM {
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

        Ability abilitySpeed = new Ability(true, 0.6d, (player, item) -> speedAbility(player));
        setAbilities(
            null,
            null,
            abilitySpeed,
            abilitySpeed
        );
    }




    static private void speedAbility(final @NotNull Player player) {
        player.setWalkSpeed(0.4f);
        Scheduler.delay(() -> player.setWalkSpeed(0.2f), 20 * 4L);
    }
}
