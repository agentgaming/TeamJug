package com.mike724.teamjug.enviro;

import com.mike724.teamjug.TeamJug;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class MapManager {

    private List<GameMap> maps;
    private GameMap loadedMap;
    private World loadedWorld;
    private World lobbyWorld;

    public MapManager() {
        this.maps = new ArrayList<>();
        this.loadedMap = null;
        this.loadedWorld = null;
        this.lobbyWorld = Bukkit.getWorld("lobby");
    }

    public void loadMap(GameMap map) {
        this.unloadCurrent();

        World w = Bukkit.getServer().createWorld(new WorldCreator(map.getRawName()));
        w.setDifficulty(Difficulty.NORMAL);
        w.setPVP(true);
        w.setMonsterSpawnLimit(0);
        w.setAnimalSpawnLimit(0);
        w.setWaterAnimalSpawnLimit(0);
        w.setAmbientSpawnLimit(0);
        w.setGameRuleValue("mobGriefing", "false");
        w.setGameRuleValue("doFireTick", "false");
        w.setAutoSave(false);
        w.setKeepSpawnInMemory(false);

        this.loadedMap = map;
        this.loadedWorld = w;

        TeamJug.getInstance().getLogger().info("Map " + map.getRawName() + " loaded!");
    }

    public void unloadCurrent() {
        if(loadedWorld != null && loadedMap != null) {
            Bukkit.getServer().unloadWorld(loadedMap.getRawName(), false);
            TeamJug.getInstance().getLogger().info("Map " + loadedMap.getRawName() + " unloaded!");
        }
    }

    public World getCurrentWorld() {
        return loadedWorld;
    }

    public World getLobbyWorld() {
        return lobbyWorld;
    }

    public void addDefaultMaps() {
        //Scrapyard
        TLocation[] m1rs = {new TLocation(-14, 24, 186)};
        TLocation[] m1bs = {new TLocation(-67, 23, 107)};
        GameMap m1 = new GameMap("Scrapyard", "scrapyard", strTok("CaptainP1ckle"), m1rs, m1bs);
        maps.add(m1);

        //Another map
    }

    public List<GameMap> getMaps() {
        return Collections.unmodifiableList(maps);
    }

    public String[] strTok(String data) {
        return data.split(",");
    }

}
