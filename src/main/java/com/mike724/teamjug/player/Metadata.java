package com.mike724.teamjug.player;

import com.mike724.networkapi.DataStorage;
import com.mike724.networkapi.NetworkPlayer;
import com.mike724.networkapi.NetworkRank;
import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.stats.TStats;

import java.util.HashMap;

/** This class holds the player's metadata, for example:
 *  - Stats (TStats)
 *  - Network player (NetworkPlayer)
 */
@SuppressWarnings("unused")
public class Metadata {

    private String playerName;
    private KitType kitType;
    private NetworkPlayer networkPlayer;
    private NetworkRank networkRank;
    private TStats stats;

    public Metadata(String playerName) {
        this.playerName = playerName;
        this.networkPlayer = null;
        this.networkRank = null;
        this.stats = null;
    }

    public void loadMetadata() {
        DataStorage ds = TeamJug.getInstance().getDataStorage();

        Object npObj = ds.getObject(NetworkPlayer.class, playerName);
        if(npObj == null) {
            npObj = new NetworkPlayer(playerName);
        }
        this.networkPlayer = (NetworkPlayer)npObj;

        Object rankObj = ds.getObject(NetworkRank.class, playerName);
        if(rankObj == null) {
            rankObj = NetworkRank.USER;
        }
        this.networkRank = (NetworkRank)rankObj;

        Object statsObj = ds.getObject(TStats.class, playerName);
        if(statsObj == null) {
            statsObj = new TStats(playerName);
        }
        this.stats = (TStats)statsObj;
    }

    public HashMap<Object, String> getSaveMap() {
        HashMap<Object, String> map = new HashMap<>();
        map.put(networkPlayer, playerName);
        map.put(networkRank, playerName);
        map.put(stats, playerName);
        return map;
    }

    public String getPlayerName() {
        return playerName;
    }

    public NetworkPlayer getNetworkPlayer() {
        return networkPlayer;
    }

    public NetworkRank getNetworkRank() {
        return networkRank;
    }

    public TStats getStats() {
        return stats;
    }

    public KitType getKitType() {
        return kitType;
    }

    public void setKitType(KitType kitType) {
        this.kitType = kitType;
    }
}
