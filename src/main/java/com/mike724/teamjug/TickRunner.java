package com.mike724.teamjug;

/** Runnable that runs every tick */
@SuppressWarnings("unused")
public class TickRunner implements Runnable {

    private long ticks = 0l;

    @Override
    public void run() {
        TeamJug tj = TeamJug.getInstance();
        tj.getTimeManager().onTick(ticks);
        tj.getEnviroMaintainer().onTick(ticks);
        tj.getGameManager().onTick(ticks);
        ticks++;
    }

}
