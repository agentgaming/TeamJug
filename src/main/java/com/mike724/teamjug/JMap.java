package com.mike724.teamjug;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

public class JMap {

    private String dispName;
    private String name;
    private final String creator;
    private JLocation redSpawn;
    private JLocation blueSpawn;

    public JMap(String dispName, String name, String creator, JLocation redSpawn, JLocation blueSpawn) {
        this.dispName = dispName;
        this.name = name;
        this.creator = creator;
        this.redSpawn = redSpawn;
        this.blueSpawn = blueSpawn;
    }

    public String getDispName() {
        return dispName;
    }

    public void setDispName(String dispName) {
        this.dispName = dispName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public JLocation getRedSpawn() {
        return redSpawn;
    }

    public void setRedSpawn(JLocation redSpawn) {
        this.redSpawn = redSpawn;
    }

    public JLocation getBlueSpawn() {
        return blueSpawn;
    }

    public void setBlueSpawn(JLocation blueSpawn) {
        this.blueSpawn = blueSpawn;
    }

    public void loadWorld() {
        if (this.isWorldLoaded()) {
            return;
        }
        World w = Bukkit.getServer().createWorld(new WorldCreator(this.getName()));
        w.setMonsterSpawnLimit(0);
        w.setAnimalSpawnLimit(0);
        w.setWaterAnimalSpawnLimit(0);
        w.setAmbientSpawnLimit(0);
        w.setGameRuleValue("mobGriefing", "false");
        w.setGameRuleValue("doFireTick", "false");
        w.setAutoSave(false);
        TeamJug.getInstance().getLogger().info("World " + this.getName() + " loaded!");
    }

    public void unloadWorld() {
        if (this.isWorldLoaded()) {
            //False because we don't want to save any changes (if any happened)
            Bukkit.getServer().unloadWorld(this.getName(), false);
            TeamJug.getInstance().getLogger().info("World " + this.getName() + " unloaded!");
        }
    }

    public boolean isWorldLoaded() {
        return Bukkit.getWorld(this.getName()) != null;
    }

    public World getWorld() {
        World w = Bukkit.getWorld(this.getName());
        if (w == null) {
            w = Bukkit.getServer().createWorld(new WorldCreator(this.getName()));
        }
        return w;
    }
}
