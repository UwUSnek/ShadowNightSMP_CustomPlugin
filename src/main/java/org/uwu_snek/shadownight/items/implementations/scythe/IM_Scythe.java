package org.uwu_snek.shadownight.items.implementations.scythe;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_ConeArea;
import org.uwu_snek.shadownight.items.Ability;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.items.IM_MeleeWeapon;
import org.uwu_snek.shadownight.utils.blockdata.BlockProperty;
import org.uwu_snek.shadownight.utils.spigot.ItemUtils;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public abstract class IM_Scythe extends IM_MeleeWeapon {
    public IM_Scythe(final @NotNull String _displayName, final @NotNull _custom_item_id _customItemId, final double _hitDamage, final double _atkSpeed) {
        super(
            _displayName,
            _customItemId,
            new ATK_ConeArea(6),
            _hitDamage,
            1.5d,
            _atkSpeed,
            0.5d,
            true
        );

        Ability abilityBreak = new Ability(
            "Harvesting",
            new String[]{ "Break crops and vegetation in a §a" + (int)breakWidth + "*" + (int)breakLen + "§7 area in front of you." },
            true,
            1d,
            IM_Scythe::breakBlocks
        );
        setAbilities(
            null,
            null,
            abilityBreak,
            abilityBreak
        );
    }

    static final double breakWidth = 4;
    static final double breakLen = 10;

    static private void breakBlocks(final @NotNull Player player, final @NotNull ItemStack item) {
        Location eyePos = player.getEyeLocation();
        Vector dir = eyePos.getDirection();


        //TODO REPLACE WITH SCHEDULER METHOD
        for (double l = 0; l < breakLen; l += 0.5) {
            final double lFinal = l;
            Scheduler.delay(() -> {
                for (int h = -1; h <= 1; ++h) {
                    boolean blocksBroken = false;
                    for (double w = -breakWidth; w < breakWidth; w += 0.5) {
                        final Block target = eyePos.clone()
                            .add(new Vector(dir.getX(), 0, dir.getZ()).rotateAroundY(Math.PI / 2).multiply(w))
                            .add(dir.clone().multiply(lFinal)
                            .add(new Vector(0, h, 0))
                        ).getBlock();

                        if (BlockProperty.isVegetation(target.getType())) {
                            target.breakNaturally(true);
                            blocksBroken = true;
                        }
                    }
                    if (blocksBroken) ItemUtils.damageItem(player, item, 1);
                }
                ItemUtils.damageItem(player, item, 1);
            }, (long)(l * 2));
        }
        ATK.simulateSweepingEffect(player.getLocation());
    }
}
