package com.mike724.teamjug.player;

import com.mike724.teamjug.TeamJug;

import java.util.HashMap;

public class MetadataManager {

    private HashMap<String, Metadata> cache;

    public MetadataManager() {
        cache = new HashMap<>();
    }

    public void addPlayerToCache(String pName) {
        Metadata md = new Metadata(pName);
        md.loadMetadata();
        cache.put(pName, md);
    }

    public void removePlayerFromCache(String pName) {
        if(cache.containsKey(pName)) {
            cache.remove(pName);
        }
    }

    public void clearAllCache() {
        cache.clear();
    }

    public void savePlayerMetadata(String pName) {
        if(cache.containsKey(pName)) {
            TeamJug.getInstance().getDataStorage().writeObjects(cache.get(pName).getSaveMap());
        }
    }

    public void saveAllMetadata() {
        HashMap<Object, String> upload = new HashMap<>();
        for(Metadata md : cache.values()) {
            upload.putAll(md.getSaveMap());
        }
        if(upload.isEmpty()) {
            return;
        }
        TeamJug.getInstance().getDataStorage().writeObjects(upload);
    }

    public Metadata getPlayerMetadata(String pName) {
        if(cache.containsKey(pName)) {
            return cache.get(pName);
        } else {
            return null;
        }
    }

}
