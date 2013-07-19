package com.mike724.teamjug.teams;

import com.mike724.teamjug.TeamJug;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Handles the management of the different teams
 */
public class TeamManager {

    private Team redTeam;
    private Team blueTeam;

    public TeamManager() {
        redTeam = new Team(TeamType.RED);
        blueTeam = new Team(TeamType.BLUE);
    }

    public void emptyTeams() {
        redTeam.getPlayers().clear();
        blueTeam.getPlayers().clear();
    }

    public void setupTeams(Player[] allPlayersArray) {
        TeamJug tj = TeamJug.getInstance();
        List<Player> allPlayers = Arrays.asList(allPlayersArray);
        Collections.shuffle(allPlayers);
        List<Player> redPlayers = new ArrayList<>();
        redPlayers.addAll(allPlayers.subList(0, allPlayers.size() / 2 + allPlayers.size() % 2));
        List<Player> bluePlayers = new ArrayList<>();
        bluePlayers.addAll(allPlayers.subList(allPlayers.size() / 2 + allPlayers.size() % 2, allPlayers.size()));

        redTeam.getPlayers().addAll(redPlayers);
        blueTeam.getPlayers().addAll(bluePlayers);
    }

    public TeamType addPlayerToAnyTeam(Player p) {
        Team team = (redTeam.getPlayers().size() < blueTeam.getPlayers().size()) ? redTeam : blueTeam;
        team.getPlayers().add(p);
        return team.getType();
    }

    public Team getRedTeam() {
        return redTeam;
    }

    public Team getBlueTeam() {
        return blueTeam;
    }

    public List<Player> getAllPlayers() {
        List<Player> players1 = redTeam.getPlayers();
        List<Player> players2 = blueTeam.getPlayers();
        List<Player> all = new ArrayList<>(players1);
        all.addAll(players2);
        return all;
    }
}
