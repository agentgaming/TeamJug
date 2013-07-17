package com.mike724.teamjug.game;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.enviro.BaseListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
        final Location deathLoc = event.getEntity().getEyeLocation();
        final Player p = event.getEntity();
        p.setHealth(20);
        event.getDrops().clear();
        event.setDroppedExp(0);
        event.setDeathMessage("");
        Bukkit.broadcastMessage(ChatColor.ITALIC + "Player " + p.getName() + " died");
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
