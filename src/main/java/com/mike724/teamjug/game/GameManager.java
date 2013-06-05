package com.mike724.teamjug.game;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.game.Game;
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

    public Game getGame() {
        return game;
    }

    public void onTick(long ticks) {
        if(game != null) {
            game.onTick(ticks);
        }
    }
}
