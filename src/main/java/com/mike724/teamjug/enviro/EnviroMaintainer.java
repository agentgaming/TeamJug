package com.mike724.teamjug.enviro;

import com.mike724.teamjug.TeamJug;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/** This class is a Bukkit Listener.
 *  It's job is to maintain the server environment
 *  Including weather, time, mobs, etc.
 */
@SuppressWarnings("unused")
public class EnviroMaintainer implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onWeatherChange(WeatherChangeEvent event) {
        //No weather on ANY world
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        //No mobs/monsters may spawn
        event.setCancelled(true);
    }

    public void onTick(long ticks) {
        TeamJug tj = TeamJug.getInstance();

        //For all worlds set time to 1500/day
        for(World w : tj.getServer().getWorlds()) {
            w.setTime(1500);
        }
    }


}
