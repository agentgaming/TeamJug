package com.mike724.teamjug.game;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.teams.TeamManager;
import com.mike724.teamjug.timing.TimeConstants;
import com.mike724.teamjug.timing.Timer;
import org.bukkit.ChatColor;

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

        String bcast = "";
        switch(reason) {
            case TIME_OVER: bcast = "Time has ran out!"; break;
            case RED_WIN: bcast = "The "+ChatColor.RED+"RED"+ChatColor.WHITE+" team has won!"; break;
            case BLUE_WIN: bcast = "The "+ChatColor.BLUE+"BLUE"+ChatColor.WHITE+" team has won!"; break;
            case MIN_PLAYERS: bcast = "A minimum of "+ChatColor.YELLOW+GameConstants.MIN_PLAYERS+ChatColor.WHITE+" player(s) are required.";
        }
        this.broadcast(ChatColor.ITALIC+bcast);
        this.broadcast("Game ending...");



        tm.emptyTeams();
    }

    public void broadcast(String msg) {
        TeamJug.getInstance().getServer().broadcastMessage(ChatColor.DARK_RED+"[TeamJug] "+ChatColor.WHITE+msg);
    }

}
