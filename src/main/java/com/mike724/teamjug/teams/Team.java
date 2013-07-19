package com.mike724.teamjug.teams;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private List<Player> roster;
    private TeamType type;

    public Team(TeamType teamType) {
        roster = new ArrayList<>();
        type = teamType;
    }

    public List<Player> getPlayers() {
        return roster;
    }

    public TeamType getType() {
        return type;
    }
}
