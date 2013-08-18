package com.mike724.teamjug.lobby;

import com.mike724.teamjug.enviro.BaseListener;
import com.mike724.teamjug.vote.Vote;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@SuppressWarnings("unused")
public class LobbyListener extends BaseListener {

    private Lobby lobby;

    public LobbyListener(Lobby lobby) {
        this.lobby = lobby;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        if(lobby.getVoteManager().isVotingInventory(event.getInventory())) {
            int mapID = event.getRawSlot();
            int weight = 1;
            lobby.getVoteManager().submitVote((Player)event.getWhoClicked(), new Vote(mapID, weight));
        }
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            lobby.getVoteManager().openVotingScreen(event.getPlayer());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerOpenInventory(InventoryOpenEvent event) {
        if(!lobby.getVoteManager().isVotingInventory(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        lobby.handlePlayer(p);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if(event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

}
