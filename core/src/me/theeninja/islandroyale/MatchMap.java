package me.theeninja.islandroyale;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import me.theeninja.islandroyale.building.Building;
import me.theeninja.islandroyale.treasure.ResourceTreasure;
import me.theeninja.islandroyale.treasure.Treasure;

import java.util.HashMap;
import java.util.Map;

public class MatchMap {
    private final int tileLength;
    private final int tileWidth;

    private static final int ISLAND_GAP = 110;

    private final Map<Island, Vector3> islands = new HashMap<>();
    private final Map<Treasure, Vector3> treasures = new HashMap<>();

    private final Vector3 bottomLeft;

    public MatchMap(int tileLength, int tileWidth) {
        this.tileLength = tileLength;
        this.tileWidth = tileWidth;

        generateIslands();
        generateTreasures();

        bottomLeft = new Vector3(0, 0, 0);
    }

    private void generateIslands() {
        for (int xTile = 0; xTile < getTileWidth(); xTile += ISLAND_GAP) {
            for (int yTile = 0; yTile < getTileLength(); yTile += ISLAND_GAP) {
                Island island = new Island(11, 11);
                Vector3 point = new Vector3(xTile, yTile, 0);

                getIslands().put(island, point);
            }
        }
    }

    private void generateTreasures() {
        for (int xTile = 0; xTile < getTileWidth(); xTile++) {
            for (int yTile = 0; yTile < getTileLength(); yTile++) {
                if (xTile % 2 == 0 && yTile % 2 == 0) {
                    ResourceTreasure resourceTreasure = new ResourceTreasure(Resource.WOOD, 5);

                    getTreasures().put(resourceTreasure, new Vector3(xTile, yTile, 0));
                }
            }
        }
    }

    private boolean canBuild(Building building, int x, int y) {
        for (Map.Entry<Island, Vector3> entry : getIslands().entrySet()) {
            Island island = entry.getKey();
            Vector3 location = entry.getValue();

            int relativeToIslandX = (int) location.x - x;
            int relativeToIslandY = (int) location.y - y;

            boolean xInIsland = 0 <= relativeToIslandX && relativeToIslandX < island.getMaxWidth();
            boolean yInIsland = 0 <= relativeToIslandY && relativeToIslandY < island.getMaxHeight();

            if (!(xInIsland && yInIsland))
                continue;

            int groundTiles = 0;

            for (int xIndex = 0; xIndex < building.getTileWidth(); xIndex++)
                for (int yIndex = 0; yIndex < building.getTileWidth(); yIndex++)
                    if (island.getRepr()[relativeToIslandX + xIndex][relativeToIslandY + yIndex] != null)
                        groundTiles++;

            if (groundTiles >= building.getMinGroundTiles())
                return true;
        }

        return false;
    }

    private void build(Building building, int x, int y) {
        for (Map.Entry<Island, Vector3> entry : getIslands().entrySet()) {
            Island island = entry.getKey();
            Vector3 location = entry.getValue();

            int relativeToIslandX = (int) location.x - x;
            int relativeToIslandY = (int) location.y - y;

            boolean xInIsland = 0 <= relativeToIslandX && relativeToIslandX < island.getMaxWidth();
            boolean yInIsland = 0 <= relativeToIslandY && relativeToIslandY < island.getMaxHeight();

            if (!(xInIsland && yInIsland))
                continue;


        }
    }

    public int getTileLength() {
        return tileLength;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public Map<Island, Vector3> getIslands() {
        return islands;
    }

    public Vector3 getBottomLeft() {
        return bottomLeft;
    }

    public Map<Treasure, Vector3> getTreasures() {
        return treasures;
    }
}
