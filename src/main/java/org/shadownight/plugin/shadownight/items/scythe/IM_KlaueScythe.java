package org.shadownight.plugin.shadownight.items.scythe;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.items.CustomItemId;
import org.shadownight.plugin.shadownight.utils.blockdata.BlockProperty;
import org.shadownight.plugin.shadownight.utils.spigot.PlayerUtils;
import org.shadownight.plugin.shadownight.utils.spigot.Scheduler;




public final class IM_KlaueScythe extends IM_Scythe {
    @Override public Material     getMaterial()        { return Material.NETHERITE_SWORD;  }
    @Override public String       getDisplayName()     { return "§6Edgy Scythe";           }
    @Override public int          getCustomModelData() { return 14;                        }
    @Override public CustomItemId getCustomId()        { return CustomItemId.KLAUE_SCYTHE; }

    @Override protected double getAttackSpeed() { return -3.0; }
    @Override public    double getHitDamage()   { return 18;   }
    @Override public double getHitKnockbackMultiplier() { return 2; }


    @Override
    protected void setRecipe(final @NotNull ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.COMMAND_BLOCK);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }








    @Override
    protected void onInteract(final @NotNull PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            event.setCancelled(true);
            if(player.isSneaking()) player.sendMessage("shadow fury knock off");
            //else customAttack(player, event.getItem());
            else attack.execute(player, null, player.getLocation(), event.getItem());
        }
        else if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            event.setCancelled(true);
            if(player.isSneaking()) {
                //Block targetBlock = player.getTargetBlockExact(200, FluidCollisionMode.NEVER);
                Block targetBlock = PlayerUtils.getTargetBlock(player.getEyeLocation(), BlockProperty::isWalkable, 200);
                if(targetBlock != null) {
                    Location targetBlockLocation = targetBlock.getLocation();
                    Location playerLocation = player.getLocation();
                    // Teleport with delay. 0 delay causes the event to be fired twice
                    Scheduler.delay(() -> {
                        player.teleport(new Location(
                            playerLocation.getWorld(),
                            targetBlockLocation.getX() + 0.5,
                            targetBlockLocation.getY() + 1,
                            targetBlockLocation.getZ() + 0.5,
                            playerLocation.getYaw(),
                            playerLocation.getPitch()
                        ));
                        player.getWorld().spawnParticle(Particle.SPELL_WITCH, player.getLocation(), 100, 1, 0.2, 1, 0);
                    }, 2L);
                }
            }
            else {
                new ScytheThrowDisplay(player, event.getItem());
            }
        }
    }
}
