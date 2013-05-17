package com.mike724.teamjug;

import com.mike724.networkapi.DataStorage;
import com.mike724.networkapi.NetworkPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedHashSet;
import java.util.Set;

public class TeamJug extends JavaPlugin {

    private static TeamJug instance;
    private Game game;
    private GameState state;
    private JMap spawnMap;
    private Set<JMap> maps = new LinkedHashSet<>();

    private DataStorage dataStorage;

    @Override
    public void onEnable() {
        instance = this;

        this.dataStorage = new DataStorage("auth", "OBjwrGyI1Pdj3Dzi", "password");

        //Add maps
        JLocation spawnLobbyLoc = new JLocation(4, 60, 8);
        this.spawnMap = new JMap("Spawn Lobby", "lobby", "Mike724", spawnLobbyLoc, spawnLobbyLoc);
        this.maps.add(new JMap("Rust", "rust", "CaptainP1ckle", new JLocation(0, 67, -2), new JLocation(32, 66, 15)));
        this.maps.add(new JMap("Dome", "dome", "CaptainP1ckle", new JLocation(-324, 16, -25), new JLocation(-265, 10, 75)));
        this.maps.add(new JMap("Scrapyard", "scrapyard", "CaptainP1ckle", new JLocation(-14, 24, 186), new JLocation(-67, 23, 107)));
        this.maps.add(new JMap("Underground", "underground", "CaptainP1ckle & hardc0reapplez", new JLocation(-327, 16, 77), new JLocation(-225, 13, 62)));

        this.state = GameState.LOBBY;
        this.game = new Game();

        this.getServer().getPluginManager().registerEvents(new JListener(), this);

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, this.game, 0l, 1l);
    }

    @Override
    public void onDisable() {
        for (JMap map : this.maps) {
            map.unloadWorld();
        }
        for (NetworkPlayer np : NetworkPlayers.getNetworkPlayers()) {
            np.setOnline(false);
            this.getDataStorage().writeObject(np, np.getPlayer());
        }
        this.spawnMap.unloadWorld();
    }

    public void errorLog(String msg) {
        this.getLogger().severe(msg);
    }

    public JMap getSpawnMap() {
        return spawnMap;
    }

    public Set<JMap> getMaps() {
        return maps;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public static TeamJug getInstance() {
        return instance;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public void safeTP(Player p, Location loc) {
        if (!loc.getChunk().isLoaded()) {
            loc.getChunk().load();
        }
        p.teleport(loc);
    }

}
