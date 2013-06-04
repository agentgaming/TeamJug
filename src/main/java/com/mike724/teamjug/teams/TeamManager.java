package com.mike724.teamjug.teams;

import com.mike724.networkapi.DataStorage;
import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.stats.TStats;
import org.bukkit.entity.Player;

import java.util.*;

/** Handles the management of the different teams */
public class TeamManager {

    private Team redTeam;
    private Team blueTeam;

    public TeamManager() {
        redTeam = new Team();
        blueTeam = new Team();
    }

    public void emptyTeams() {
        redTeam.getRosterRaw().clear();
        blueTeam.getRosterRaw().clear();
    }

    public void setupTeams(Player[] allPlayersArray) {
        TeamJug tj = TeamJug.getInstance();
        List<Player> allPlayers = Arrays.asList(allPlayersArray);
        Collections.shuffle(allPlayers);
        List<Player> redPlayers = new ArrayList<>();
        redPlayers.addAll(allPlayers.subList(0, allPlayers.size() / 2 + allPlayers.size() % 2));
        List<Player> bluePlayers = new ArrayList<>();
        bluePlayers.addAll(allPlayers.subList(allPlayers.size() / 2 + allPlayers.size() % 2, allPlayers.size()));

        DataStorage ds = tj.getDataStorage();

        ArrayList<TStats> redStats = new ArrayList<>();
        ArrayList<TStats> blueStats = new ArrayList<>();
        for(int i=0;i<2;i++) {
            List<Player> players = (i==0) ? redPlayers : bluePlayers;
            String[] names = new String[players.size()];
            for(Player p : players) {
                names[names.length] = p.getName();
            }
            for(Object obj : ds.getObjects(Arrays.asList(names), TStats.class)) {
                if(obj != null && obj instanceof TStats) {
                    ((i==0) ? redStats : blueStats).add((TStats)obj);
                }
            }
        }

        for(int i=0;i<2;i++) {
            for(Player p : ((i==0) ? redPlayers : bluePlayers)) {
                boolean found = false;
                Team team = (i==0) ? redTeam : blueTeam;
                for(TStats ts : ((i==0) ? redStats : blueStats)) {
                    if(p.getName().equalsIgnoreCase(ts.getName())) {
                        team.getRosterRaw().put(p, ts);
                        found = true;
                    }
                }
                if(!found) {
                    //New player detected
                    team.getRosterRaw().put(p, new TStats(p.getName()));
                }
            }
        }
    }

    public Team getRedTeam() {
        return redTeam;
    }

    public Team getBlueTeam() {
        return blueTeam;
    }

    public Set<Player> getAllPlayers() {
        Set<Player> players = redTeam.getRosterRaw().keySet();
        players.addAll(blueTeam.getRosterRaw().keySet());
        return players;
    }
}