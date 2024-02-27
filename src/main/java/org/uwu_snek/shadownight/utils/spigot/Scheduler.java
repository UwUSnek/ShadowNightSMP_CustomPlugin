package org.uwu_snek.shadownight.utils.spigot;


import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.uwu_snek.shadownight.ShadowNight;
import org.uwu_snek.shadownight.utils.UtilityClass;




@SuppressWarnings("unused")
public final class Scheduler extends UtilityClass {
    public static BukkitTask   run(@NotNull final Runnable runnable)                                        { return Bukkit.getScheduler().runTaskAsynchronously(ShadowNight.plugin, runnable); }
    public static BukkitTask  loop(@NotNull final Runnable runnable, final long delay, final long duration) { return Bukkit.getScheduler().runTaskTimer         (ShadowNight.plugin, runnable, delay, duration); }
    public static BukkitTask delay(@NotNull final Runnable runnable, final long delay)                      { return Bukkit.getScheduler().runTaskLater         (ShadowNight.plugin, runnable, delay); }

    public static BukkitTask   runAsync(@NotNull final Runnable runnable)                                        { return Bukkit.getScheduler().runTaskAsynchronously     (ShadowNight.plugin, runnable); }
    public static BukkitTask  loopAsync(@NotNull final Runnable runnable, final long delay, final long duration) { return Bukkit.getScheduler().runTaskTimerAsynchronously(ShadowNight.plugin, runnable, delay, duration); }
    public static BukkitTask delayAsync(@NotNull final Runnable runnable, final long delay)                      { return Bukkit.getScheduler().runTaskLaterAsynchronously(ShadowNight.plugin, runnable, delay); }
}
