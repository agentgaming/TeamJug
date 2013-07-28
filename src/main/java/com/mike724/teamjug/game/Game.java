package com.mike724.teamjug.game;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.enviro.GameMap;
import com.mike724.teamjug.teams.TeamManager;
import com.mike724.teamjug.teams.TeamType;
import com.mike724.teamjug.timing.TimeConstants;
import com.mike724.teamjug.timing.Timer;
import com.mike724.teamjug.util.PlayerUtil;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class Game {

    private TeamManager tm;
    private Timer endGameTimer;
    private Location redSpawn;
    private Location blueSpawn;
    private World world;
    private GameListener listener;

    public Game(TeamManager tm, GameMap map) {

        this.tm = tm;
        //Register timer for game end
        TeamJug tj = TeamJug.getInstance();
        this.endGameTimer = new Timer(TimeConstants.GAME_MAX_TIME, false, this, "_endTimeOver");
        tj.getTimeManager().addTimer(endGameTimer);
        this.broadcast("Game session started on map " + map.getDisplayName());

        //Teleport players
        world = tj.getMapManager().getCurrentWorld();
        redSpawn = map.getRedSpawns()[0].getBukkitLocation(world);
        blueSpawn = map.getBlueSpawns()[0].getBukkitLocation(world);

        if (redSpawn == null || blueSpawn == null) {
            this.broadcast("NULL LOCATION");
        }

        for (Player p : tm.getRedTeam().getPlayers()) {
            p.sendMessage(ChatColor.RED + "" + ChatColor.ITALIC + "You are on the red team.");
            handlePlayer(p, TeamType.RED);
        }
        for (Player p : tm.getBlueTeam().getPlayers()) {
            p.sendMessage(ChatColor.BLUE + "" + ChatColor.ITALIC + "You are on the blue team.");
            handlePlayer(p, TeamType.BLUE);
        }

        //Register listener
        listener = new GameListener(this);
        tj.getServer().getPluginManager().registerEvents(listener, tj);
    }

    //Don't worry little guy, I'll get you into the game
    public void addPlayerAfterStart(Player p) {
        p.sendMessage(ChatColor.GRAY + "Welcome to the game!");
        TeamType tt = tm.addPlayerToAnyTeam(p);
        this.handlePlayer(p, tt);
    }

    public void handlePlayer(Player p, TeamType tt) {
        PlayerUtil.clearPlayerCompletely(p);
        p.setGameMode(GameMode.ADVENTURE);
        switch (tt) {
            case RED:
                p.teleport(redSpawn);
                break;
            case BLUE:
                p.teleport(blueSpawn);
                break;
            default:
                break;
        }
        //TEMPORARY!!!!! WILL
        LeatherArmorMeta lam = (LeatherArmorMeta) (new ItemStack(Material.LEATHER_BOOTS)).getItemMeta();
        lam.setColor(tt == TeamType.RED ? Color.RED : Color.BLUE);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);
        boots.setItemMeta(lam);
        ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
        leggings.setItemMeta(lam);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        chestplate.setItemMeta(lam);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        helmet.setItemMeta(lam);
        PlayerInventory inv = p.getInventory();
        inv.setBoots(boots);
        inv.setLeggings(leggings);
        inv.setChestplate(chestplate);
        inv.setHelmet(helmet);
        inv.setItem(0, new ItemStack(Material.WOOD_SWORD, 1));

        ItemStack infinityBow = new ItemStack(Material.BOW, 1);
        infinityBow.addEnchantment(Enchantment.ARROW_INFINITE, 1);

        inv.setItem(1, infinityBow);

        inv.setItem(2, new ItemStack(Material.ARROW, 1));

        inv.setItem(3, new ItemStack(Material.GRILLED_PORK, 3));
    }

    public void onTick(long ticks) {
        TeamJug tj = TeamJug.getInstance();
        List<Player> all = tm.getAllPlayers();

        //Time bar
        long timeLeft = tj.getTimeManager().getTimeLeft(endGameTimer);
        long totalTime = endGameTimer.getTime();
        float percent = (float) ((double) timeLeft / (double) totalTime);
        for (Player p : all) {
            p.setExp(percent);
        }
    }

    public void _endTimeOver() {
        this.end(GameEndReason.TIME_OVER);
    }

    public void end(GameEndReason reason) {
        //Kill the time over timer
        if (reason != GameEndReason.TIME_OVER) {
            endGameTimer.setKillFlag(true);
        }

        String bcast = "";
        switch (reason) {
            case TIME_OVER:
                bcast = "Time has run out!";
                break;
            case RED_WIN:
                bcast = "The " + ChatColor.RED + "RED" + ChatColor.WHITE + " team has won!";
                break;
            case BLUE_WIN:
                bcast = "The " + ChatColor.BLUE + "BLUE" + ChatColor.WHITE + " team has won!";
                break;
            case MIN_PLAYERS:
                bcast = "A minimum of " + ChatColor.YELLOW + GameConstants.MIN_PLAYERS + ChatColor.WHITE + " player(s) are required.";
        }
        this.broadcast(ChatColor.ITALIC + bcast);
        this.broadcast("Game ending...");
        tm.emptyTeams();

        HandlerList.unregisterAll(listener);
        TeamJug.getInstance().getGameManager().startLobby();
    }

    public TeamType getPlayerTeamType(Player p) {
        if(this.tm.getRedTeam().getPlayers().contains(p)) {
            return TeamType.RED;
        } else if(this.tm.getBlueTeam().getPlayers().contains(p)) {
            return TeamType.BLUE;
        }
        return null;
    }

    public void broadcast(String msg) {
        TeamJug.getInstance().getServer().broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "[TeamJug] " + ChatColor.WHITE + msg);
    }

}
