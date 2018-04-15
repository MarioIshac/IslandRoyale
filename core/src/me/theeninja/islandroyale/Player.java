package me.theeninja.islandroyale;

public class Player {
    private final Inventory inventory = new Inventory();
    private final Island mainIsland;

    public Player(Island island) {
        this.mainIsland = island;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Island getMainIsland() {
        return mainIsland;
    }
}
