package com.mike724.teamjug.lobby;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.timing.TimeConstants;
import com.mike724.teamjug.timing.Timer;

@SuppressWarnings("unused")
public class Lobby {

    public Lobby() {
        Timer end = new Timer(TimeConstants.LOBBY_TIME, false, this, "onTimeOver");
        TeamJug.getInstance().getTimeManager().addTimer(end);
    }

    public void onTimeOver() {
        //Start game
    }

    public void onTick(long ticks) {

    }

}
