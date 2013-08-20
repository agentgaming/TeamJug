package com.mike724.teamjug.game;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.enviro.BaseListener;
import com.mike724.teamjug.stats.StatsController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;

@SuppressWarnings("unused")
public class GameListener extends BaseListener {

    private Game game;

    public GameListener(Game game) {
        this.game = game;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        //Remove drops and crap
        event.getDrops().clear();
        event.setDroppedExp(0);

        //"cancel" the event
        event.getEntity().setHealth(20);
        event.getEntity().setFallDistance(0);

        Player p = event.getEntity();
        final Location deathLoc = p.getEyeLocation();

        EntityDamageEvent ede = event.getEntity().getLastDamageCause();
        if(!(ede instanceof EntityDamageByEntityEvent)) {
            event.setDeathMessage(ChatColor.ITALIC + "Player " + p.getName() + " died");
            this.handleDeath(p, deathLoc);
            return;
        }

        EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent)ede;
        Entity damager = edbee.getDamager();
        Player killer;
        if(damager instanceof Player) {
            killer = (Player)damager;
        } else if(damager instanceof Arrow) {
            killer = (Player)((Arrow) damager).getShooter();
        } else {
            //Not sure how to handle yet, output debug
            event.setDeathMessage("Player "+p.getName()+" killed by "+damager.getClass().getName());
            this.handleDeath(p, deathLoc);
            return;
        }

        //Suicide? Idk how this would happen. Shooting arrows in the sky?
        if(p == killer) {
            event.setDeathMessage(ChatColor.ITALIC+"Player "+p.getName()+" committed suicide");
            this.handleDeath(p, deathLoc);
            return;
        }

        //Add a kill
        StatsController.addKills(killer.getName(), 1);

        event.setDeathMessage(ChatColor.ITALIC+"Player " + p.getName() + " was killed by "+killer.getName());

        this.handleDeath(p, deathLoc);
    }


    //Particles, re-spawns player, adds death to count
    private void handleDeath(final Player p, final Location deathLoc) {
        StatsController.addDeaths(p.getName(), 1);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(TeamJug.getInstance(), new Runnable() {
            @Override
            public void run() {
                Location newLoc1 = deathLoc.clone();
                newLoc1.setY(newLoc1.getY()-1);
                Location newLoc2 = deathLoc.clone();
                newLoc2.setY(newLoc2.getY()+1);
                newLoc1.getWorld().playEffect(newLoc1, Effect.SMOKE, 4);
                deathLoc.getWorld().playEffect(deathLoc, Effect.SMOKE, 4);
                newLoc2.getWorld().playEffect(newLoc2, Effect.SMOKE, 4);
                game.handlePlayer(p, game.getPlayerTeamType(p));
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent edbee = (EntityDamageByEntityEvent)event;
            if(edbee.getEntity() instanceof Player) {
                Player p = (Player)edbee.getEntity();
                Entity damager = edbee.getDamager();
                if(damager instanceof Player) {
                    Player killer = (Player)damager;
                    if(game.getPlayerTeamType(p) == game.getPlayerTeamType(killer)) {
                        TeamJug.errorMessage("Damage cancelled");
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerOpenInventory(InventoryOpenEvent event) {
        if (event.getInventory().getType() != InventoryType.PLAYER) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        game.addPlayerAfterStart(p);
    }

}
