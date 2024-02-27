package org.uwu_snek.shadownight.utils;

import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.Nullable;
import org.uwu_snek.shadownight.utils.spigot.Scheduler;




public final class Timer {
    private final int duration;
    private int s;
    private final Runnable onStart;
    private final Runnable onUpdate;
    private final Runnable onFinish;
    private BukkitTask task;


    /**
     * Creates a new Timer.
     * @param _duration The duration of the timer expressed in seconds
     * @param _onStart The function to run when the timer is started
     * @param _onUpdate The function to run when the remaining time is updated
     * @param _onFinish The function to run when the time runs out
     */
    public Timer(final int _duration, @Nullable final Runnable _onStart, @Nullable final Runnable _onUpdate, @Nullable final Runnable _onFinish){
        duration = _duration;
        onStart = _onStart;
        onUpdate = _onUpdate;
        onFinish = _onFinish;
    }


    /**
     * Returns the remaining time expressed in seconds
     * @return The remaining time
     */
    public int getTimeLeft(){
        return s;
    }


    private void update(){
        s--;
        if(s == 0) {
            task.cancel();
            if(onFinish != null) onFinish.run();
        }
        else if(onUpdate != null) onUpdate.run();
    }


    /**
     * Calls the onStart function and starts the timer
     */
    public void start(){
        if(task != null) task.cancel();
        s = duration;
        if(onStart != null) onStart.run();
        task = Scheduler.loop(this::update, 20, 20);
    }
}
