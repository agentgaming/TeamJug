package com.mike724.teamjug;

import com.mike724.networkapi.NetworkPlayer;
import net.minecraft.server.v1_5_R3.EntityLiving;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_5_R3.entity.CraftPlayer;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scoreboard.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Game implements Runnable {

    private Team red;
    private Team blue;
    private List<JPlayer> allPlayers;
    private long ticks;
    private JMap map;

    public Game() {
        this.ticks = 1;
    }

    public void run() {
        TeamJug tj = TeamJug.getInstance();

        //Keep day
        tj.getSpawnMap().getWorld().setTime(1500);
        if (this.map != null) {
            this.map.getWorld().setTime(1500);
        }

        GameState state = tj.getState();

        int numPlayers = tj.getServer().getOnlinePlayers().length;

        if (numPlayers == 0 && state == GameState.LOBBY) {
            ticks = 0l;
            return;
        }

        if (state == GameState.LOBBY) {
            //Make sure we have enough players to start
            if (numPlayers < Constants.MIN_PLAYERS) {
                if (ticks >= Constants.MIN_PLAYERS_START_REPEAT_TIME) {
                    broadcast("A minimum of " + Constants.MIN_PLAYERS + " players are required to start the game.");
                    ticks = 0l;
                }
                ticks++;
                return;
            }

            //Switch to game
            if (ticks >= Constants.LOBBY_TIME) {
                this.startGame();
                return;
            } else if (ticks < Constants.LOBBY_TIME) {
                //Only act if we're on a new second
                if (ticks % 20 == 0) {
                    long seconds = ticks / 20;
                    long totalSecs = Constants.LOBBY_TIME / 20;
                    this.updateAllScoreboards((int) (totalSecs - seconds));
                }
            }
            //Don't continue, increment ticks
            ticks++;
            return;
        } else if (state == GameState.GAME) {
            //Only act if we're on a new second
            if (ticks % 20 == 0) {
                long seconds = ticks / 20;
                long totalSecs = Constants.GAME_TIME / 20;
                this.updateAllScoreboards((int) (totalSecs - seconds));
            }
        }

        //Make sure we have enough players to continue
        if (numPlayers < Constants.MIN_PLAYERS) {
            this.broadcast("A minimum of " + Constants.MIN_PLAYERS + " player(s) are needed to play this game.");
            this.broadcast("Since there are only " + numPlayers + " player(s) online, the game will now end. Sorry!");
            this.endGame();
            return;
        }

        /* WINS SECTION */
        boolean redWins = red.getJugKills() >= Constants.MAX_JUG_KILLS;
        boolean blueWins = blue.getJugKills() >= Constants.MAX_JUG_KILLS;
        boolean timeOver = this.ticks >= Constants.GAME_TIME;

        if (redWins) {
            this.broadcast("The red team has won this game!");
            this.endGame();
            return;
        } else if (blueWins) {
            this.broadcast("The blue team has won this game!");
            this.endGame();
            return;
        } else if (timeOver) {
            this.broadcast("Time is up! Maybe next time...");
            this.endGame();
            return;
        } else {
            //Continue game normally
        }
        /* END WINS SECTION */

        //TODO: Game logic

        // this.broadcast("Reached end of game loop");
        this.ticks++;
    }

    public void startGame() {
        ticks = 0l;
        TeamJug tj = TeamJug.getInstance();
        tj.setState(GameState.GAME);
        tj.getSpawnMap().unloadWorld();
        //Teams
        this.allPlayers = new ArrayList<>();
        for (Player p : tj.getServer().getOnlinePlayers()) {
            this.allPlayers.add(new JPlayer(p.getName()));
        }
        Collections.shuffle((List) allPlayers);
        List<JPlayer> teamRed = new ArrayList<>();
        teamRed.addAll(allPlayers.subList(0, allPlayers.size() / 2 + allPlayers.size() % 2));
        List<JPlayer> teamBlue = new ArrayList<>();
        teamBlue.addAll(allPlayers.subList(allPlayers.size() / 2 + allPlayers.size() % 2, allPlayers.size()));
        this.red = new Team(teamRed);
        this.blue = new Team(teamBlue);
        for (JPlayer jp : teamRed) {
            jp.setTeam(TeamType.RED);
        }
        for (JPlayer jp : teamBlue) {
            jp.setTeam(TeamType.BLUE);
        }
        Object[] maps = tj.getMaps().toArray();
        this.map = (JMap) maps[new Random().nextInt(maps.length)];
        this.broadcast("Loading map " + map.getDispName() + " by " + map.getCreator() + ".");
        map.loadWorld();
        this.broadcast("Map loaded");
        World w = map.getWorld();
        //TODO: Per team spawn
        Location blueSpawn = map.getBlueSpawn().getLocation(w);
        Location redSpawn = map.getRedSpawn().getLocation(w);
        redSpawn.getChunk().load();
        blueSpawn.getChunk().load();
        for (Player p : tj.getServer().getOnlinePlayers()) {
            TeamType team = this.getTeam(p);
            this.giveLoadout(p, team);
            p.teleport(team == TeamType.RED ? redSpawn : blueSpawn);
        }
        this.broadcast("Game started");
    }

    public void endGame() {
        ticks = 0l;
        TeamJug tj = TeamJug.getInstance();
        this.broadcast("The game has ended!");
        this.red = null;
        this.blue = null;
        this.map.unloadWorld();
        tj.getSpawnMap().loadWorld();

        JMap lobbyMap = tj.getSpawnMap();
        World lobbyWorld = lobbyMap.getWorld();
        //Use red spawn as default, both red and blue should be the same (no teams)
        Location loc = lobbyMap.getRedSpawn().getLocation(lobbyWorld);
        if (!loc.getChunk().isLoaded()) {
            loc.getChunk().load();
        }

        for (Player p : tj.getServer().getOnlinePlayers()) {
            this.giveLoadout(p, null);
            p.teleport(loc);
        }

        tj.setState(GameState.LOBBY);
    }

    public JPlayer getPlayer(String name) {
        if (TeamJug.getInstance().getState() == GameState.LOBBY) {
            return null;
        }
        boolean team = red.isPlayerOnTeam(name);
        if (!team) {
            if (blue.isPlayerOnTeam(name)) {
                return blue.getPlayer(name);
            }
        } else {
            return red.getPlayer(name);
        }
        return null;
    }

    public TeamType getTeam(Player p) {
        JPlayer jp = this.getPlayer(p.getName());
        if (jp == null) {
            return null;
        }
        return jp.getTeam();
    }

    public void giveLoadout(Player p, TeamType team) {
        PlayerInventory inv = p.getInventory();
        inv.clear();
        inv.setArmorContents(new ItemStack[4]);
        p.setLevel(0);
        p.setExp(0.0f);
        p.setVelocity(p.getVelocity().zero());
        p.setFallDistance(0f);
        p.setFoodLevel(20);
        p.setHealth(20);
        p.setGameMode(GameMode.ADVENTURE);
        if (team == null) {
            return;
        }
        LeatherArmorMeta lam = (LeatherArmorMeta) (new ItemStack(Material.LEATHER_BOOTS)).getItemMeta();
        lam.setColor(team == TeamType.RED ? Color.RED : Color.BLUE);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        boots.setItemMeta(lam);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leggings.setItemMeta(lam);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        chestplate.setItemMeta(lam);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        helmet.setItemMeta(lam);

        JPlayer jp = this.getPlayer(p.getName());
        if(jp == null) {
            return;
        }
        jp.setJuggernaut(true);
        if(!jp.isJuggernaut()) {
            inv.setBoots(boots);
            inv.setLeggings(leggings);
            inv.setChestplate(chestplate);
            inv.setHelmet(helmet);
            inv.setItem(0, new ItemStack(Material.WOOD_SWORD, 1));
            inv.setItem(1, new ItemStack(Material.GRILLED_PORK, 3));
        } else {
            inv.setHelmet(helmet);
            ItemStack chestplateD = new ItemStack(Material.DIAMOND_CHESTPLATE);
            ItemStack leggingsD = new ItemStack(Material.DIAMOND_LEGGINGS);
            ItemStack bootsD = new ItemStack(Material.DIAMOND_BOOTS);
            inv.setChestplate(chestplateD);
            inv.setLeggings(leggingsD);
            inv.setBoots(bootsD);

            //Reflection to set max health above 20
            try {
                for(Field aField : EntityLiving.class.getFields()) {
                //    System.out.println("field: "+aField.getName());
                }
                Field f = EntityLiving.class.getDeclaredField("health");
                f.setAccessible(true);
                Field f1 = EntityLiving.class.getField("maxHealth");
                try {
                    f1.setInt(p, 200);
                    f.setInt(p, 200);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

            this.broadcast("Health (loadout): "+p.getHealth());
            this.broadcast("Health (loadout max): "+p.getMaxHealth());

            inv.setItem(0, new ItemStack(Material.DIAMOND_SWORD, 1));
        }
    }

    public void updateAllScoreboards(int secs) {
        final long hr = TimeUnit.SECONDS.toHours(secs);
        final long min = TimeUnit.SECONDS.toMinutes(secs - TimeUnit.HOURS.toSeconds(hr));
        final long sec = TimeUnit.SECONDS.toSeconds(secs - TimeUnit.HOURS.toSeconds(hr) - TimeUnit.MINUTES.toSeconds(min));
        String timeLeft = String.format("%02dm:%02ds", min, sec);

        boolean gamePlaying = TeamJug.getInstance().getState() == GameState.GAME;

        for (Player p : TeamJug.getInstance().getServer().getOnlinePlayers()) {
            ScoreboardManager manager = Bukkit.getScoreboardManager();
            Scoreboard board = manager.getNewScoreboard();
            Objective objective = board.registerNewObjective(p.getName(), "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(timeLeft);
            if (gamePlaying) {
                JPlayer jp = this.getPlayer(p.getName());
                Score kills = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Kills: "));
                kills.setScore(jp.getData().getKills());
                Score deaths = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Deaths: "));
                deaths.setScore(jp.getData().getDeaths());
                //   Score jugKills = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GREEN + "Jug Kills: "));
                //   deaths.setScore(jp.getData().getDeaths());
            } else {
                NetworkPlayer np = NetworkPlayers.getNetworkPlayer(p.getName());
                Score tokens = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GRAY + "Tokens:"));
                tokens.setScore(np.getTokens());
                Score cash = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.GOLD + "Cash:"));
                cash.setScore(np.getCash());
            }
            p.setScoreboard(board);
        }
    }

    public void broadcast(String msg) {
        TeamJug.getInstance().getServer().broadcastMessage(ChatColor.BLUE + "[TeamJug] " + ChatColor.GRAY + msg);
    }

    public long getTicks() {
        return ticks;
    }

    public void setTicks(long ticks) {
        this.ticks = ticks;
    }

    public Team getRedTeam() {
        return red;
    }

    public Team getBlueTeam() {
        return blue;
    }

    public JMap getMap() {
        return map;
    }
}
