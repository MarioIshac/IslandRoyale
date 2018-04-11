package me.theeninja.islandroyale;

import com.badlogic.gdx.math.GridPoint2;
import me.theeninja.islandroyale.treasure.Treasure;

import java.util.HashMap;
import java.util.Map;

public class MatchMap {
    private final int tileLength;
    private final int tileWidth;

    private static final int ISLAND_GAP = 110;

    private final Map<Island, GridPoint2> islands = new HashMap<>();
    private final Map<Treasure, GridPoint2> treasures = new HashMap<>();

    private final GridPoint2 bottomLeft;

    public MatchMap(int tileLength, int tileWidth) {
        this.tileLength = tileLength;
        this.tileWidth = tileWidth;

        generateIslands();
        generateTreasures();

        bottomLeft = new GridPoint2(0, 0);
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

    public GridPoint2 getBottomLeft() {
        return bottomLeft;
    }

    public Map<Treasure, GridPoint2> getTreasures() {
        return treasures;
    }
}
