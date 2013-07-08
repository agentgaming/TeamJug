package com.mike724.teamjug;

import com.mike724.networkapi.NetworkPlayer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class NetworkPlayerManager {

    private HashMap<String, NetworkPlayer> cache;

    public  NetworkPlayerManager() {
        cache = new HashMap<>();
    }

    public boolean saveAllNetworkPlayers() {
        HashMap<String, Object> dumbMap = new HashMap<>();
        for(Map.Entry<String, NetworkPlayer> e : cache.entrySet()) {
            dumbMap.put(e.getKey(), (Object)e.getValue());
        }
        return TeamJug.getInstance().getDataStorage().writeObjects(dumbMap, NetworkPlayer.class);
    }

    public boolean saveNetworkPlayer(String name) {
        if(!cache.containsKey(name)) {
            return false;
        }
        return TeamJug.getInstance().getDataStorage().writeObject(cache.get(name), name);
    }

    public NetworkPlayer getNetworkPlayer(String name) {
        if(cache.containsKey(name)) {
            return cache.get(name);
        }
        NetworkPlayer np;
        Object obj = TeamJug.getInstance().getDataStorage().getObject(NetworkPlayer.class, name);
        if(obj == null || !(obj instanceof NetworkPlayer)) {
            np = new NetworkPlayer(name);
        } else {
            np = (NetworkPlayer)obj;
        }
        cache.put(name, np);
        return np;
    }

    public Collection<NetworkPlayer> getAllNetworkPlayers() {
        return cache.values();
    }

    public void removeNetworkPlayer(String name) {
        cache.remove(name);
    }

    public void removeAllNetworkPlayers() {
        cache.clear();
    }
}
