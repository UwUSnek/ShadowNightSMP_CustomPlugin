package org.shadownight.plugin.shadownight.items.scythe;

import com.google.common.collect.HashMultimap;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.attackOverride.AttackOverride;
import org.shadownight.plugin.shadownight.attackOverride.attacks.ATK_ConeArea;
import org.shadownight.plugin.shadownight.items.IM;
import org.shadownight.plugin.shadownight.utils.math.Func;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;

import java.util.HashMap;
import java.util.List;
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




    static private void breakBlocks(@NotNull final Player player) {
        Location playerPos = player.getLocation();
        Vector playerDirection = playerPos.getDirection();

        player.sendMessage("debug: used rclick ability");
        //for()
    }



    @Override
    protected void onInteract(final @NotNull PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            attack.execute(event.getPlayer(), null, event.getItem());
        }
        else if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            breakBlocks(event.getPlayer());
        }
    }
}
