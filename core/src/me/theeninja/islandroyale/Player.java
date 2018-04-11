package me.theeninja.islandroyale;

public class Player {
    private final Inventory inventory = new Inventory();
    private final Island mainIsland = new Island(11, 11);

    public Inventory getInventory() {
        return inventory;
    }

    public Island getMainIsland() {
        return mainIsland;
    }
}
