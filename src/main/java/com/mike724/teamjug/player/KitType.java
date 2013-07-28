package com.mike724.teamjug.player;

public enum KitType {
    WARRIOR("Warrior"),
    GLADIATOR("Gladiator"),
    TANK("Tank"),
    SPY("Spy"),
    MEDIC("Medic");

    private String displayName;

    KitType(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}
