package com.mike724.teamjug;

import com.mike724.teamjug.teams.TeamManager;

@SuppressWarnings("unused")
public class GameManager {

    private Game game;

    public GameManager() {

    }

    public void newGame() {
        TeamManager tm = new TeamManager();
        //Setup teams, this will also each player's stats
        tm.setupTeams(TeamJug.getInstance().getServer().getOnlinePlayers());
        //Initialize the game object, give it the TeamManager
        game = new Game(tm);
    }

    public void endGame() {
        game.end();
        game = null;
    }

    public void onTick(long ticks) {
        if(game != null) {
            game.onTick(ticks);
        }
    }
}
