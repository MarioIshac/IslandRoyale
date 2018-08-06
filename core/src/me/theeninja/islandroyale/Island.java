package me.theeninja.islandroyale;

import com.badlogic.gdx.math.MathUtils;

import java.util.*;

import com.badlogic.gdx.utils.IntArray;
import me.theeninja.islandroyale.entity.building.BuildingType;

public class Island {
    private IslandTileType[][] repr;

    public static final int BLOCK_MULTIPLIER = 2;
    private static final float NON_SCALED_CENTER_BLOCKS = (float) Math.sqrt(2);
    private static final int SURROUNDING_BLOCKS_REQUIRED = 2;

    public int getX() {
        return x;
    }

    public final int x;

    public int getY() {
        return y;
    }

    public final int y;

    private final int maxWidth;
    private final int maxHeight;

    Island(int unScaledMaxWidth, int unscaledMaxHeight, int x, int y) {
        this.maxHeight = unScaledMaxWidth * BLOCK_MULTIPLIER;
        this.maxWidth = unscaledMaxHeight * BLOCK_MULTIPLIER;

        repr = new IslandTileType[getMaxWidth()][getMaxHeight()];

        populateIsland();
        smoothIsland();
        scaleIsland();

        this.x = x;
        this.y = y;
    }

    public void smoothIsland() {
        IslandTileType[][] oldRepr = Arrays.copyOfRange(repr, 0, repr.length);

        for (int currentX = 0; currentX < getUnscaledMaxWidth(); currentX++) {
            for (int currentY = 0; currentY < getUnscaledMaxHeight(); currentY++) {
                if (oldRepr[currentX][currentY] == null)
                    continue;

                int nextX = currentX + 1;
                int prevX = currentX - 1;

                int nextY = currentY + 1;
                int prevY = currentY - 1;

                IslandTileType westType = prevX == -1 ? null : oldRepr[prevX][currentY];
                IslandTileType northType = nextY == getUnscaledMaxHeight() ? null : oldRepr[currentX][nextY];
                IslandTileType eastType = nextX == getUnscaledMaxWidth() ? null : oldRepr[nextX][currentY];
                IslandTileType southType = prevY == -1 ? null : oldRepr[currentX][prevY];

                int surroundingBlocks = 0;

                if (westType != null)
                    surroundingBlocks++;

                if (northType != null)
                    surroundingBlocks++;

                if (eastType != null)
                    surroundingBlocks++;

                if (southType != null)
                    surroundingBlocks++;

                if (surroundingBlocks < SURROUNDING_BLOCKS_REQUIRED) {
                    getRepr()[currentX][currentY] = null;
                }
            }
        }
    }

    public void populateIsland() {
        float originX = getUnscaledMaxWidth() / 2;
        float originY = getUnscaledMaxHeight() / 2;

        for (int currentX = 0; currentX < getUnscaledMaxWidth(); currentX++)
            for (int currentY = 0; currentY < getUnscaledMaxHeight(); currentY++) {
                float xOriginGap = Math.abs(originX - currentX);
                float yOriginGap = Math.abs(originY - currentY);

                float distanceFromOrigin = (float) (Math.sqrt(xOriginGap * xOriginGap
                                                            + yOriginGap * yOriginGap));

                float chance = MathUtils.random(0, distanceFromOrigin);

                if (chance < NON_SCALED_CENTER_BLOCKS)
                    getRepr()[currentX][currentY] = IslandTileType.DIRT;
            }
    }

    public void scaleIsland() {
        IslandTileType[][] repCopy = new IslandTileType[getUnscaledMaxWidth()][getUnscaledMaxHeight()];

        for (int xCopyIndex = 0; xCopyIndex < repCopy.length; xCopyIndex++)
            for (int yCopyIndex = 0; yCopyIndex < repCopy[xCopyIndex].length; yCopyIndex++)
                repCopy[xCopyIndex][yCopyIndex] = getRepr()[xCopyIndex][yCopyIndex];

        for (int currentX = 0; currentX < repCopy.length; currentX++)
            for (int currentY = 0; currentY < repCopy[currentX].length; currentY++) {

            IslandTileType targetIslandTileType = repCopy[currentX][currentY];

                for (int xOffset = 0; xOffset < BLOCK_MULTIPLIER; xOffset++)
                    for (int yOffset = 0; yOffset < BLOCK_MULTIPLIER; yOffset++) {
                        int targetX = currentX * BLOCK_MULTIPLIER + xOffset;
                        int targetY = currentY * BLOCK_MULTIPLIER + yOffset;

                        getRepr()[targetX][targetY] = targetIslandTileType;
                    }
            }
    }

    public void prettifyIsland() {
        for (int currentX = 0; currentX < getRepr().length; currentX++) {
            for (int currentY = 0; currentY < getRepr()[currentX].length; currentY++) {
                IslandTileType currentIslandTileType = getRepr()[currentX][currentY];

                if (currentIslandTileType == null)
                    return;

                boolean[][] directionArray = new boolean[2][2];

                for (int xOffset = -1; xOffset < 2; xOffset++) {
                    for (int yOffset = -1; yOffset < 2; yOffset++) {
                        if (xOffset == 0 && yOffset == 0)
                            continue;

                        int targetX = currentX + xOffset;
                        int targetY = currentY + yOffset;

                        IslandTileType targetIslandTileType = getRepr()[targetX][targetY];

                        directionArray[xOffset + 1][yOffset + 1] = targetIslandTileType == null;
                    }
                }
            }
        }
    }

    public boolean canBuild(BuildingType buildingType, int xTile, int yTile, MatchMap matchMap) {
        int groundTilesNum = 0;

        for (int xOffset = 0; xOffset < buildingType.getTileWidth(); xOffset++) {
            for (int yOffset = 0; yOffset < buildingType.getTileHeight(); yOffset++) {
                int newX = xTile + xOffset;
                int newY = yTile + yOffset;

                IslandTileType islandTileType;

                boolean xOutOfBounds = !(0 < newX && newX < getMaxWidth());
                boolean yOutOfBounds = !(0 < newY && newY < getMaxHeight());

                boolean outOfBounds = xOutOfBounds || yOutOfBounds;

                islandTileType = outOfBounds ? null : getRepr()[newX][newY];

                if (islandTileType != null)
                    groundTilesNum++;
            }
        }

        return groundTilesNum >= buildingType.getMinGroundTiles();
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public int getUnscaledMaxWidth() {
        return getMaxWidth() / BLOCK_MULTIPLIER;
    }

    public int getUnscaledMaxHeight() {
        return getMaxHeight() / BLOCK_MULTIPLIER;
    }

    public IslandTileType[][] getRepr() {
        return repr;
    }

    @Override
    public String toString() {
        return Island.class.getSimpleName() + "[" + getMaxWidth() + ", " + getMaxHeight() + "]";
    }
}
