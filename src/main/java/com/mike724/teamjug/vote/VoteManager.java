package com.mike724.teamjug.vote;

import com.mike724.teamjug.TeamJug;
import com.mike724.teamjug.enviro.GameMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.*;

import java.util.*;

@SuppressWarnings("unused")
public class VoteManager {

    private LinkedHashMap<Integer, GameMap> maps;
    private HashMap<Integer, Integer> votes;
    private ArrayList<String> voted;
    private boolean voteLocked = false;
    private final int NUM_OPTIONS = 2;
    private Inventory voteInv;
    private Scoreboard scoreboard;

    public VoteManager() {
        maps = new LinkedHashMap<>();
        votes = new HashMap<>();
        voted = new ArrayList<>();
        voteInv = Bukkit.createInventory(null, 9, "Enter your vote");
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    }

    public void setup() {
        TeamJug tj = TeamJug.getInstance();
        List<GameMap> options = tj.getMapManager().getMaps();
        Collections.shuffle(options);
        if(options.size() < NUM_OPTIONS) {
            TeamJug.errorMessage("NOT ENOUGH MAP OPTIONS FOR VOTE");
            return;
        }
        for(int i = 0; i < NUM_OPTIONS; i++) {
            maps.put(i, options.get(i));
            votes.put(i, 0);
        }
        for(final GameMap map : maps.values()) {
            ItemStack mapItem = new ItemStack(Material.EMPTY_MAP, 1);
            ItemMeta meta = mapItem.getItemMeta();
            meta.setDisplayName(map.getDisplayName());
            //Probably shouldn't use this style in production code... do I care? NOPE.
            meta.setLore(new ArrayList<String>() {{ add("Created by:"); addAll(Arrays.asList(map.getCreators())); }});
            mapItem.setItemMeta(meta);
            voteInv.addItem(mapItem);
        }

        //Scoreboard
        Objective objective = scoreboard.registerNewObjective("votes", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("Votes");
        this.updateVoteBoard();
    }

    public void openVotingScreen(Player p) {
        p.openInventory(voteInv);
    }

    public void showVoteBoard(Player p) {
        p.setScoreboard(scoreboard);
    }

    public boolean isVotingInventory(Inventory inv) {
        return inv.getName() == voteInv.getName() && inv.getSize() == voteInv.getSize();
    }

    public void submitVote(Player p, Vote v) {
        if(isVoteLocked()) {
            p.sendMessage(ChatColor.RED+"Voting is locked");
            return;
        } else if(voted.contains(p.getName())) {
            p.sendMessage(ChatColor.RED+"You have already voted");
            return;
        }
        int mapID = v.getMapID();
        if(maps.containsKey(mapID)) {
            int numVotes = votes.get(mapID);
            votes.put(mapID, numVotes + v.getWeight());
            voted.add(p.getName());
            p.sendMessage(ChatColor.GREEN+"Vote for map "+ChatColor.YELLOW+maps.get(mapID).getDisplayName()+ChatColor.GREEN+" added");
            updateVoteBoard();
        }
    }

    public void updateVoteBoard() {
        Objective objective = scoreboard.getObjective("votes");
        for(Map.Entry<Integer, GameMap> entry : maps.entrySet()) {
            int numVotes = 0;
            if(votes.containsKey(entry.getKey())) {
                numVotes = votes.get(entry.getKey());
            }
            String name = entry.getValue().getDisplayName();
            Score voteScore = objective.getScore(Bukkit.getOfflinePlayer(name));
            voteScore.setScore(numVotes);
        }
    }

    public GameMap getWinningMap() {
        this.voteLocked = true;
        int max = -1;
        for(Map.Entry<Integer, Integer> entry : votes.entrySet()) {
            int mapID = entry.getKey();
            int votes = entry.getValue();
            if(votes > max) {
                max = votes;
            }
        }

        List<GameMap> wins = new ArrayList<>();
        //Find any ties
        for(Map.Entry<Integer, Integer> entry : votes.entrySet()) {
            if(entry.getValue() == max) {
                wins.add(maps.get(entry.getKey()));
            }
        }

        GameMap winner = null;
        int numWins = wins.size();
        if(numWins == 1) {
            winner = wins.get(0);
            this.broadcast("The map "+ChatColor.YELLOW+winner.getDisplayName()+ChatColor.WHITE+" has won!");
        } else if(numWins > 1) {
            //TIE!!!
            this.broadcast("It was a tie! A map will randomly be selected to break the tie");
            int winID = new Random().nextInt(numWins);
            winner = wins.get(winID);
            this.broadcast("The map "+ChatColor.YELLOW+winner.getDisplayName()+ChatColor.WHITE+" has been selected!");
        } else {
            //what
        }
        return winner;
    }

    public void broadcast(String msg) {
        TeamJug.getInstance().getServer().broadcastMessage(ChatColor.DARK_RED+""+ChatColor.BOLD+"[TeamJug] "+ChatColor.WHITE+msg);
    }

    public boolean isVoteLocked() {
        return voteLocked;
    }

    public void setVoteLocked(boolean voteLocked) {
        this.voteLocked = voteLocked;
    }
}
