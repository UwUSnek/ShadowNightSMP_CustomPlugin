package org.uwu_snek.shadownight.qol;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.uwu_snek.shadownight.utils.UtilityClass;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;





public final class NoIFrames extends UtilityClass {
    public static void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof LivingEntity e) {
            Scheduler.delay(() -> e.setNoDamageTicks(0), 1);
        }
    }
}
