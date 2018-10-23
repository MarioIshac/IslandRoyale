package me.theeninja.islandroyale;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.building.*;
import me.theeninja.islandroyale.entity.treasure.*;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

public class MatchMap {
    private final int tileWidth;
    private final int tileHeight;
    private final MatchScreen matchScreen;

    public <A extends Entity<A, B>, B extends EntityType<A, B>> void addEntity(A entity) {
        // Add entity to stage
        getMatchScreen().getMapStage().addActor(entity);

        Array<A> entities = getEntitiesOfType(entity.getEntityType());

        // Add entity to entity list
        entities.add(entity);
    }

    private final byte[][] tiles = new byte[MatchScreen.WHOLE_WORLD_TILE_WIDTH][MatchScreen.WHOLE_WORLD_TILE_HEIGHT];
    private final float[][] tileHeights;

    @SuppressWarnings("unchecked") // Generic Array Creation not Allowed in Java
    private final Array<Entity<?, ?>>[] entities = new Array[EntityType.NUMBER_OF_ENTITY_TYPES];

    public void flushDeadEntities() {
        // Iterate backwards to allow removing entities on the way
        for (int entityPriority = 0; entityPriority < getEntities().length; entityPriority++) {
            final Array<Entity<?, ?>> certainPriorityEntities = getEntities()[entityPriority];

            final int lastEntityIndex = certainPriorityEntities.size - 1;

            for (int entityIndex = lastEntityIndex; entityIndex >= 0; entityIndex--) {
                final Entity<?, ?> entity = certainPriorityEntities.get(entityIndex);

                if (entity.shouldRemove()) {
                    // Remove entity from stage
                    entity.remove();

                    // Remove entity from list
                    certainPriorityEntities.removeIndex(entityIndex);
                }
            }
        }
    }

    private void populateHeadquarters() {
        final float islandHeight = getMapMode().getMinimumIslandHeightPercentage() * MAX_HEIGHT;

        HeadQuartersType startingHeadQuartersType = getHeadQuartersTypes().get(HeadQuartersTyoeRegisterer.HEAD_QUARTERS_STARTING_INDEX);

        for (Player player : getPlayers()) {
            // Initialized to random values (Integer.MIN_VALUE), as guaranteed to be changed later
            int tileMaxX = Integer.MIN_VALUE;
            int tileMaxY = Integer.MIN_VALUE;

            float currentHeightMax = MIN_HEIGHT;

            // TODO add support for water-based headquarters, current step-taking system does not account for situation where min required tiles != area

            for (int xTile = 0; xTile < MatchScreen.WHOLE_WORLD_TILE_WIDTH; xTile++) {
                for (int yTile = 0; yTile < MatchScreen.WHOLE_WORLD_TILE_HEIGHT; yTile++) {
                    final float tileHeight = getTileHeights()[xTile][yTile];

                    // Assume that building can be built at (xTile, yTile) by default
                    int xJump = 0;
                    int yJump = 0;

                    // Set x and yJump to maximum amount of tiles that can be jumped with the guarantee of no usable locations in that region
                    for (int xOffset = startingHeadQuartersType.getTileWidth() - 1; xOffset >= 0; xOffset--) {
                        for (int yOffset = startingHeadQuartersType.getTileHeight() - 1; yOffset >= 0; yOffset--) {
                            if (tileHeight < islandHeight) {
                                xJump = xOffset;
                                break;
                            }
                        }
                    }

                    for (int yOffset = startingHeadQuartersType.getTileHeight() - 1; yOffset >= 0; yOffset--) {
                        for (int xOffset = startingHeadQuartersType.getTileWidth() - 1; xOffset >= 0; xOffset--) {
                            if (tileHeight < islandHeight) {
                                yJump = yOffset;
                                break;
                            }
                        }
                    }
                    //

                    if (
                        // Can build at (x, y)
                        xJump == 0 && yJump == 0 &&

                        // (x, y)'s height is higher than current height
                        tileHeight > currentHeightMax
                    ) {
                        currentHeightMax = tileHeight;
                        tileMaxX = xTile;
                        tileMaxY = yTile;
                    }
                }
            }

            final Array<HeadQuarters> allPlayerHeadQuarters = player.getAllHeadQuarters();
            final HeadQuarters newHeadQuarters = new HeadQuarters(startingHeadQuartersType, player, tileMaxX, tileMaxY, getMatchScreen().getMatch());

            allPlayerHeadQuarters.add(newHeadQuarters);

            System.out.println("Head X " + newHeadQuarters.getX());
            System.out.println("Head Y " + newHeadQuarters.getY());

            addEntity(newHeadQuarters);
        }
    }

    private final Array<DataTreasureType> dataTreasureTypes;
    private final Array<ResourceTreasureType> resourceTreasureTypes;
    private final Array<HeadQuartersType> headQuartersTypes;
    private final Player[] players;
    private final MapMode mapMode;

    public MatchMap(int tileWidth, int tileHeight, MatchScreen matchScreen,
                    Array<DataTreasureType> dataTreasureTypes,
                    Array<ResourceTreasureType> resourceTreasureTypes,
                    Array<HeadQuartersType> headQuartersTypes, Player[] players, MapMode mapMode) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.matchScreen = matchScreen;
        this.dataTreasureTypes = dataTreasureTypes;
        this.resourceTreasureTypes = resourceTreasureTypes;
        this.headQuartersTypes = headQuartersTypes;

        this.players = players;
        this.mapMode = mapMode;

        this.tileHeights = populateTiles();
        setUpEntitiesArray();

        generateTreasures(getResourceTreasureTypes(), ResourceTreasure::new, 5);
        generateTreasures(getDataTreasureTypes(), DataTreasure::new, 5);

        populateHeadquarters();
    }

    private void setUpEntitiesArray() {
        for (int entityPriority = 0; entityPriority < getEntities().length; entityPriority++)
            getEntities()[entityPriority] = new Array<>();
    }


    public byte[][] getTiles() {
        return tiles;
    }

    private static final int MIN_HEIGHT = 0;
    private static final int MAX_HEIGHT = 100;

    private float[][] populateTiles() {
        final float islandHeight = getMapMode().getMinimumIslandHeightPercentage() * MAX_HEIGHT;
        final float grassHeight = getMapMode().getMaximumGrassHeightPercentage() * MAX_HEIGHT;

        final float[][] heightMap = PerlinNoiseGenerator.generateHeightMap(MatchScreen.WHOLE_WORLD_TILE_WIDTH, MatchScreen.WHOLE_WORLD_TILE_HEIGHT, MIN_HEIGHT, MAX_HEIGHT, mapMode.getOctaveCount());

        for (int xTile = 0; xTile < MatchScreen.WHOLE_WORLD_TILE_WIDTH; xTile++) {
            for (int yTile = 0; yTile < MatchScreen.WHOLE_WORLD_TILE_HEIGHT; yTile++) {
                final float tileHeight = heightMap[xTile][yTile];

                // less than required height
                if (tileHeight < islandHeight) {
                    getTiles()[xTile][yTile] = IslandTileType.OCEAN;
                    continue;
                }

                final boolean withinGrassheight = tileHeight <= grassHeight;

                getTiles()[xTile][yTile] = withinGrassheight ? IslandTileType.GRASS : IslandTileType.DIRT;
            }
        }

        return heightMap;
    }

    public <A extends Building<A, B>, B extends BuildingType<A, B>> boolean canBuild(B buildingType, int buildingTileX, int buildingTileY) {
        int buildTileCount = 0;

        final Array<A> buildingEntities = getEntitiesOfType(buildingType);

        final float rightX = buildingTileX + buildingType.getTileWidth();
        final float upperY = buildingTileY + buildingType.getTileHeight();

        for (final Entity<?, ?> buildingEntity : buildingEntities) {
            if (
                // Current building contains same x values
                (buildingTileX <= buildingEntity.getX() && buildingEntity.getX() < rightX) &&

                // Current building contains same y values
                (buildingTileY <= buildingEntity.getY() && buildingEntity.getY() < upperY)
            ) {
                return false;
            }
        }

        for (int xOffset = 0; xOffset < buildingType.getTileWidth(); xOffset++) {
            for (int yOffset = 0; yOffset < buildingType.getTileHeight(); yOffset++) {
                final int tileX = (buildingTileX + xOffset) % MatchScreen.WHOLE_WORLD_TILE_WIDTH;
                final int tileY = (buildingTileY + yOffset) % MatchScreen.WHOLE_WORLD_TILE_HEIGHT;

                final byte tileType = getTiles()[tileX][tileY];
                final boolean isGroundTile = IslandTileType.isGround(tileType);

                System.out.println(tileX + " : " + tileY + " " + isGroundTile);

                if (isGroundTile) {
                    buildTileCount++;
                }
            }
        }

        return buildTileCount >= buildingType.getMinGroundTiles();
    }

    @FunctionalInterface
    private interface TreasureConstructor<A extends Treasure<A, B>, B extends TreasureType<A, B>> {
        A construct(B treasureType, Player owner, float x, float y);
    }

    private <A extends Treasure<A, B>, B extends TreasureType<A, B>> void generateTreasures(Array<B> treasures, TreasureConstructor<A, B> treasureConstructor, int amountOfTreasures) {
        for (int generationIndex = 0; generationIndex < amountOfTreasures; generationIndex++) {
            final int randomXTile = MathUtils.random(getTileWidth());
            final int randomYTile = MathUtils.random(getTileHeight());

            // TODO Fix bug, as size of treasures is always 0

            final int maxIndex = treasures.size - 1;

            // Indicates there are loaded treasure types
            if (maxIndex != -1) {
                final int treasureTypeIndex = MathUtils.random(maxIndex);
                final B randomTreasureType = treasures.get(treasureTypeIndex);

                final A resourceTreasure = treasureConstructor.construct(randomTreasureType, null, randomXTile, randomYTile);

                getEntitiesOfType(randomTreasureType).add(resourceTreasure);
            }
        }
    }
    
    public int getTileHeight() {
        return tileHeight;
    }

    public Array<Entity<?, ?>>[] getEntities() {
        return entities;
    }

    public <A extends Entity<A, B>, B extends EntityType<A, B>> Array<A> getEntitiesOfType(B entityType) {
        return getEntitiesOfType(entityType.getEntityTypeIndex());
    }

    @SuppressWarnings("unchecked")
    public <A extends Entity<A, B>, B extends EntityType<A, B>> Array<A> getEntitiesOfType(int entityTypeIndex) {
        Array<Entity<?, ?>> uncastedEntities = getEntities()[entityTypeIndex];

        return (Array<A>) uncastedEntities;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public MatchScreen getMatchScreen() {
        return matchScreen;
    }

    public float[][] getTileHeights() {
        return tileHeights;
    }

    public Player[] getPlayers() {
        return players;
    }

    public Array<HeadQuartersType> getHeadQuartersTypes() {
        return headQuartersTypes;
    }

    public Array<ResourceTreasureType> getResourceTreasureTypes() {
        return resourceTreasureTypes;
    }

    public Array<DataTreasureType> getDataTreasureTypes() {
        return dataTreasureTypes;
    }

    public MapMode getMapMode() {
        return mapMode;
    }
}
