package org.shadownight.plugin.shadownight.items;

import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.shadownight.plugin.shadownight.utils.UtilityClass;
import org.shadownight.plugin.shadownight.utils.math.Func;
import org.shadownight.plugin.shadownight.utils.spigot.ItemUtils;
import org.shadownight.plugin.shadownight.utils.spigot.Scheduler;
import org.shadownight.plugin.shadownight.utils.utils;

import java.util.*;
import java.util.logging.Level;




/**
 * This class is used to manage player attacks because Bukkit's system is atrocious.
 * Allows for custom attacks and overrides vanilla attacks.
 * Provides actually useful data for the death message manager.
 */
public final class AttackOverride extends UtilityClass {
    private static final Map<Material, Double> vanillaDamage = Map.ofEntries(
        new AbstractMap.SimpleEntry<>(Material.WOODEN_SWORD,      4d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_SWORD,      4d),
        new AbstractMap.SimpleEntry<>(Material.STONE_SWORD,       5d),
        new AbstractMap.SimpleEntry<>(Material.IRON_SWORD,        6d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_SWORD,     7d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_SWORD,   8d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_AXE,        7d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_AXE,        7d),
        new AbstractMap.SimpleEntry<>(Material.STONE_AXE,         9d),
        new AbstractMap.SimpleEntry<>(Material.IRON_AXE,          9d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_AXE,       9d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_AXE,     10d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_PICKAXE,    2d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_PICKAXE,    2d),
        new AbstractMap.SimpleEntry<>(Material.STONE_PICKAXE,     3d),
        new AbstractMap.SimpleEntry<>(Material.IRON_PICKAXE,      4d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_PICKAXE,   5d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_PICKAXE, 6d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_SHOVEL,     2.5d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_SHOVEL,     2.5d),
        new AbstractMap.SimpleEntry<>(Material.STONE_SHOVEL,      3.5d),
        new AbstractMap.SimpleEntry<>(Material.IRON_SHOVEL,       4.5d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_SHOVEL,    5.5d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_SHOVEL,  6.5d),

        new AbstractMap.SimpleEntry<>(Material.WOODEN_HOE,        1d),
        new AbstractMap.SimpleEntry<>(Material.GOLDEN_HOE,        1d),
        new AbstractMap.SimpleEntry<>(Material.STONE_HOE,         1d),
        new AbstractMap.SimpleEntry<>(Material.IRON_HOE,          1d),
        new AbstractMap.SimpleEntry<>(Material.DIAMOND_HOE,       1d),
        new AbstractMap.SimpleEntry<>(Material.NETHERITE_HOE,     1d),

        new AbstractMap.SimpleEntry<>(Material.TRIDENT,           9d)
    );


    public static class AttackData {
        public final LivingEntity damager;
        public final ItemStack usedItem;
        public final Long time;

        // This is used to determine if an event has to actually damage the entity and trigger vanilla mob behaviours.
        // Set to true after the mirror event is fired (event got mirrored, no need to do that again).
        public boolean mirrored = false;

        public AttackData(LivingEntity _damager, ItemStack _usedItem, Long _time) {
            damager = _damager;
            usedItem = _usedItem;
            time = _time;
        }
    }


    public  static final HashMap<UUID, CircularFifoQueue<AttackData>> attacks = new HashMap<>();
    private static final HashMap<UUID, Boolean> knockbackSprintBuff = new HashMap<>();
    private static final double customDamageValue = 1.0E-4;





    public static void resetKnockbackSprintBuff(@NotNull final PlayerToggleSprintEvent event){
        if(event.isSprinting()) knockbackSprintBuff.put(event.getPlayer().getUniqueId(), true);
    }

    private static double getBaseKnockbackMultiplier(@Nullable final ItemStack item, @NotNull LivingEntity target) {
        double base = 1;
        if(item != null && item.getType() != Material.AIR) {
            final Long itemId = ItemUtils.getCustomItemId(item);
            if(itemId != null) base *= ItemManager.getValueFromId(itemId).getHitKnockbackMultiplier();
        }
        return base;
    }

    private static @NotNull Vector getKnockback(@Nullable final ItemStack item, @NotNull final LivingEntity damager, @NotNull final LivingEntity target) {
        // Calculate base knockback
        Vector direction = damager.getLocation().getDirection().setY(0).normalize();
        Vector knockback = direction.clone().multiply(0.4 /* default kb is 0.4 length on XZ */).multiply(getBaseKnockbackMultiplier(item, target)).setY(0.325d);



        // Vanilla sprint mechanic
        int hasSprintbuff = 0;
        if(damager instanceof Player player && player.isSprinting()) {
            final Boolean sprintBuff = knockbackSprintBuff.get(player.getUniqueId());
            if(sprintBuff != null && sprintBuff) {
                knockbackSprintBuff.put(player.getUniqueId(), false);
                ++hasSprintbuff;
            }
        }
        // Calculate enchantments
        if(item != null && item.getType() != Material.AIR) {
            int knockbackLv = item.getEnchantmentLevel(Enchantment.KNOCKBACK);
            knockback.add(new Vector(0.3f, 0, 0.3f).multiply(knockbackLv + hasSprintbuff).multiply(direction));
            if(knockbackLv > 0) knockback.setY(0.4f);
            //TODO calculate reeling enchant
        }



        // Calculate resistance
        final AttributeInstance attribute = target.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        double resistance = attribute == null ? 0 : attribute.getBaseValue(); // 0 to 100

        // Return effective knockback
        return knockback.multiply(1 - resistance);
    }




    private static double getBaseDamage(@Nullable final ItemStack item, @NotNull final LivingEntity damager) {
        // Get item base attack damage
        final Long itemId = ItemUtils.getCustomItemId(item);
        if(itemId != null) return ItemManager.getValueFromId(itemId).getHitDamage();                    // If item is a custom item, get the base damage from its item manager
        else if(item != null && item.getType() != Material.AIR) {                                       // If item is a vanilla item
            final Double _vanillaDamage = vanillaDamage.get(item.getType());                                // If the item has a vanilla hard coded damage value, use that
            return _vanillaDamage == null ? 1d : _vanillaDamage;                                            // If not, return the default 1 damage
        }
        else {                                                                                          // If no item was used (most mobs & players punching)
            final AttributeInstance attribute = damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);      // If the entity can attack (has an attack damage attribute) use that
            return attribute == null ? 0 : attribute.getBaseValue();                                        // If not, return the default 0 damage
        }
    }

    private static double getDamage(@Nullable final ItemStack item, boolean canCrit, @NotNull final LivingEntity damager, @NotNull final LivingEntity target) {
        double damage = getBaseDamage(item, damager);
        double enchantDamage = 0d;
        double charge = 1;
        if(damager instanceof Player player) charge = player.getAttackCooldown();


        // Calculate potion effects
        PotionEffect str = damager.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
        if(str != null) damage += 3 * str.getAmplifier();


        // Calculate critical multiplier //TODO calculate critical enchant
        if(canCrit && isCritical(damager)) {
            damage *= 1.5;
            //damager.getWorld().spawnParticle(Particle.CRIT, target.getLocation(), 10);
        }


        // Calculate enchantments (Separate charge multiplier)
        if(item != null && item.getType() != Material.AIR) {
            for(Map.Entry<Enchantment, Integer> e : item.getEnchantments().entrySet()) {
                Enchantment key = e.getKey();
                if(key == Enchantment.DAMAGE_ALL)        enchantDamage += 0.5 + e.getValue() * 0.5;
                if(key == Enchantment.DAMAGE_ARTHROPODS) if(target.getCategory() == EntityCategory.ARTHROPOD) enchantDamage += 2.5 * e.getValue();
                if(key == Enchantment.DAMAGE_UNDEAD)     if(target.getCategory() == EntityCategory.UNDEAD)    enchantDamage += 2.5 * e.getValue();
                if(key == Enchantment.IMPALING)          if(target.getCategory() == EntityCategory.WATER)     enchantDamage += 2.5 * e.getValue();
            }
        }


        // Return effective damage
        return
            damage *        (0.2 + Math.pow(charge, 2) * 0.8) +
            enchantDamage * (0.2 +          charge     * 0.8)
        ;
    }




    /**
     * Checks if an entity can deal critical hits in its current state.
     * @param damager The damager entity
     * @return True if it can deal critical hits, false otherwise
     */
    private static boolean isCritical(@NotNull final LivingEntity damager) {
        return
            damager.getFallDistance() > 0f &&
            !damager.isOnGround() &&
            !damager.isClimbing() &&
            !damager.isInWater() &&
            damager.getVehicle() == null &&
            damager.getPotionEffect(PotionEffectType.BLINDNESS) == null &&
            !(damager instanceof Player player && player.isSprinting())
        ;
    }


    /**
     * Catches custom attack damage events and sets the invincibility ticks to 0 to allow the damage helper event to trigger.
     * @param e The event to check
     */
    public static void cancelInvincibility(@NotNull final EntityDamageEvent e) {
        //if(e instanceof LivingEntity target && e.getCause() == EntityDamageEvent.DamageCause.CUSTOM) target.setMaximumNoDamageTicks(0);

    }

    /**
     * Replaces a Vanilla EntityDamageByEntityEvent event with a custom attack.
     * @param e The event to replace
     * @param canCrit True if the attack can crit, false otherwise.
     *                Whether the attack will effectively be critical depends on the current status of the attacking entity
     */
    public static void customAttack(@NotNull final EntityDamageByEntityEvent e, final boolean canCrit) {
        //if(e.getDamage() == 0d) {utils.log(Level.WARNING, "helper detected"); if(e.getEntity() instanceof LivingEntity target) target.setMaximumNoDamageTicks(0); return; }                             // Let helper events through to restore mob behaviour
        CircularFifoQueue<AttackData> attackQueue = attacks.get(e.getEntity().getUniqueId());
        if(attackQueue != null) {
            AttackData lastAttack = attackQueue.get(0);
            if(lastAttack.damager == e.getDamager() && !lastAttack.mirrored) {
                utils.log(Level.SEVERE, "Letting damage event through: " + e.getDamage());
                lastAttack.mirrored = true;
                return;
            }
        }

        if(Func.doubleEquals(e.getDamage(), customDamageValue, customDamageValue / 4)) return;                             // Let helper events through to restore mob behaviour
        if(e.getDamager().getType() == EntityType.CREEPER) return;  // Use Vanilla creeper explosions

        // Cancel event and compute custom attack
        if (e.getDamager() instanceof LivingEntity damager && e.getEntity() instanceof LivingEntity target) {
            utils.log(Level.WARNING, "player hit detected: " + e.getDamage());
            e.setCancelled(true);
            customAttack(damager, target, canCrit);
        }
    }

    /**
     * Registers a custom attack.
     * This method is also used to replace Vanilla EntityDamageByEntityEvent events.
     * @param damager The attacking entity
     * @param target The damaged entity
     * @param canCrit True if the attack can crit, false otherwise.
     *                Whether the attack will effectively be critical depends on the current status of the attacking entity
     */
    public static void customAttack(@NotNull final LivingEntity damager, @NotNull final LivingEntity target, final boolean canCrit) {
        // Cancel the vanilla event and save the used item
        final EntityEquipment equipment = damager.getEquipment();
        final ItemStack item = equipment == null ? null : equipment.getItemInMainHand();

        // Save the attack data
        final UUID targetId = target.getUniqueId();
        attacks.putIfAbsent(targetId, new CircularFifoQueue<>(2));
        attacks.get(targetId).add(new AttackData(
            damager,
            item,
            System.currentTimeMillis()
        ));




        // Damage the target
        final double damage = getDamage(item, canCrit, damager, target);
        //target.damage(damage);          // Apply damage
        //target.setHealth(Func.clampMin(target.getHealth() - damage, 0));
        //Scheduler.delay(() -> target.damage(0d, damager), 20L);     // Fix mob behaviour
        {
            //target.damage(customDamageValue, damager);     // Fix mob behaviour
            //target.damage(Double.MIN_NORMAL, new DamageSource() {
            //    @NotNull @Override public DamageType getDamageType() { return DamageType.GENERIC; }
            //    @Nullable @Override public Entity getCausingEntity() { return damager; }
            //    @Nullable @Override public Entity getDirectEntity() { return damager; }
            //    @Nullable @Override public Location getDamageLocation() { return null; }
            //    @Nullable @Override public Location getSourceLocation() { return null; }
            //    @Override public boolean isIndirect() { return false; }
            //    @Override public float getFoodExhaustion() { return 0; }
            //    @Override public boolean scalesWithDifficulty() { return false; }
            //});     // Fix mob behaviour
            Scheduler.delay(() -> target.damage(damage, damager), 1L);
                //@NotNull @Override public DamageType getDamageType() { return DamageType.GENERIC; }
                //@Nullable @Override public Entity getCausingEntity() { return damager; }
                //@Nullable @Override public Entity getDirectEntity() { return damager; }
                //@Nullable @Override public Location getDamageLocation() { return null; }
                //@Nullable @Override public Location getSourceLocation() { return null; }
                //@Override public boolean isIndirect() { return false; }
                //@Override public float getFoodExhaustion() { return 0; }
                //@Override public boolean scalesWithDifficulty() { return false; }
            //});     // Fix mob behaviour
            utils.log(Level.SEVERE, "Custom damage sent: " + damage);
        }
        Bukkit.broadcastMessage("Damaged for " + damage);
        //TODO review and simplify custom scythe attacks


        // Fix mob behaviour
        /*
        if(target != damager) {
            if (target instanceof Tameable tameable) { // Don't attack owner
                AnimalTamer owner = tameable.getOwner();
                if (owner != damager) tameable.setTarget(damager);
                else if (tameable instanceof Wolf && owner instanceof Player player) angerWolves(player, target); // Make other wolves in the group of the target attack the damager
            }
            else if(target instanceof Golem golem) if(golem.)
            else if (target instanceof Mob mob) mob.setTarget(damager);
            else if (damager instanceof Player player) angerWolves(player, target); // Make wolves tamed by the damager attack the target
        }
        */


        // Knockback the target
        final Vector velocity = getKnockback(item, damager, target);
        target.setVelocity(target.getVelocity().add(velocity));
        Bukkit.broadcastMessage("knockbacked for " + velocity.length());

        target.setVelocity(velocity);
    }


    private static void angerWolves(@NotNull final Player owner, @NotNull final LivingEntity target) {
        if(
            (target instanceof Tameable tameable && tameable.isTamed()) ||
            target.getType() == EntityType.CREEPER ||
            target.getType() == EntityType.GHAST
        ) return;

        for(Entity _wolf : owner.getWorld().getEntitiesByClasses(Wolf.class)) {
            Wolf wolf = (Wolf)_wolf;
            if(wolf.isTamed() && !wolf.isSitting() && wolf.getOwner() == owner) wolf.setTarget(target);
        }
    }
}
