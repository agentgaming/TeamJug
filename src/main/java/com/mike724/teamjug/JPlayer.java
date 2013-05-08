package com.mike724.teamjug;

import com.mike724.networkapi.DataStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class JPlayer {

    private TJData data;
    private boolean juggernaut;
    private TeamType team;

    public JPlayer(String name) {
        DataStorage ds = TeamJug.getInstance().getDataStorage();
        this.data = (TJData) ds.getObject(TJData.class, name);
        if (this.data == null) {
            this.data = new TJData(name, 0, 0, 0);
            ds.writeObject(this.data, name);
        }
        team = TeamType.NULL;
    }

    public void tell(String msg) {
        Player p = this.getBukkitPlayer();
        if (p != null) {
            p.sendMessage(ChatColor.BLUE + "[TeamJug] " + ChatColor.GRAY + msg);
            return;
        }
        TeamJug.getInstance().errorLog("Null player (tell method) name: " + this.getName());
    }

    public String getName() {
        return data.getPlayer().trim();
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(this.data.getPlayer());
    }

    public TJData getData() {
        return data;
    }

    public boolean isJuggernaut() {
        return juggernaut;
    }

    public void setJuggernaut(boolean juggernaut) {
        this.juggernaut = juggernaut;
    }

    public TeamType getTeam() {
        return team;
    }

    public void setTeam(TeamType team) {
        this.team = team;
    }
}
