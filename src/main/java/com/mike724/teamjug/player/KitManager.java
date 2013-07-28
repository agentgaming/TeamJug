package com.mike724.teamjug.player;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class KitManager {

    private HashMap<KitType, Kit> kits;

    public KitManager() {
        kits = new HashMap<>();
    }

    private void addDefaultKits() {
        Kit warrior = new Kit(KitType.WARRIOR);
        warrior.setPrimaryWeapon(Material.IRON_SWORD);
        warrior.setChestplate(Material.IRON_CHESTPLATE);
        warrior.setPants(Material.IRON_LEGGINGS);
        warrior.setShoes(Material.CHAINMAIL_BOOTS);
        warrior.setPotionEffects(new PotionEffect[] {
                getPotionEffect(PotionEffectType.REGENERATION, 1),
                getPotionEffect(PotionEffectType.SPEED, 1)
        });
        this.addKit(KitType.WARRIOR, warrior);

        Kit gladiator = new Kit(KitType.GLADIATOR);
        gladiator.setPrimaryWeapon(Material.DIAMOND_SWORD);
        gladiator.setChestplate(Material.LEATHER_CHESTPLATE);
        gladiator.setPants(Material.LEATHER_LEGGINGS);
        gladiator.setShoes(Material.LEATHER_BOOTS);
        gladiator.setPotionEffects(new PotionEffect[] {
                getPotionEffect(PotionEffectType.SPEED, 1),
                getPotionEffect(PotionEffectType.INCREASE_DAMAGE, 1)
        });
        this.addKit(KitType.GLADIATOR, gladiator);

        Kit tank = new Kit(KitType.TANK);
        tank.setPrimaryWeapon(Material.WOOD_SWORD);
        
    }

    //Returns potion effect with "unlimited" duration and ambient
    public PotionEffect getPotionEffect(PotionEffectType pet, int lvl) {
        return new PotionEffect(pet, 2400, lvl, true);
    }


    public void addKit(KitType type, Kit kit) {
        kits.put(type, kit);
    }

}
