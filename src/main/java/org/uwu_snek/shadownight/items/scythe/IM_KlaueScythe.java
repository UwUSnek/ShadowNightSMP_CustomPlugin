package org.uwu_snek.shadownight.items.scythe;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.CustomItemId;
import org.uwu_snek.shadownight.utils.blockdata.BlockProperty;
import org.uwu_snek.shadownight.utils.spigot.PlayerUtils;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public final class IM_KlaueScythe extends IM_Scythe {
    public IM_KlaueScythe(){
        super(
            "§6Edgy Scythe",
            CustomItemId.KLAUE_SCYTHE,
            18,
            1
        );
    }


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
