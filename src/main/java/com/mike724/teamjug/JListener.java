package com.mike724.teamjug;

import com.mike724.networkapi.DataStorage;
import com.mike724.networkapi.NetworkPlayer;
import com.mike724.networkapi.NetworkRank;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public class JListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHealthRegen(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRightClick(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getItem() != null && event.getItem().getType().isEdible()) {
                Player p = event.getPlayer();
                if (p.getHealth() >= p.getMaxHealth()) {
                    return;
                }
                int amt = 4;
                int curHealth = p.getHealth();
                int newHealth = curHealth + amt;
                if (newHealth > p.getMaxHealth()) {
                    p.setHealth(p.getMaxHealth());
                } else {
                    p.setHealth(newHealth);
                }
                ItemStack inHand = p.getItemInHand();
                int newAmt = inHand.getAmount() - 1;
                if (newAmt <= 0) {
                    p.setItemInHand(new ItemStack(Material.AIR));
                } else {
                    inHand.setAmount(newAmt);
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDestroyBlock(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPlaceBlock(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRightClickInventory(InventoryOpenEvent event) {
        if (event.getInventory().getType() != InventoryType.PLAYER) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onHungerChange(FoodLevelChangeEvent event) {
        HumanEntity ent = event.getEntity();
        if(!(ent instanceof Player)) {
            return;
        }
        JPlayer jp = TeamJug.getInstance().getGame().getPlayer(ent.getName());
        if(jp == null) {
            event.setFoodLevel(20);
            return;
        }
        if(jp.isJuggernaut()) {
            event.setFoodLevel(6);
        } else {
            event.setFoodLevel(20);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.ARMOR) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDeath(PlayerDeathEvent event) {
        final Location deathLoc = event.getEntity().getEyeLocation();
        event.getEntity().setHealth(20);
        event.getDrops().clear();
        event.setDroppedExp(0);
        Player killer = event.getEntity().getKiller();
        final Player p = event.getEntity();
        JPlayer jp = TeamJug.getInstance().getGame().getPlayer(p.getName());
        boolean wasJug = jp.isJuggernaut();
        jp.setJuggernaut(false);
        final Game game = TeamJug.getInstance().getGame();
        if (killer != null) {
            event.setDeathMessage(event.getEntity().getName() + " was killed by " + killer.getName());
            JPlayer jp1 = TeamJug.getInstance().getGame().getPlayer(killer.getName());
            if(wasJug) {
                Team t = jp1.getTeamObject();
                if(t != null) {
                    t.setJugKills(t.getJugKills()+1);
                }
            }
            jp1.getData().setKills(jp1.getData().getKills() + 1);

            //juggernaut
            Team t = null;
            TeamType tt = jp1.getTeam();
            if(tt == TeamType.RED) {
                t = game.getRedTeam();
            } else if(tt == TeamType.BLUE) {
                t = game.getBlueTeam();
            }
            if(t != null) {
                if(!t.isJuggernautSet()) {
                    t.setNewJuggernaut();
                    game.broadcast("debug: set new jug");
                }
            }
        } else {
            event.setDeathMessage(event.getEntity().getName() + " died");
        }
        final TeamJug tj = TeamJug.getInstance();
        final TeamType team = game.getTeam(p);
        if (team == null) {
            return;
        }
        jp.getData().setDeaths(jp.getData().getDeaths() + 1);
        JMap curMap = game.getMap();
        World curWorld = curMap.getWorld();
        final Location loc;
        if (team == TeamType.RED) {
            loc = curMap.getRedSpawn().getLocation(curWorld);
        } else {
            loc = curMap.getBlueSpawn().getLocation(curWorld);
        }
        System.out.println("team null check: " + (team == null));
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
                game.giveLoadout(p, team);
                tj.safeTP(p, loc);
            }
        });
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        System.out.println("Player respawn event");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHurt(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        TeamJug tj = TeamJug.getInstance();
        if (tj.getState() == GameState.LOBBY) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player && event.getDamager() instanceof Player)) {
            return;
        }
        TeamJug tj = TeamJug.getInstance();
        if (tj.getState() == GameState.LOBBY) {
            event.setCancelled(true);
            return;
        }
        Player noob = (Player) event.getEntity();
        Player dick = (Player) event.getDamager();

        dick.getItemInHand().setDurability((short)0);

        for(ItemStack is : noob.getInventory().getArmorContents()) {
            is.setDurability((short)0);
        }

        if (tj.getGame().getTeam(noob) == tj.getGame().getTeam(dick)) {
            event.setCancelled(true);
        }

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
        TeamJug tj = TeamJug.getInstance();
        Player p = event.getPlayer();

        NetworkPlayer np = NetworkPlayers.getNetworkPlayer(event.getPlayer().getName());
        //np.updateWallet();

        if (tj.getState() == GameState.LOBBY) {
            JMap lobbyMap = tj.getSpawnMap();
            World lobbyWorld = lobbyMap.getWorld();
            //Use red spawn as default, both red and blue should be the same (no teams)
            Location loc = lobbyMap.getRedSpawn().getLocation(lobbyWorld);
            tj.safeTP(p, loc);
            tj.getGame().giveLoadout(p, null);
            p.teleport(loc);
        } else if (tj.getState() == GameState.GAME) {
            Game game = tj.getGame();
            String name = event.getPlayer().getName();

            //Determine team
            Team red = game.getRedTeam();
            Team blue = game.getBlueTeam();
            JPlayer jp = new JPlayer(name);
            TeamType team = TeamType.BLUE;
            if (blue.getMembers().size() < red.getMembers().size()) {
                blue.addPlayer(name, jp);
            } else {
                red.addPlayer(name, jp);
                team = TeamType.RED;
            }
            jp.setTeam(team);
            game.giveLoadout(event.getPlayer(), team);

            JMap curMap = game.getMap();
            World curWorld = curMap.getWorld();
            Location loc = null;
            if (team == TeamType.RED) {
                loc = curMap.getRedSpawn().getLocation(curWorld);
            } else {
                loc = curMap.getBlueSpawn().getLocation(curWorld);
            }
            if (loc == null) {
                loc = new Location(curWorld, 0, 256, 0);
            }
            tj.safeTP(event.getPlayer(), loc);
        }
        tj.getGame().broadcast("Welcome " + p.getName() + "!");
        event.setJoinMessage("");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLogin(PlayerLoginEvent event) {
        TeamJug tj = TeamJug.getInstance();
        DataStorage ds = tj.getDataStorage();

        String name = event.getPlayer().getName();
        Boolean check = true;
        NetworkPlayer np = (NetworkPlayer) ds.getObject(NetworkPlayer.class, name);
        if (np == null) {
            np = new NetworkPlayer(event.getPlayer().getName(), 0, 0, false, true, NetworkRank.USER);
            ds.writeObject(np, event.getPlayer().getName());
            NetworkPlayers.addNetworkPlayer(np);
            check = false;
        }

        if (check) {
            if (np.isOnline()) {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are already playing on the network!");
                return;
            }

            if (np.isBanned()) {
                event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "You are banned from the network!");
                return;
            }

            np.setOnline(true);
            NetworkPlayers.addNetworkPlayer(np);
            ds.writeObject(np, event.getPlayer().getName());
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerQuit(PlayerQuitEvent event) {
        TeamJug tj = TeamJug.getInstance();
        DataStorage ds = tj.getDataStorage();

        NetworkPlayer np = NetworkPlayers.getNetworkPlayer(event.getPlayer().getName());
        np.setOnline(false);
        ds.writeObject(np, event.getPlayer().getName());

        Game game = tj.getGame();
        TeamType team = game.getTeam(event.getPlayer());
        if (team == TeamType.BLUE) {
            game.getBlueTeam().removePlayer(event.getPlayer().getName());
        } else if (team == TeamType.RED) {
            game.getRedTeam().removePlayer(event.getPlayer().getName());
        } else {
            //Lobby
        }

        event.setQuitMessage("");
        game.broadcast("Goodbye, " + event.getPlayer().getName() + "!");

        NetworkPlayers.removeNetworkPlayer(event.getPlayer().getName());
    }
}
