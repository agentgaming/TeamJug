package com.mike724.teamjug.stats;

public class TStats {

    private String name;
    private int kills, deaths;

    public TStats(String name) {
        this.name = name;
        this.kills = 0;
        this.deaths = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
}
