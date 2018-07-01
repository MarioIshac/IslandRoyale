package me.theeninja.islandroyale;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.treasure.ResourceTreasure;
import me.theeninja.islandroyale.treasure.Treasure;

import java.util.*;

public class MatchMap {
    private final int tileWidth;
    private final int tileHeight;

    private static final int ISLAND_GAP = 110;

    private final List<Island> islands = new ArrayList<>();
    private final Map<Treasure, Vector2> treasures = new HashMap<>();
    private final BitSet exploredTiles;

    private int getCorrelatingBit(int x, int y) {
        return x * getTileHeight() + y;
    }

    public Island getIsland(float absoluteX, float absoluteY) {
        for (Island island : getIslands()) {
            boolean xInBounds = island.x <= absoluteX && absoluteX <= island.x + island.getMaxWidth();
            boolean yInBounds = island.y <= absoluteY && absoluteY <= island.y + island.getMaxHeight();

            if (xInBounds && yInBounds)
                return island;
        }

        return null;
    }

    private final Array<Entity<?, ?>> entities = new Array<>();

    public void flushDeadEntities() {


       // entities.removeIf(Entity::shouldRemove);
    }

    public MatchMap(int tileWidth, int tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;

        generateIslands();
        generateTreasures();

        this.exploredTiles = new BitSet(getTileHeight() * getTileHeight());
    }

    private void generateIslands() {
        for (int xTile = 0; xTile < getTileHeight(); xTile += ISLAND_GAP) {
            for (int yTile = 0; yTile < getTileHeight(); yTile += ISLAND_GAP) {
                Island island = new Island(11, 11, xTile, yTile);

                getIslands().add(island);
            }
        }
    }

    private void generateTreasures() {
        for (int xTile = 0; xTile < getTileHeight(); xTile++) {
            for (int yTile = 0; yTile < getTileHeight(); yTile++) {
                if (xTile % 2 == 0 && yTile % 2 == 0) {
                    Treasure resourceTreasure = new ResourceTreasure(Resource.WOOD, 5);

                    getTreasures().put(resourceTreasure, new Vector2(xTile, yTile));
                }
            }
        }
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public Map<Treasure, Vector2> getTreasures() {
        return treasures;
    }

    public Array<Entity<?, ?>> getEntities() {
        return entities;
    }

    public boolean isExplored(int x, int y) {
        return getExploredTiles().get(getCorrelatingBit(x, y));
    }

    public void explore(int x, int y) {
        getExploredTiles().set(getCorrelatingBit(x, y));
    }

    public BitSet getExploredTiles() {
        return exploredTiles;
    }

    public int getTileWidth() {
        return tileWidth;
    }
}
