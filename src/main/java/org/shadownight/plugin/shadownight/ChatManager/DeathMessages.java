package org.shadownight.plugin.shadownight.ChatManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;

import static org.bukkit.event.entity.EntityDamageEvent.DamageCause;


public class DeathMessages {
    public static class DeathData {
        final String icon;
        final String[] variants;

        public DeathData(String _icon, String[] _variants){
            icon = _icon;
            variants = _variants;
        }
    }



    static final Map<EntityDamageEvent.DamageCause, DeathData> environmentData = Map.ofEntries(
        // Passive-only
        new AbstractMap.SimpleEntry<>(DamageCause.CONTACT,                new DeathData("§l💥", new String[]{ "was poked to death", "was pricked to death" })),
        new AbstractMap.SimpleEntry<>(DamageCause.CRAMMING,               new DeathData("☠",   new String[]{ "was squished too much" })),
        new AbstractMap.SimpleEntry<>(DamageCause.DROWNING,               new DeathData("☠",   new String[]{ "drowned", "forgot to breathe" })),
        new AbstractMap.SimpleEntry<>(DamageCause.FALL,                   new DeathData("§l💥", new String[]{ "was doomed to fall", "fell from a high place", "couldn't fly", "hit the ground too hard", "fell to their death" })),
        new AbstractMap.SimpleEntry<>(DamageCause.FALLING_BLOCK,          new DeathData("§l💥", new String[]{ "was squashed", "was crushed" })),
        new AbstractMap.SimpleEntry<>(DamageCause.FIRE,                   new DeathData("🔥",   new String[]{ "went up in flames", "stared at the sun for too long" })),
        new AbstractMap.SimpleEntry<>(DamageCause.FIRE_TICK,              new DeathData("🔥",   new String[]{ "burned to death", "couldn't find water on time", "burned to a crisp" })),
        new AbstractMap.SimpleEntry<>(DamageCause.FLY_INTO_WALL,          new DeathData("§l💥", new String[]{ "experienced kinetic energy", "flew into a wall" , "crashed into a wall" })),
        new AbstractMap.SimpleEntry<>(DamageCause.FREEZE,                 new DeathData("§l❄", new String[]{ "froze to death" })),
        new AbstractMap.SimpleEntry<>(DamageCause.HOT_FLOOR,              new DeathData("🔥",   new String[]{ "discovered the floor was lava" })),
        new AbstractMap.SimpleEntry<>(DamageCause.LAVA,                   new DeathData("🔥",   new String[]{ "tried to swim in lava" })),
        new AbstractMap.SimpleEntry<>(DamageCause.LIGHTNING,              new DeathData("§l⚡", new String[]{ "was struck by lightning", "angered Zeus" })),
        new AbstractMap.SimpleEntry<>(DamageCause.WITHER,                 new DeathData("§l✨", new String[]{ "withered away" })),
        new AbstractMap.SimpleEntry<>(DamageCause.SONIC_BOOM,             new DeathData("§l✨", new String[]{ "was obliterated by a sonically-charged shriek", "was shred to pieces by a sonically-charged shriek" })),
        new AbstractMap.SimpleEntry<>(DamageCause.STARVATION,             new DeathData("☠",   new String[]{ "starved to death", "forgot to eat" })),
        new AbstractMap.SimpleEntry<>(DamageCause.SUFFOCATION,            new DeathData("☠",   new String[]{ "suffocated to death", "suffocated in a wall" })),
        new AbstractMap.SimpleEntry<>(DamageCause.WORLD_BORDER,           new DeathData("☠",   new String[]{ "left the confines of this world" })),
        new AbstractMap.SimpleEntry<>(DamageCause.BLOCK_EXPLOSION,        new DeathData("§l💥", new String[]{ "blew up", "exploded", "went off with a bang", "was blown to bits" })),
        new AbstractMap.SimpleEntry<>(DamageCause.MAGIC,                  new DeathData("§l✨", new String[]{ "was killed by magic" })),
        new AbstractMap.SimpleEntry<>(DamageCause.VOID,                   new DeathData("☠",   new String[]{ "fell out of the world" }))
                                                                                              );



    static final Map<EntityDamageEvent.DamageCause, DeathData> environmentAssistData = Map.ofEntries(
        // Passive-only
        //new AbstractMap.SimpleEntry<>(DamageCause.CONTACT,                new DeathData("§l💥", new String[]{ "was poked to death", "was pricked to death" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.CRAMMING,               new DeathData("☠",   new String[]{ "was squished too much" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.DROWNING,               new DeathData("☠",   new String[]{ "drowned", "forgot to breathe" })),
        new AbstractMap.SimpleEntry<>(DamageCause.FALL,                   new DeathData("§l💥", new String[]{ "was doomed to fall by %killer%", "was knockbacked off a cliff by %killer%",  "fell to their death trying to escape %killer%" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.FALLING_BLOCK,          new DeathData("§l💥", new String[]{ "was squashed", "was crushed" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.FIRE,                   new DeathData("🔥",   new String[]{ "went up in flames", "stared at the sun for too long" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.FIRE_TICK,              new DeathData("🔥",   new String[]{ "burned to death", "couldn't find water on time", "burned to a crisp" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.FLY_INTO_WALL,          new DeathData("§l💥", new String[]{ "experienced kinetic energy", "flew into a wall" , "crashed into a wall" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.FREEZE,                 new DeathData("§l❄", new String[]{ "froze to death" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.HOT_FLOOR,              new DeathData("🔥",   new String[]{ "discovered the floor was lava" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.LAVA,                   new DeathData("🔥",   new String[]{ "tried to swim in lava" })),
        new AbstractMap.SimpleEntry<>(DamageCause.LIGHTNING,              new DeathData("§l⚡", new String[]{ "died from a lightning summoned by %killer%", "was struck by a lightning summoned by %killer%" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.WITHER,                 new DeathData("§l✨", new String[]{ "withered away" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.SONIC_BOOM,             new DeathData("§l✨", new String[]{ "was obliterated by a sonically-charged shriek", "was shred to pieces by a sonically-charged shriek" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.STARVATION,             new DeathData("☠",   new String[]{ "starved to death", "forgot to eat" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.SUFFOCATION,            new DeathData("☠",   new String[]{ "suffocated to death", "suffocated in a wall" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.WORLD_BORDER,           new DeathData("☠",   new String[]{ "left the confines of this world" })),
        //new AbstractMap.SimpleEntry<>(DamageCause.BLOCK_EXPLOSION,        new DeathData("§l💥", new String[]{ "blew up", "exploded", "went off with a bang" })),
        new AbstractMap.SimpleEntry<>(DamageCause.MAGIC,                  new DeathData("§l✨", new String[]{ "lost a magic duel with %killer%", "couldn't counter %killer%'s spells", "couldn't dodge %killer%'s potions" })),
        new AbstractMap.SimpleEntry<>(DamageCause.VOID,                   new DeathData("☠",   new String[]{ "didn't want to live in the same world as %killer%", "was thrown out of this world by %killer%", "was kicked out of this world by %killer%", "jumped into the void to escape %killer%" }))
                                                                                                    );



    static final Map<EntityDamageEvent.DamageCause, DeathData> activeData = Map.ofEntries(
        new AbstractMap.SimpleEntry<>(DamageCause.ENTITY_EXPLOSION,       new DeathData("§l💥", new String[]{ "was blown up by %killer%", "was disintegrated by %killer%", "was blown to bits by %killer%" })),
        new AbstractMap.SimpleEntry<>(DamageCause.ENTITY_SWEEP_ATTACK,    new DeathData("§l⚔", new String[]{ "was accidentally killed by %killer%" })),
        new AbstractMap.SimpleEntry<>(DamageCause.ENTITY_ATTACK,          new DeathData("§l⚔", new String[]{ "was killed by %killer%", "was slain by %killer%", "was destroyed by %killer%" })),
        new AbstractMap.SimpleEntry<>(DamageCause.THORNS,                 new DeathData("§l⚔", new String[]{ "was killed while trying to hurt %killer%", "underestimated %killer%'s thorns armor" })),

        // !!! Projectiles are handled separately, but the data is needed for icon and simpler code logic
        new AbstractMap.SimpleEntry<>(DamageCause.PROJECTILE,             new DeathData("§l🏹", new String[]{ "" }))
                                                                                         );



    static final Map<EntityType, String[]> projectileData = Map.ofEntries(
        new AbstractMap.SimpleEntry<>(EntityType.ARROW,             new String[]{ "was shot by %killer%", "was sniped by %killer%", "underestimated %killer%'s aim", "couldn't dodge %killer%'s arrows" }),
        new AbstractMap.SimpleEntry<>(EntityType.SPECTRAL_ARROW,    new String[]{ "was shot by %killer%", "was sniped by %killer%", "underestimated %killer%'s aim", "couldn't dodge %killer%'s spectral arrows" }),
        new AbstractMap.SimpleEntry<>(EntityType.TRIDENT,           new String[]{ "was impaled by %killer%", "couldn't dodge %killer%'s trident" }),
        new AbstractMap.SimpleEntry<>(EntityType.FIREBALL,          new String[]{ "was fireballed to death by %killer%", "couldn't dodge %killer%'s fireballs" }),
        new AbstractMap.SimpleEntry<>(EntityType.SMALL_FIREBALL,    new String[]{ "was fireballed to death by %killer%", "couldn't dodge %killer%'s fireballs" }),
        new AbstractMap.SimpleEntry<>(EntityType.WITHER_SKULL,      new String[]{ "was killed by a skull shot by %killer%", "couldn't dodge %killer%'s skulls", "forgor" }),
        new AbstractMap.SimpleEntry<>(EntityType.SHULKER_BULLET,    new String[]{ "was shot by %killer%", "was sniped by %killer%", "couldn't dodge %killer%'s bullets"  }),
        new AbstractMap.SimpleEntry<>(EntityType.LLAMA_SPIT,        new String[]{ "was spit to death by %killer%", "suffocated in %killer%'s spit" }),
        new AbstractMap.SimpleEntry<>(EntityType.EVOKER_FANGS,      new String[]{ "was chopped to pieces by %killer%'s fang attack", "was ripped in half by %killer%'s fang attack" })
        //new AbstractMap.SimpleEntry<>(EntityType.SNOWBALL,          new String[]{ "" }),  // This is not possible
        //new AbstractMap.SimpleEntry<>(EntityType.EGG,               new String[]{ "" }),  // This is not possible
        //new AbstractMap.SimpleEntry<>(EntityType.ENDER_PEARL,       new String[]{ "" }),  // This is not possible
        //new AbstractMap.SimpleEntry<>(EntityType.DRAGON_FIREBALL,   new String[]{ "" }),  // This is not possible
                                                                         );

    static final String[] playerSpecificVariants = {"lost a fight with %killer%", "was rejected by %killer%", "couldn't escape %killer%", "ended their friendship with %killer%", "skill issued during a fight with %killer%", "was killed by lag while fighting %killer%", "was slaughtered by %killer%" };

    static final String[] defaultAssistModifiers = {"trying to escape %killer%", "while fighting %killer%", "during a fight with %killer%" };

    //new AbstractMap.SimpleEntry<>(DamageCause.CUSTOM,                 new DeathData("", new String[]{ "" })),     // Custom damage
    //new AbstractMap.SimpleEntry<>(DamageCause.DRAGON_BREATH,          new DeathData("", new String[]{ "" })),     // This cannot happen naturally
    //new AbstractMap.SimpleEntry<>(DamageCause.KILL,                   new DeathData("", new String[]{ "" })),     // Kill command
    //new AbstractMap.SimpleEntry<>(DamageCause.DRYOUT,                 ＿＿＿＿＿＿＿⯅⯅＿＿⧋＿＿＿⯅⯅▅﹏﹏█﹏﹏▅＿＿＿＿⯅＿◀▀＿＿⯅＿＿＿＿⧋＿＿⧋﹏▅﹏﹏█
    //new AbstractMap.SimpleEntry<>(DamageCause.MELTING,                new DeathData("", new String[]{ "" })),     // Reserved for snowmen dying from high temperature
    //new AbstractMap.SimpleEntry<>(DamageCause.SUICIDE,                new DeathData("", new String[]{ "" })),     // Idk what this is
    //new AbstractMap.SimpleEntry<>(DamageCause.POISON,                 new DeathData("", new String[]{ "" })),     // Cant die from this



    static final Random rnd = new Random();



    static public void formatDeathMessage(PlayerDeathEvent event){
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();
        EntityDamageEvent.DamageCause cause = player.getLastDamageCause().getCause();

        String a = "§8[§4";
        String b = "§8] §r§7";
        String m = "§l⚔§4";
        String msg;


        // If death was an active action
        if(cause == DamageCause.ENTITY_SWEEP_ATTACK || cause == DamageCause.ENTITY_ATTACK || cause == DamageCause.PROJECTILE || cause == DamageCause.THORNS || cause == DamageCause.ENTITY_EXPLOSION){
            DeathData data = activeData.get(cause);
            if(data == null) {
                event.setDeathMessage(null);
                return;
            }
            String killerName;
            String causeMessage; // Initialize to "" to avoid "might not be initialized" error. Some projectiles cannot deal damage


            // If killer was a player
            if(cause != DamageCause.PROJECTILE && killer != null) {
                causeMessage = rnd.nextBoolean() ? data.variants[rnd.nextInt(data.variants.length)] : playerSpecificVariants[rnd.nextInt(playerSpecificVariants.length)];
                killerName = killer.getName();
            }
            // If killer was not a player
            else {
                EntityDamageByEntityEvent entityEvent = (EntityDamageByEntityEvent) player.getLastDamageCause();
                Entity damager = entityEvent.getDamager();

                // If player was shot by a projectile, use custom message
                if(cause == DamageCause.PROJECTILE ){
                    String[] projectileVariants = projectileData.get(damager.getType());
                    causeMessage = projectileVariants[rnd.nextInt(projectileVariants.length)];
                    killerName = ((Entity) ((Projectile) damager).getShooter()).getName();
                }
                // If not, use normal specific message
                else {
                    causeMessage = data.variants[rnd.nextInt(data.variants.length)];
                    killerName = damager.getType() == EntityType.AREA_EFFECT_CLOUD ? "magic" : damager.getName() ;
                }
            }

            msg = (data.icon + b + player.getName() + "§r§7 " + causeMessage).replace("%killer%", killerName);
        }



        // If death was from the environment
        else {
            DeathData data = environmentData.get(cause);
            if(data == null) {
                event.setDeathMessage(null);
                return;
            }
            msg = data.icon + b + player.getName() + "§r§7 ";


            // If a player helped with the kill
            if(killer != null) {
                DeathData customMessages = environmentAssistData.get(cause);
                msg = (
                    m + msg +
                    (
                        customMessages == null
                            ? data.variants[rnd.nextInt(data.variants.length)] + " " + defaultAssistModifiers[rnd.nextInt(defaultAssistModifiers.length)]
                            : customMessages.variants[rnd.nextInt(customMessages.variants.length)]
                    )
                ).replace("%killer%", killer.getName());
            }
            // If not
            else {
                msg += data.variants[rnd.nextInt(data.variants.length)];
            }
        }



        event.setDeathMessage(null);
        String output = a + msg + ".";
        Bukkit.broadcastMessage(output);
        DiscordBotManager.sendBridgeMessage(output);
    }
}



