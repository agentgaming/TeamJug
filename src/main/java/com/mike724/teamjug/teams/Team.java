package com.mike724.teamjug.teams;

import com.mike724.teamjug.stats.TStats;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class Team {

    private HashMap<Player, TStats> roster;

    public Team() {
        roster = new HashMap<>();
    }

    //TeamManager requires the HashMap
    public HashMap<Player, TStats> getRosterRaw() {
        return roster;
    }

}
