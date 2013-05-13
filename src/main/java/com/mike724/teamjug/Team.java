package com.mike724.teamjug;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Team {

    private HashMap<String, JPlayer> roster;
    private boolean juggernautSet = false;
    private int jugKills;

    public Team(List<JPlayer> members) {
        this.jugKills = 0;
        roster = new HashMap<>();
        for (JPlayer jp : members) {
            roster.put(jp.getName(), jp);
        }
    }

    public Collection<JPlayer> getMembers() {
        return roster.values();
    }

    public void addPlayer(String name, JPlayer jp) {
        roster.put(name, jp);
    }

    public void removePlayer(String name) {
        roster.remove(name);
    }

    public boolean isPlayerOnTeam(String name) {
        if (roster == null || roster.isEmpty()) {
            return false;
        }
        boolean ans = roster.containsKey(name);
        return roster.containsKey(name);
    }

    public JPlayer getPlayer(String name) {
        return roster.get(name);
    }

    public void setNewJuggernaut() {
        JPlayer cur = this.getJuggernaut();
        if(cur != null) {
            cur.setJuggernaut(false);
            cur.tell("You're no longer the juggernaut!");
        }
        Random rand = new Random();
        int num = rand.nextInt(roster.size());
        JPlayer newJug = (JPlayer) roster.values().toArray()[num];
        newJug.setJuggernaut(true);
        TeamJug.getInstance().getGame().giveLoadout(newJug.getBukkitPlayer(), newJug.getTeam());
        newJug.tell("You're the new juggernaut!");
        TeamJug.getInstance().getGame().broadcast(newJug.getName()+" is team "+newJug.getTeam().name()+"'s new juggernaut!");
        this.juggernautSet = true;
    }

    public boolean isJuggernautSet() {
        return juggernautSet;
    }

    public JPlayer getJuggernaut() {
        for (JPlayer p : roster.values()) {
            if (p.isJuggernaut()) {
                return p;
            }
        }
        return null;
    }

    public int getJugKills() {
        return jugKills;
    }

    public void setJugKills(int jugKills) {
        this.jugKills = jugKills;
    }
}
