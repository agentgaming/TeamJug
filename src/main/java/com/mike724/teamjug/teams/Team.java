package com.mike724.teamjug.teams;

import com.mike724.teamjug.stats.TStats;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;

public class Team {

    private HashMap<Player, TStats> roster;
    private TeamType type;

    public Team(TeamType teamType) {
        roster = new HashMap<>();
        type = teamType;
    }

    //TeamManager requires the HashMap
    public HashMap<Player, TStats> getRosterRaw() {
        return roster;
    }

    public Set<Player> getPlayers() {
        return roster.keySet();
    }

    public TeamType getType() {
        return type;
    }
}
