package org.shadownight.plugin.shadownight.utils;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import org.shadownight.plugin.shadownight.ShadowNight;

public class Timer {
    private final int max;
    private int s;
    final Runnable onStart;
    final Runnable onUpdate;
    final Runnable onFinish;
    BukkitTask task;

    public Timer(int _max, Runnable _onStart, Runnable _onUpdate, Runnable _onFinish){
        onStart = _onStart;
        onUpdate = _onUpdate;
        onFinish = _onFinish;
        max = _max;
    }

    public int getTimeLeft(){
        return s;
    }

    public void update(){
        s--;
        if(s == 0) {
            task.cancel();
            onFinish.run();
        }
        else onUpdate.run();
    }

    public void start(){
        if(task != null) task.cancel();
        s = max;
        onStart.run();
        task = Bukkit.getScheduler().runTaskTimer(ShadowNight.plugin, this::update, 20, 20);
    }


}
