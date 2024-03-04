package org.uwu_snek.shadownight.items.dagger;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_LineArea;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_Standard;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.IM;

import java.util.Objects;
import java.util.UUID;




public abstract class IM_Dagger extends IM {
    public IM_Dagger(final @NotNull String _displayName, final @NotNull CustomItemId _customItemId, final double _hitDamage, final double _atkSpeed) {
        super(
            _displayName,
            _customItemId,
            new ATK_Standard(),
            _hitDamage,
            0.5,
            _atkSpeed
        );
    }


    static private void rclickAbility(final @NotNull Player player) {
        Location playerPos = player.getLocation();
        Vector playerDirection = playerPos.getDirection();

        player.sendMessage("debug: used rclick ability");
    }



    @Override
    protected void onInteract(final @NotNull PlayerInteractEvent event) {
         if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            rclickAbility(event.getPlayer());
        }
    }
}
