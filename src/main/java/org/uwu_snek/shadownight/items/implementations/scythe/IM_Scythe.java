package org.uwu_snek.shadownight.items.implementations.scythe;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_ConeArea;
import org.uwu_snek.shadownight.items.Ability;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.IM;

import java.util.Objects;
import java.util.UUID;


public abstract class IM_Scythe extends IM {
    public IM_Scythe(final @NotNull String _displayName, final @NotNull CustomItemId _customItemId, final double _hitDamage, final double _atkSpeed) {
        super(
            _displayName,
            _customItemId,
            new ATK_ConeArea(6),
            _hitDamage,
            1.5d,
            _atkSpeed,
            0.5d
        );

        Ability abilityAttack = new Ability(true, 0d, (player, item) -> attack.execute(player, null, player.getLocation(), item));
        Ability abilityBreak  = new Ability(true, 1d, IM_Scythe::breakBlocks);
        setAbilities(
            abilityAttack,
            abilityAttack,
            abilityBreak,
            abilityBreak
        );
    }


    static private void breakBlocks(final @NotNull Player player, final @NotNull ItemStack item) {
        Location playerPos = player.getLocation();
        Vector playerDirection = playerPos.getDirection();

        player.sendMessage("debug: used rclick ability");
        //for()
    }
}
