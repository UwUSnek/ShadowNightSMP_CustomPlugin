package org.shadownight.plugin.shadownight.items.scythe;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.items.CustomItemId;


public final class IM_KlaueScythe extends IM_Scythe {
    @Override public Material     getMaterial()        { return Material.NETHERITE_SWORD;  }
    @Override public String       getDisplayName()     { return "Edgy Scythe";             }
    @Override public int          getCustomModelData() { return 14;                        }
    @Override public CustomItemId getCustomId()        { return CustomItemId.KLAUE_SCYTHE; }

    @Override protected double       getAttackSpeed()     { return -3.0;                      }
    @Override protected double       getDamage()          { return 14;                        }


    @Override
    protected void setRecipe(@NotNull final ShapedRecipe recipe) {
        recipe.shape("III", "  S", " S ");
        recipe.setIngredient('I', Material.COMMAND_BLOCK);
        recipe.setIngredient('S', Material.COMMAND_BLOCK);
    }








    @Override
    protected void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            event.setCancelled(true);
            if(player.isSneaking()) player.sendMessage("shadow fury knock off");
            else customAttack(player, event.getItem());
        }
        else if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            event.setCancelled(true);
            if(player.isSneaking()) {
                Block targetBlock = player.getTargetBlockExact(1000, FluidCollisionMode.NEVER);
                if(targetBlock != null) {
                    Location targetBlockLocation = targetBlock.getLocation();
                    Location playerLocation = player.getLocation();
                    // Teleport with delay. 0 delay causes the event to be fired twice
                    Bukkit.getScheduler().runTaskLater(ShadowNight.plugin, () -> {
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




    @Override
    protected void onAttack(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();

        if(player.isSneaking()) {
            event.setCancelled(true);
            player.sendMessage("shadow fury knock off");
        }
        else super.onAttack(event);
    }
}
