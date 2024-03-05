package org.uwu_snek.shadownight.items.implementations.dagger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_LineArea;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_Standard;
import org.uwu_snek.shadownight.items.Ability;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.IM;
import org.uwu_snek.shadownight.items.implementations.spear.IM_Spear;
import org.uwu_snek.shadownight.utils.ResetPotionEffect;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;
import org.uwu_snek.shadownight.utils.utils;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;




public abstract class IM_Dagger extends IM {
    public IM_Dagger(final @NotNull String _displayName, final @NotNull CustomItemId _customItemId, final double _hitDamage, final double _atkSpeed) {
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
