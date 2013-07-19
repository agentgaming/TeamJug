package com.mike724.teamjug;

import com.mike724.networkapi.DataStorage;
import com.mike724.teamjug.enviro.EnviroMaintainer;
import com.mike724.teamjug.enviro.MapManager;
import com.mike724.teamjug.game.GameManager;
import com.mike724.teamjug.player.Metadata;
import com.mike724.teamjug.player.MetadataManager;
import com.mike724.teamjug.timing.TimeManager;
import com.mike724.teamjug.timing.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamJug extends JavaPlugin {

    private static TeamJug instance;
    private TimeManager timeManager;
    private EnviroMaintainer enviroMaintainer;
    private DataStorage dataStorage;
    private GameManager gameManager;
    private MapManager mapManager;
    private MetadataManager metadataManager;

    /* Startup/shutdown logic */

    @Override
    public void onEnable() {
        instance = this;
        timeManager = new TimeManager();
        enviroMaintainer = new EnviroMaintainer();
        try {
            dataStorage = new DataStorage("jxBkqvpe0seZhgfavRqB", "RXaCcuuQcIUFZuVZik9K", "nXWvOgfgRJKBbbzowle1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameManager = new GameManager();
        mapManager = new MapManager();
        mapManager.addDefaultMaps();
        metadataManager = new MetadataManager();

        //this.getTimeManager().addTimer(new Timer(5, true, "testMethod"));
        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new TickRunner(), 0l, 1l);

        //Listeners
        PluginManager pm = this.getServer().getPluginManager();
        pm.registerEvents(enviroMaintainer, this);

        //START IT UP
        gameManager.kickStart();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    /* Getters and setters */

    public TimeManager getTimeManager() {
        return timeManager;
    }

    public static TeamJug getInstance() {
        return instance;
    }

    public EnviroMaintainer getEnviroMaintainer() {
        return enviroMaintainer;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public MetadataManager getMetadataManager() {
        return metadataManager;
    }

    /* Debug messages */

    public static void errorMessage(String error) {
        getInstance().getLogger().severe(error);
    }
}
