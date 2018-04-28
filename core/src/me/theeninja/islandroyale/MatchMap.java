package me.theeninja.islandroyale;

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

    private final Map<Island, Vector2> islands = new HashMap<>();
    private final Map<Treasure, Vector2> treasures = new HashMap<>();

    private final Map<Entity<? extends EntityType<?>>, Vector2> entities = new HashMap<>();
    private final boolean[][] exploredTiles;

    public void flushDeadEntities() {
        entities.entrySet().removeIf(Entity::isEntityDead);
    }

    private final Vector2 focusOrigin;

    public Vector2 absoluteToRelativeTile(Vector2 absolute) {
        return absolute.cpy().sub(getFocusOrigin());
    }

    public MatchMap(int tileLength, int tileWidth) {
        this.tileLength = tileLength;
        this.tileWidth = tileWidth;

        generateIslands();
        generateTreasures();

        this.focusOrigin = new Vector2(0, 0);

        this.exploredTiles = new boolean[tileLength][tileWidth];
    }

    private void exploreCenterTiles(int tilesEachDir) {
        int xTileStart = (int) (getFocusOrigin().x - tilesEachDir);
        int yTileStart = (int) (getFocusOrigin().y - tilesEachDir);

        int xTileEnd = (int) (getFocusOrigin().x + tilesEachDir);
        int yTileEnd = (int) (getFocusOrigin().y + tilesEachDir);

        for (int xTile = xTileStart; xTile <= xTileEnd; xTile++)
            for (int yTile = yTileStart; yTile <= yTileEnd; yTile++)
                getExploredTiles()[xTile][yTile] = true;
    }

    private void generateIslands() {
        for (int xTile = 0; xTile < getTileWidth(); xTile += ISLAND_GAP) {
            for (int yTile = 0; yTile < getTileLength(); yTile += ISLAND_GAP) {
                Island island = new Island(11, 11);
                Vector2 point = new Vector2(xTile, yTile);

                getIslands().put(island, point);
            }
        }
    }

    private void generateTreasures() {
        for (int xTile = 0; xTile < getTileWidth(); xTile++) {
            for (int yTile = 0; yTile < getTileLength(); yTile++) {
                if (xTile % 2 == 0 && yTile % 2 == 0) {
                    ResourceTreasure resourceTreasure = new ResourceTreasure(Resource.WOOD, 5);

                    getTreasures().put(resourceTreasure, new Vector2(xTile, yTile));
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

    public Map<Island, Vector2> getIslands() {
        return islands;
    }

    public Vector2 getFocusOrigin() {
        return focusOrigin;
    }

    public Map<Treasure, Vector2> getTreasures() {
        return treasures;
    }

    public Map<Entity<? extends EntityType<?>>, Vector2> getEntities() {
        return entities;
    }

    public boolean[][] getExploredTiles() {
        return exploredTiles;
    }
}
