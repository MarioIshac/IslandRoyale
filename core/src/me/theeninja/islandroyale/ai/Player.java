package me.theeninja.islandroyale.ai;

import com.badlogic.gdx.math.Vector3;
import me.theeninja.islandroyale.Inventory;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.IslandTileType;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

import java.util.BitSet;
import java.util.function.Consumer;

import static me.theeninja.islandroyale.gui.screens.MatchScreen.*;

public abstract class Player {
    private final Inventory inventory = new Inventory();
    private final Island mainIsland;
    private final int initialIslandReach;

    public Player(Island mainIsland, int initialIslandReach) {
        this.mainIsland = mainIsland;
        this.initialIslandReach = initialIslandReach;

        exploreInitialTiles();
    }

    private void exploreInitialTiles() {
        // TODO Currently some to-be-explored tiles are explored multiple times because they are in the reach of multiple tiles on the island.
        // We can make this more efficient later, perhaps be incrementing by getInitialIslandReach() instead of 1

        for (int xTileIndex = 0; xTileIndex < getMainIsland().getMaxWidth(); xTileIndex++) {
            for (int yTileIndex = 0; yTileIndex < getMainIsland().getMaxHeight(); yTileIndex++) {
                IslandTileType islandTileType = getMainIsland().getRepr()[xTileIndex][yTileIndex];

                if (islandTileType == null)
                    continue;

                int absoluteXTileIndex = xTileIndex + getMainIsland().getX();
                int absoluteYTileIndex = xTileIndex + getMainIsland().getY();

                for (int xTileOffset = -getInitialIslandReach(); xTileOffset <= getInitialIslandReach(); xTileOffset++) {
                    for (int yTileOffset = -getInitialIslandReach(); yTileOffset <= getInitialIslandReach(); yTileOffset++) {
                        int exploredXTileIndex = absoluteXTileIndex + xTileOffset;
                        int exploredYTileIndex = absoluteYTileIndex + yTileOffset;

                        explore(exploredXTileIndex, exploredYTileIndex);
                    }
                }
            }
        }
    }

    public void sendMessage(String message) {

    }

    public Inventory getInventory() {
        return inventory;
    }

    public Island getMainIsland() {
        return mainIsland;
    }

    /**
     * The function that is called when a player requests a transportation route for one of their
     * transporters (entity that transports). This function will eventually have formed an array of 3d vectors
     * that represent the route the transporter will take. Said array will then be passed to a consumer that must
     * handle it.
     *
     * @param entity The entity whose transportation route is being requested.
     * @param matchMap The match map the entity is contained within.
     */
    public abstract void requestTransportationRoute(
        ControllableEntity<?, ?> entity,
        MatchMap matchMap
    );

    private final BitSet exploredTiles = new BitSet(WHOLE_WORLD_TILE_WIDTH * WHOLE_WORLD_TILE_WIDTH + WHOLE_WORLD_TILE_HEIGHT);

    private int toBitIndex(int xTile, int yTile) {
        int weightedXIndex = xTile * WHOLE_WORLD_TILE_WIDTH;
        return weightedXIndex + yTile;
    }

    public boolean isExplored(int xTile, int yTile) {
        int tileIndex = toBitIndex(xTile, yTile);

        return getExploredTiles().get(tileIndex);
    }

    public void explore(int xTile, int yTile) {
        int tileIndex = toBitIndex(xTile, yTile);

        getExploredTiles().set(tileIndex);
    }

    public BitSet getExploredTiles() {
        return exploredTiles;
    }

    public int getInitialIslandReach() {
        return initialIslandReach;
    }
}
