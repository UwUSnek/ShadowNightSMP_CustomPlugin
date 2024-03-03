package org.uwu_snek.shadownight.items.scythe;

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
import org.uwu_snek.shadownight.attackOverride.attacks.ATK_ConeArea;
import org.uwu_snek.shadownight.items.IM;

import java.util.Objects;
import java.util.UUID;


public abstract class IM_Scythe extends IM {
    public IM_Scythe() {
        super(new ATK_ConeArea(6, 500));
    }

    protected abstract double getAttackSpeed();



    @Override
    protected void setItemAttributes() {
        final ItemMeta meta = defaultItem.getItemMeta();
        Objects.requireNonNull(meta, "Object meta is null");
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,  new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed",  getAttackSpeed(), AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        defaultItem.setItemMeta(meta);
    }




    static private void breakBlocks(final @NotNull Player player) {
        Location playerPos = player.getLocation();
        Vector playerDirection = playerPos.getDirection();

        player.sendMessage("debug: used rclick ability");
        //for()
    }



    @Override
    protected void onInteract(final @NotNull PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            attack.execute(event.getPlayer(), null, event.getPlayer().getLocation(), event.getItem());
        }
        else if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            breakBlocks(event.getPlayer());
        }
    }
}
