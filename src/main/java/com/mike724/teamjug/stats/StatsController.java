package com.mike724.teamjug.stats;

import com.mike724.motoapi.storage.Storage;
import com.mike724.teamjug.TeamJug;

public class StatsController {

    public static void addKills(String p, int num) {
        TStats stats = getTStats(p);
        stats.setKills(stats.getKills()+num);
    }
    public static void addDeaths(String p, int num) {
        TStats stats = getTStats(p);
        stats.setDeaths(stats.getDeaths()+num);
    }

    public static TStats getTStats(String p) {
        Storage s = TeamJug.getInstance().getStorage();
        if(s.cacheContains(p, TStats.class)) {
            return s.getObject(p, TStats.class);
        } else {
            s.cacheObject(p, new TStats(p));
            return s.getObject(p, TStats.class);
        }
    }
}
