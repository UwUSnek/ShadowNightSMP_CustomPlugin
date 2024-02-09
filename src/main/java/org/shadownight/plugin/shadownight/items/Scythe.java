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

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class Scythe {
    private static final int SCYTHE_DATA = 1;
    public static final ItemStack ironItem      = utils.createItemStackCustom(Material.IRON_SWORD, 1, "Iron Scythe", SCYTHE_DATA, ItemManager.CustomItemId.IRON_SCYTHE);
    public static final ItemStack diamondItem   = utils.createItemStackCustom(Material.DIAMOND_SWORD, 1, "Diamond Scythe", SCYTHE_DATA, ItemManager.CustomItemId.DIAMOND_SCYTHE);
    public static final ItemStack netheriteItem = utils.createItemStackCustom(Material.NETHERITE_SWORD, 1, "Netherite Scythe", SCYTHE_DATA, ItemManager.CustomItemId.NETHERITE_SCYTHE);
    public static final ItemStack klaueItem     = utils.createItemStackCustom(Material.NETHERITE_SWORD, 1, "Klaue's Edgy Scythe", 14, ItemManager.CustomItemId.KLAUE_SCYTHE);

    private static final int attackRange = 6;





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






    static private void breakBlocks(Player player) {
        Location playerPos = player.getLocation();
        Vector playerDirection = playerPos.getDirection();

        player.sendMessage("debug: used rclick ability");
        //for()
    }





    public static final HashMultimap<UUID, UUID> attackQueue = HashMultimap.create();
    private static final HashMap<UUID, Long> last_times = new HashMap<>();
    private static final long cooldown = 500;

    static private void customAttack(Player player, ItemStack item) {
        Location playerPos = player.getLocation();
        UUID playerId = player.getUniqueId();
        long currentTime = System.currentTimeMillis();


        Long last_time = last_times.get(playerId);
        if(last_time == null || currentTime - last_time >= cooldown) {
            last_times.put(playerId, currentTime);
            Vector playerDirection = playerPos.getDirection();
            List<Entity> entities = player.getNearbyEntities(attackRange, attackRange, attackRange);

            double vanillaCooldown = player.getAttackCooldown();
            double damage = vanillaCooldown * Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE), "Attack damage attribute is null").getValue();

            int damagedEntities = 0;
            for (Entity e : entities) {
                if (
                    e instanceof LivingEntity &&
                        playerPos.distance(e.getLocation()) < attackRange &&
                        utils.isInCone(playerPos.toVector(), playerDirection, e.getLocation().toVector(), 3)
                ) {
                    ++damagedEntities;
                    attackQueue.put(playerId, e.getUniqueId());
                    ((LivingEntity) e).damage(damage, player);
                    attackQueue.remove(playerId, e.getUniqueId());
                    e.setVelocity(e.getVelocity().add(playerDirection.clone().multiply(new Vector(1, 0, 1)).multiply(vanillaCooldown))); // Double the normal kb (Damaging e already gives it normal kb)
                }
            }


            if (damagedEntities > 0) utils.damageItem(player, item);
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, playerPos.clone().add(playerDirection.clone().multiply(new Vector(2, 0, 2))).add(new Vector(0, 1, 0)), 1, 0, 0, 0);
        }
    }



    static public void onInteractNormal(PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) customAttack(event.getPlayer(), event.getItem());
        else if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            breakBlocks(event.getPlayer());
        }
    }



    static public void onAttackKlaue(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();

        if(player.isSneaking()) {
            player.sendMessage("shadow fury knock off");
            event.setCancelled(true);
        }
        else onAttack(event);
    }


    static public void onAttack(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        Entity target = event.getEntity();

        UUID playerId = player.getUniqueId();
        UUID targetId = target.getUniqueId();


        if(attackQueue.containsEntry(playerId, targetId)) {
            attackQueue.remove(playerId, targetId);
        }
        else {
            event.setCancelled(true);
            customAttack(player, player.getInventory().getItemInMainHand());
        }
    }




    static public void onInteractKlaue(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if(event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
            event.setCancelled(true);
            if(player.isSneaking()) player.sendMessage("shadow fury knock off");
            else customAttack(player, event.getItem());
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
