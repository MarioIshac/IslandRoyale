package me.theeninja.islandroyale;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.treasure.ResourceTreasure;
import me.theeninja.islandroyale.treasure.Treasure;

import java.util.HashMap;
import java.util.Map;

public class MatchMap {
    private final int tileLength;
    private final int tileWidth;

    private static final int ISLAND_GAP = 110;

    private final Map<Island, GridPoint2> islands = new HashMap<>();
    private final Map<Treasure, GridPoint2> treasures = new HashMap<>();

    private final Map<Entity<? extends EntityType>, Vector2> entities = new HashMap<>();

    public void flushDeadEntities() {
        entities.entrySet().removeIf(Entity::isEntityDead);
    }

    private final GridPoint2 focusOrigin;

    public MatchMap(int tileLength, int tileWidth) {
        this.tileLength = tileLength;
        this.tileWidth = tileWidth;

        generateIslands();
        generateTreasures();

        focusOrigin = new GridPoint2(0, 0);
    }

    private void generateIslands() {
        for (int xTile = 0; xTile < getTileWidth(); xTile += ISLAND_GAP) {
            for (int yTile = 0; yTile < getTileLength(); yTile += ISLAND_GAP) {
                Island island = new Island(11, 11);
                GridPoint2 point = new GridPoint2(xTile, yTile);

                getIslands().put(island, point);
            }
        }
    }

    private void generateTreasures() {
        for (int xTile = 0; xTile < getTileWidth(); xTile++) {
            for (int yTile = 0; yTile < getTileLength(); yTile++) {
                if (xTile % 2 == 0 && yTile % 2 == 0) {
                    ResourceTreasure resourceTreasure = new ResourceTreasure(Resource.WOOD, 5);

                    getTreasures().put(resourceTreasure, new GridPoint2(xTile, yTile));
                }
            }
        }
    }

    public int getTileLength() {
        return tileLength;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public Map<Island, GridPoint2> getIslands() {
        return islands;
    }

    public GridPoint2 getFocusOrigin() {
        return focusOrigin;
    }

    public Map<Treasure, GridPoint2> getTreasures() {
        return treasures;
    }

    public Map<Entity<? extends EntityType>, Vector2> getEntities() {
        return entities;
    }
}
