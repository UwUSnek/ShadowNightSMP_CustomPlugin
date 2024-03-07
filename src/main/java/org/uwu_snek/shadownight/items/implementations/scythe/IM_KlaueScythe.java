package org.uwu_snek.shadownight.items.implementations.scythe;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.items.Ability;
import org.uwu_snek.shadownight._generated._custom_item_id;
import org.uwu_snek.shadownight.utils.blockdata.BlockProperty;
import org.uwu_snek.shadownight.utils.spigot.PlayerUtils;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public final class IM_KlaueScythe extends IM_Scythe_Craftable {
    public IM_KlaueScythe(){
        super(
            "§6Edgy Scythe",
            _custom_item_id.KLAUE_SCYTHE,
            18,
            1
        );

        setAbilities(
            null,
            new Ability(true, 0d, (player, item) -> shadowFuryKnockoff(player)),
            new Ability(true, 0.5d, ScytheThrowDisplay::new),
            new Ability(true, 0d, (player, item) -> teleportPlayer(player))
        );
    }

    @Override
    protected Material getRecipeMaterial() {
        return Material.COMMAND_BLOCK;
    }

    @Override
    protected Material getRecipeBlock() {
        return Material.COMMAND_BLOCK;
    }


    private static void shadowFuryKnockoff(final @NotNull Player player) {
        player.sendMessage("shadow fury knock off");
    }

    private static void teleportPlayer(final @NotNull Player player) {
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
}
