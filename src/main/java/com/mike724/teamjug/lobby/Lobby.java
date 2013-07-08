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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@SuppressWarnings("unused")
public class Lobby {

    private TeamManager tmPassOff = null;
    private GameMap mapPassOff = null;
    private final TLocation SPAWN =  new TLocation(4, 60, 8);
    private LobbyListener listener;

    public Lobby() {
        TeamJug tj = TeamJug.getInstance();
        Timer end = new Timer(TimeConstants.LOBBY_TIME, false, this, "onTimeOver");
        tj.getTimeManager().addTimer(end);

        //Teleport all players
        Location loc = SPAWN.getBukkitLocation(tj.getMapManager().getLobbyWorld());
        for(Player p : getAllPlayers()) {
            p.teleport(loc);
        }

        listener = new LobbyListener();
        tj.getServer().getPluginManager().registerEvents(listener, tj);
        this.broadcast("Lobby session started");
    }

    public void onTimeOver() {
        //Start prepping the game
        TeamJug.getInstance().getTimeManager().addTimer(new Timer(TimeConstants.LOBBY_PREP_TIME, false, this, "onPrepOver"));

        this.broadcast("Preparing game...");

        MapManager mm = TeamJug.getInstance().getMapManager();

        GameMap map = mm.getMaps().get(0);
        mm.loadMap(map);

        TeamManager tm = new TeamManager();
        tm.setupTeams(TeamJug.getInstance().getServer().getOnlinePlayers());

        this.tmPassOff = tm;
        this.mapPassOff = map;
    }

    public void onPrepOver() {
        HandlerList.unregisterAll(listener);
        TeamJug.getInstance().getGameManager().startNewGame(tmPassOff, mapPassOff);
    }

    public void onTick(long ticks) {

    }

    public Player[] getAllPlayers() {
        return Bukkit.getOnlinePlayers();
    }

    public void broadcast(String msg) {
        TeamJug.getInstance().getServer().broadcastMessage(ChatColor.DARK_RED+""+ChatColor.BOLD+"[TeamJug] "+ChatColor.WHITE+msg);
    }

}
