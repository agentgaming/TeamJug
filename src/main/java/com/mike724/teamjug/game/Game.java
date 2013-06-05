package com.mike724.teamjug.game;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.teams.TeamManager;
import com.mike724.teamjug.timing.TimeConstants;
import com.mike724.teamjug.timing.Timer;

@SuppressWarnings("unused")
public class Game {

    private TeamManager tm;
    private Timer endGameTimer;

    public Game(TeamManager tm) {
        this.tm = tm;
        //Register timer for game end
        this.endGameTimer = new Timer(TimeConstants.GAME_MAX_TIME, false, this, "endTimeOver");
        TeamJug.getInstance().getTimeManager().addTimer(endGameTimer);
    }

    public void onTick(long ticks) {

    }

    public void endTimeOver() {
        this.end(GameEndReason.TIME_OVER);
    }

    public void end(GameEndReason reason) {
        //Kill the time over timer
        if(reason != GameEndReason.TIME_OVER) {
            endGameTimer.setKillFlag(true);
        }
        tm.emptyTeams();
    }

}
