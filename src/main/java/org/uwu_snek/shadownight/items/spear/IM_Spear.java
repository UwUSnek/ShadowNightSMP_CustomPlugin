package org.uwu_snek.shadownight.items.spear;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_LineArea;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.items.IM;




public abstract class IM_Spear extends IM {
    public IM_Spear(final @NotNull String _displayName, final @NotNull CustomItemId _customItemId, final double _hitDamage, final double _atkSpeed) {
        super(
            _displayName,
            _customItemId,
            new ATK_LineArea(6, 1),
            _hitDamage,
            1,
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
