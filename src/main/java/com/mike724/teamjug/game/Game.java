package com.mike724.teamjug.game;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.enviro.GameMap;
import com.mike724.teamjug.teams.TeamManager;
import com.mike724.teamjug.teams.TeamType;
import com.mike724.teamjug.timing.TimeConstants;
import com.mike724.teamjug.timing.Timer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

import java.util.Set;

@SuppressWarnings("unused")
public class Game {

    private TeamManager tm;
    private Timer endGameTimer;
    private Location redSpawn;
    private Location blueSpawn;
    private World world;
    private GameListener listener;

    public Game(TeamManager tm, GameMap map) {
        this.tm = tm;
        //Register timer for game end
        TeamJug tj = TeamJug.getInstance();
        this.endGameTimer = new Timer(TimeConstants.GAME_MAX_TIME, false, this, "endTimeOver");
        tj.getTimeManager().addTimer(endGameTimer);
        this.broadcast("Game session started on map " + map.getDisplayName());

        //Teleport players
        world = tj.getMapManager().getCurrentWorld();
        redSpawn = map.getRedSpawns()[0].getBukkitLocation(world);
        blueSpawn = map.getBlueSpawns()[0].getBukkitLocation(world);

        if (redSpawn == null || blueSpawn == null) {
            this.broadcast("NULL LOCATION");
        }

        for (Player p : tm.getRedTeam().getPlayers()) {
            handlePlayer(p, TeamType.RED);
        }
        for (Player p : tm.getBlueTeam().getPlayers()) {
            handlePlayer(p, TeamType.BLUE);
        }

        //Register listener
        listener = new GameListener();
        tj.getServer().getPluginManager().registerEvents(listener, tj);
    }

    //Don't worry little guy, I'll get you into the game
    public void addPlayerAfterStart(Player p) {
        p.sendMessage(ChatColor.GRAY + "Welcome to the game!");
        TeamType tt = tm.addPlayerToAnyTeam(p);
        this.handlePlayer(p, tt);
    }

    public void handlePlayer(Player p, TeamType tt) {
        switch (tt) {
            case RED:
                p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You are on the red team.");
                p.teleport(redSpawn);
                break;
            case BLUE:
                p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You are on the blue team.");
                p.teleport(blueSpawn);
                break;
            default:
                break;
        }
        //TODO: Give player weapons!
    }

    public void onTick(long ticks) {
        TeamJug tj = TeamJug.getInstance();
        Set<Player> all = tm.getAllPlayers();

        //Time bar
        long timeLeft = tj.getTimeManager().getTimeLeft(endGameTimer);
        long totalTime = endGameTimer.getTime();
        float percent = (float) ((double) timeLeft / (double) totalTime);
        for (Player p : all) {
            p.setExp(percent);
            // p.sendMessage("percent: "+percent);
            //p.sendMessage("timeLeft: "+timeLeft);
            //p.sendMessage("totalTime: "+totalTime);
        }
    }

    public void endTimeOver() {
        this.end(GameEndReason.TIME_OVER);
    }

    public void end(GameEndReason reason) {
        //Kill the time over timer
        if (reason != GameEndReason.TIME_OVER) {
            endGameTimer.setKillFlag(true);
        }

        String bcast = "";
        switch (reason) {
            case TIME_OVER:
                bcast = "Time has ran out!";
                break;
            case RED_WIN:
                bcast = "The " + ChatColor.RED + "RED" + ChatColor.WHITE + " team has won!";
                break;
            case BLUE_WIN:
                bcast = "The " + ChatColor.BLUE + "BLUE" + ChatColor.WHITE + " team has won!";
                break;
            case MIN_PLAYERS:
                bcast = "A minimum of " + ChatColor.YELLOW + GameConstants.MIN_PLAYERS + ChatColor.WHITE + " player(s) are required.";
        }
        this.broadcast(ChatColor.ITALIC + bcast);
        this.broadcast("Game ending...");
        tm.emptyTeams();

        HandlerList.unregisterAll(listener);
        TeamJug.getInstance().getGameManager().startLobby();
    }

    public void broadcast(String msg) {
        TeamJug.getInstance().getServer().broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TeamJug] " + ChatColor.WHITE + msg);
    }

}
