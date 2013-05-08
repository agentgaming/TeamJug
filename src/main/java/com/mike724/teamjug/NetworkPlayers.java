package com.mike724.teamjug;

import com.mike724.networkapi.NetworkPlayer;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * User: Dakota
 * Date: 4/14/13
 * Time: 1:17 PM
 */
public class NetworkPlayers {
    private static CopyOnWriteArrayList<NetworkPlayer> networkPlayers = new CopyOnWriteArrayList<>();

    public static void addNetworkPlayer(NetworkPlayer np) {
        for (NetworkPlayer np1 : networkPlayers) {
            if (np1.getPlayer().equals(np.getPlayer())) return;
        }
        networkPlayers.add(np);
    }

    public static void removeNetworkPlayer(String name) {
        for (NetworkPlayer np : networkPlayers) {
            if (np.getPlayer().equals(name)) networkPlayers.remove(np);
        }
    }

    public static NetworkPlayer getNetworkPlayer(String name) {
        for (NetworkPlayer np : networkPlayers) {
            if (np.getPlayer().equals(name)) return np;
        }
        return null;
    }

    public static CopyOnWriteArrayList<NetworkPlayer> getNetworkPlayers() {
        return networkPlayers;
    }
}
