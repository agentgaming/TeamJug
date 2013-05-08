package com.mike724.teamjug;

public class TJData {

    private String player;
    private int kills;
    private int deaths;
    private int jugKills;

    public TJData(String player, int kills, int deaths, int jugKills) {
        this.player = player;
        this.kills = kills;
        this.deaths = deaths;
        this.jugKills = jugKills;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
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

    public int getJugKills() {
        return jugKills;
    }

    public void setJugKills(int jugKills) {
        this.jugKills = jugKills;
    }
}
