package org.shadownight.plugin.shadownight.items;

import com.google.common.collect.HashMultimap;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;
import org.shadownight.plugin.shadownight.ShadowNight;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class Scythe {
    private static final int SCYTHE_DATA = 1;
    public static final ItemStack ironItem      = utils.createItemStackCustom(Material.IRON_SWORD, 1, "Iron Scythe", SCYTHE_DATA, ItemManager.CustomItemId.IRON_SCYTHE);
    public static final ItemStack diamondItem   = utils.createItemStackCustom(Material.DIAMOND_SWORD, 1, "Diamond Scythe", SCYTHE_DATA, ItemManager.CustomItemId.DIAMOND_SCYTHE);
    public static final ItemStack netheriteItem = utils.createItemStackCustom(Material.NETHERITE_SWORD, 1, "Netherite Scythe", SCYTHE_DATA, ItemManager.CustomItemId.NETHERITE_SCYTHE);
    public static final ItemStack klaueItem     = utils.createItemStackCustom(Material.NETHERITE_SWORD, 1, "Klaue's Edgy Scythe", 14, ItemManager.CustomItemId.KLAUE_SCYTHE);






    private static void setAttributes(ItemStack item, double speed, double damage) {
        ItemMeta meta = item.getItemMeta();
        Objects.requireNonNull(meta, "Object meta is null");
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,  new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed",  speed,  AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", damage, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND));
        item.setItemMeta(meta);
    }


    public static void createRecipes(){
        setAttributes(ironItem,      -3.4, 10); // 0.6
        setAttributes(diamondItem,   -3.2, 12); // 0.8
        setAttributes(netheriteItem, -3.0, 14); // 1.0
        setAttributes(klaueItem,     -3.0, 14); // 1.0



        NamespacedKey ironKey = new NamespacedKey(ShadowNight.plugin, "ironScythe");
        ShapedRecipe ironRecipe = new ShapedRecipe(ironKey, ironItem);

        ironRecipe.shape("III", "  S", " S ");
        ironRecipe.setIngredient('I', Material.IRON_INGOT);
        //ironRecipe.setIngredient('S', Material.STICK);
        ironRecipe.setIngredient('S', Material.COMMAND_BLOCK);
        Bukkit.addRecipe(ironRecipe); //TODO make this a rare drop instead of a recipe




        NamespacedKey diamondKey = new NamespacedKey(ShadowNight.plugin, "diamondScythe");
        ShapedRecipe diamondRecipe = new ShapedRecipe(diamondKey, diamondItem);

        diamondRecipe.shape("DDD", "  S", " S ");
        diamondRecipe.setIngredient('D', Material.DIAMOND);
        //diamondRecipe.setIngredient('S', Material.STICK);
        diamondRecipe.setIngredient('S', Material.COMMAND_BLOCK);
        Bukkit.addRecipe(diamondRecipe); //TODO make this a rare drop instead of a recipe




        NamespacedKey klaueKey = new NamespacedKey(ShadowNight.plugin, "klaueScythe");
        ShapedRecipe klaueRecipe = new ShapedRecipe(klaueKey, klaueItem);

        klaueRecipe.shape("DDD", "  S", " S ");
        klaueRecipe.setIngredient('D', Material.COMMAND_BLOCK);
        klaueRecipe.setIngredient('S', Material.COMMAND_BLOCK);
        Bukkit.addRecipe(klaueRecipe);




        NamespacedKey netheriteKey = new NamespacedKey(ShadowNight.plugin, "netheriteScythe");
        SmithingTransformRecipe netheriteRecipe = new SmithingTransformRecipe(
            netheriteKey,
            netheriteItem,
            new RecipeChoice.ExactChoice(new ItemStack(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1)),
            new RecipeChoice.ExactChoice(diamondItem),
            new RecipeChoice.ExactChoice(netheriteItem)
        );
        Bukkit.addRecipe(netheriteRecipe);
    }





    public static final HashMultimap<UUID, UUID> attackQueue = HashMultimap.create();
    private static final HashMultimap<UUID, UUID> ongoingAttacks = HashMultimap.create();

    static private void customAttack(Player player) {
        int attackRange = 6;
        Location playerPos = player.getLocation();
        Vector playerDirection = playerPos.getDirection();
        List<Entity> entities = player.getNearbyEntities(attackRange, attackRange, attackRange);

        double cooldown = player.getAttackCooldown();
        double damage = cooldown * Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE), "Attack damage attribute is null").getValue();

        for (Entity e : entities) {
            if (
                e instanceof LivingEntity &&
                playerPos.distance(e.getLocation()) < attackRange &&
                utils.isInCone(playerPos.toVector(), playerDirection, e.getLocation().toVector(), 3)
            ) {
                attackQueue.put(player.getUniqueId(), e.getUniqueId());
                ((LivingEntity) e).damage(damage, player);
                e.setVelocity(e.getVelocity().add(playerDirection.clone().multiply(new Vector(1, 0, 1)).multiply(cooldown))); // Double the normal kb (Damaging e already gives it normal kb)
            }
        }


        attackQueue.removeAll(player.getUniqueId());
        ongoingAttacks.removeAll(player.getUniqueId());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
        player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, playerPos.clone().add(playerDirection.clone().multiply(new Vector(2, 0, 2))).add(new Vector(0, 1, 0)), 1, 0, 0, 0);
    }



    static public void onInteractNormal(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(!player.isSneaking() && ((event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR))) {
            customAttack(player);
        }
    }

    static public void onAttack(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        Entity target = event.getEntity();

        UUID playerId = player.getUniqueId();
        UUID targetId = target.getUniqueId();


        if(attackQueue.containsEntry(playerId, targetId)) { //TODO remove if present
            attackQueue.remove(playerId, targetId);
            ongoingAttacks.put(playerId, targetId);
        }
        else if(ongoingAttacks.containsEntry(playerId, targetId)) {
            event.setCancelled(true);
        }
        else {
            event.setCancelled(true);
            customAttack(player);
        }
    }




    static public void onInteractKlaue(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            if(player.isSneaking()) Bukkit.broadcastMessage("shadow fury knock off");
        }
        else if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
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
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "execute at " + player.getName() + " run particle minecraft:witch ~ ~.1 ~ 1 0 1 0 100 force");
                    }, 2L);
                }
            }
            else {
                new ScytheThrowDisplay(player);
            }
        }
    }
}
