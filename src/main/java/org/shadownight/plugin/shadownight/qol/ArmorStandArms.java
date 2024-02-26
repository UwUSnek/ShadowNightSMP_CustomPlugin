package org.shadownight.plugin.shadownight.qol;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.jetbrains.annotations.NotNull;
import org.shadownight.plugin.shadownight.utils.UtilityClass;




public final class ArmorStandArms extends UtilityClass {
    public static void onSpawn(@NotNull final EntitySpawnEvent event) {
        if(event.getEntity() instanceof ArmorStand entity) entity.setArms(true);
    }
}
