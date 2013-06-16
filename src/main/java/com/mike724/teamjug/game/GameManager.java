package com.mike724.teamjug.game;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.game.Game;
import com.mike724.teamjug.lobby.Lobby;
import com.mike724.teamjug.teams.TeamManager;
import com.mike724.teamjug.timing.State;

@SuppressWarnings("unused")
public class GameManager {

    private State gameState;
    private Game game;
    private Lobby lobby;

    public GameManager() {
        game = null;
        lobby = null;
    }

    /** Called when the plugin is fully loaded and ready for the game */
    public void kickStart() {

    }

    public void newGame() {
        TeamManager tm = new TeamManager();
        //Setup teams, this will also each player's stats
        tm.setupTeams(TeamJug.getInstance().getServer().getOnlinePlayers());
        //Initialize the game object, give it the TeamManager
        game = new Game(tm);
    }

    public State getGameState() {
        return gameState;
    }

    public void setGameState(State gameState) {
        this.gameState = gameState;
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
