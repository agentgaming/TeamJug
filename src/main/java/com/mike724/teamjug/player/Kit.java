package com.mike724.teamjug.player;

import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

public class Kit {

    private final KitType type;
    private Material chestplate, pants, shoes;
    private Material primaryWeapon;
    private PotionEffect[] potionEffects;

    public Kit(KitType type) {
        this.type = type;
    }

    public KitType getType() {
        return type;
    }

    public Material getChestplate() {
        return chestplate;
    }

    public void setChestplate(Material chestplate) {
        this.chestplate = chestplate;
    }

    public Material getPants() {
        return pants;
    }

    public void setPants(Material pants) {
        this.pants = pants;
    }

    public Material getShoes() {
        return shoes;
    }

    public void setShoes(Material shoes) {
        this.shoes = shoes;
    }

    public Material getPrimaryWeapon() {
        return primaryWeapon;
    }

    public void setPrimaryWeapon(Material primaryWeapon) {
        this.primaryWeapon = primaryWeapon;
    }

    public PotionEffect[] getPotionEffects() {
        return potionEffects;
    }

    public void setPotionEffects(PotionEffect[] potionEffects) {
        this.potionEffects = potionEffects;
    }
}
