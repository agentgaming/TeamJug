package com.mike724.teamjug.lobby;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.enviro.GameMap;
import com.mike724.teamjug.enviro.MapManager;
import com.mike724.teamjug.enviro.TLocation;
import com.mike724.teamjug.game.Game;
import com.mike724.teamjug.teams.TeamManager;
import com.mike724.teamjug.timing.State;
import com.mike724.teamjug.timing.TimeConstants;
import com.mike724.teamjug.timing.Timer;
import com.mike724.teamjug.util.PlayerUtil;
import com.mike724.teamjug.vote.VoteManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class Lobby {

    private TeamManager tmPassOff = null;
    private GameMap mapPassOff = null;
    private final TLocation SPAWN =  new TLocation(4, 60, 8);
    private Location spawnLoc;
    private LobbyListener listener;
    private Timer endLobbyTimer;
    private VoteManager voteManager;

    public Lobby() {
        voteManager = new VoteManager();
        voteManager.setup();

        TeamJug tj = TeamJug.getInstance();
        endLobbyTimer = new Timer(TimeConstants.LOBBY_TIME, false, this, "_onTimeOver");
        tj.getTimeManager().addTimer(endLobbyTimer);

        //Teleport all players
        spawnLoc = SPAWN.getBukkitLocation(tj.getMapManager().getLobbyWorld());
        for(Player p : getAllPlayers()) {
            handlePlayer(p);
        }

        listener = new LobbyListener(this);
        tj.getServer().getPluginManager().registerEvents(listener, tj);
        this.broadcast("Lobby session started");
    }

    public void handlePlayer(Player p) {
        PlayerUtil.clearPlayerCompletely(p);
        p.setGameMode(GameMode.ADVENTURE);
        p.teleport(spawnLoc);
        voteManager.showVoteBoard(p);
    }

    public void _onTimeOver() {
        //Start prepping the game
        TeamJug.getInstance().getTimeManager().addTimer(new Timer(TimeConstants.LOBBY_PREP_TIME, false, this, "_onPrepOver"));

        GameMap map = voteManager.getWinningMap();

        //Remove vote scoreboard
        for(Player p : getAllPlayers()) {
            p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }

        this.broadcast("Preparing game...");

        MapManager mm = TeamJug.getInstance().getMapManager();

        mm.loadMap(map);

        TeamManager tm = new TeamManager();
        tm.setupTeams(TeamJug.getInstance().getServer().getOnlinePlayers());

        this.tmPassOff = tm;
        this.mapPassOff = map;
    }

    public void _onPrepOver() {
        HandlerList.unregisterAll(listener);
        TeamJug.getInstance().getGameManager().startNewGame(tmPassOff, mapPassOff);
    }

    public void onTick(long ticks) {
        //Time bar
        Player[] all = this.getAllPlayers();
        TeamJug tj = TeamJug.getInstance();
        long timeLeft = tj.getTimeManager().getTimeLeft(endLobbyTimer);
        long totalTime = endLobbyTimer.getTime();
        float percent = (float) ((double) timeLeft / (double) totalTime);
        for (Player p : all) {
            p.setExp(percent);
        }
    }

    public Player[] getAllPlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public VoteManager getVoteManager() {
        return voteManager;
    }

    public void broadcast(String msg) {
        TeamJug.getInstance().getServer().broadcastMessage(ChatColor.DARK_RED+""+ChatColor.BOLD+"[TeamJug] "+ChatColor.WHITE+msg);
    }

}
