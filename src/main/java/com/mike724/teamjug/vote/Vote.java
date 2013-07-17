package com.mike724.teamjug.vote;

@SuppressWarnings("unused")
public class Vote {

    private int mapID;
    //How many points to add, normally 1
    private int weight;

    public Vote(int mapID, int weight) {
        this.mapID = mapID;
        this.weight = weight;
    }

    public int getMapID() {
        return mapID;
    }

    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
