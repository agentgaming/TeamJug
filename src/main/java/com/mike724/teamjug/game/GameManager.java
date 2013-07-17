package com.mike724.teamjug.game;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.enviro.GameMap;
import com.mike724.teamjug.lobby.Lobby;
import com.mike724.teamjug.teams.TeamManager;
import com.mike724.teamjug.timing.State;
import org.bukkit.ChatColor;

@SuppressWarnings("unused")
public class GameManager {

    private State gameState;
    private Game game;
    private Lobby lobby;

    public GameManager() {
        game = null;
        lobby = null;
    }

    /**
     * Called when the plugin is fully loaded and ready for the game
     */
    public void kickStart() {
        startLobby();
    }

    public void startLobby() {
        lobby = new Lobby();
        game = null;
        gameState = State.LOBBY;
    }

    public void startNewGame(TeamManager tm, GameMap map) {
        game = new Game(tm, map);
        lobby = null;
        gameState = State.GAME;
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
        if (game != null) {
            game.onTick(ticks);
        } else if(lobby != null) {
            lobby.onTick(ticks);
        }
    }

    public void broadcast(String msg) {
        TeamJug.getInstance().getServer().broadcastMessage(ChatColor.DARK_RED + "[TeamJug] " + ChatColor.WHITE + msg);
    }
}
