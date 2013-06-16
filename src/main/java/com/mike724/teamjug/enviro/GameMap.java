package com.mike724.teamjug.enviro;

public class GameMap {

    private String displayName;
    private String rawName;
    private String[] creators;
    private TLocation[] redSpawns;
    private TLocation[] blueSpawns;

    public GameMap(String displayName, String rawName, String[] creators, TLocation[] redSpawns, TLocation[] blueSpawns) {
        this.displayName = displayName;
        this.rawName = rawName;
        this.creators = creators;
        this.redSpawns = redSpawns;
        this.blueSpawns = blueSpawns;
    }

    /** Special constructor for the lobby map */
    public GameMap(String displayName, String rawName, String[] creators, TLocation spawn) {
        this.displayName = displayName;
        this.rawName = rawName;
        this.creators = creators;
        this.redSpawns = new TLocation[1];
        this.blueSpawns = new TLocation[1];
        this.redSpawns[0] = spawn;
        this.blueSpawns[0] = spawn;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getRawName() {
        return rawName;
    }

    public void setRawName(String rawName) {
        this.rawName = rawName;
    }

    public String[] getCreators() {
        return creators;
    }

    public void setCreators(String[] creators) {
        this.creators = creators;
    }

    public TLocation[] getRedSpawns() {
        return redSpawns;
    }

    public void setRedSpawns(TLocation[] redSpawns) {
        this.redSpawns = redSpawns;
    }

    public TLocation[] getBlueSpawns() {
        return blueSpawns;
    }

    public void setBlueSpawns(TLocation[] blueSpawns) {
        this.blueSpawns = blueSpawns;
    }
}
