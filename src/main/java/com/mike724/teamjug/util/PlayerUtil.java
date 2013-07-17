package com.mike724.teamjug.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerUtil {

    public static void clearPlayerCompletely(Player p) {
        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.getActivePotionEffects().clear();
        p.setFoodLevel(20);
        p.setHealth(20);
        p.setVelocity(p.getVelocity().zero());
        p.setFallDistance(0);
        p.setFlying(false);
        p.setExp(0);
        p.setLevel(0);
        p.setExhaustion(0);
        p.setAllowFlight(false);
        p.setSaturation(20);
        p.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

}
