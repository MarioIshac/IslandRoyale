package me.theeninja.islandroyale;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectIntMap;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.treasure.*;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

import java.util.*;

public class MatchMap {
    private final int tileWidth;
    private final int tileHeight;
    private final MatchScreen matchScreen;
    private final Array<DataTreasureType> dataTreasureTypes;
    private final Array<ResourceTreasureType> resourceTreasureTypes;

    public void addEntity(Entity<?, ?> entity) {
        // Add entity to stage
        getMatchScreen().getMapStage().addActor(entity);

        Array<Entity<?, ?>> priorityEntities = getCertainPriorityEntities(entity.getEntityType().getDrawingPriority());

        // Add entity to entity list
        priorityEntities.add(entity);
    }

    private static final int ISLAND_GAP = 110;

    private final List<Island> islands = new ArrayList<>();

    public Island getIsland(float absoluteX, float absoluteY) {
        for (Island island : getIslands()) {
            boolean xInBounds = island.x <= absoluteX && absoluteX <= island.x + island.getMaxWidth();
            boolean yInBounds = island.y <= absoluteY && absoluteY <= island.y + island.getMaxHeight();

            if (xInBounds && yInBounds)
                return island;
        }

        return null;
    }

    @SuppressWarnings("unchecked") // Generic Array not Allowed in Java
    private final Array<Entity<?, ?>>[] entities = new Array[EntityType.NUMBER_OF_PRIORITIES];

    public void flushDeadEntities() {
        // Iterate backwards to allow removing entities on the way
        for (int entityPriority = 0; entityPriority < getAllPriorityEntities().length; entityPriority++) {
            Array<Entity<?, ?>> certainPriorityEntities = getCertainPriorityEntities(entityPriority);

            int lastEntityIndex = certainPriorityEntities.size - 1;

            for (int entityIndex = lastEntityIndex; entityIndex >= 0; entityIndex--) {
                Entity<?, ?> entity = certainPriorityEntities.get(entityIndex);

                if (entity.shouldRemove()) {
                    // Remove entity from stage
                    entity.remove();

                    // Remove entity from list
                    certainPriorityEntities.removeIndex(entityIndex);
                }
            }
        }
    }

    public MatchMap(int tileWidth, int tileHeight, MatchScreen matchScreen,
                    Array<DataTreasureType> dataTreasureTypes,
                    Array<ResourceTreasureType> resourceTreasureTypes) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.matchScreen = matchScreen;
        this.dataTreasureTypes = dataTreasureTypes;
        this.resourceTreasureTypes = resourceTreasureTypes;

        setUpEntitiesArray();
        generateIslands();
        generateResourceTreasures();
        generateDataTreasures();
    }

    private void setUpEntitiesArray() {
        for (int entityPriority = 0; entityPriority < getAllPriorityEntities().length; entityPriority++)
            getAllPriorityEntities()[entityPriority] = new Array<>();
    }

    private void generateIslands() {
        for (int xTile = 0; xTile < getTileHeight(); xTile += ISLAND_GAP) {
            for (int yTile = 0; yTile < getTileHeight(); yTile += ISLAND_GAP) {
                Island island = new Island(11, 11, xTile, yTile);

                getIslands().add(island);
            }
        }
    }

    private int resourceTreasureGenerationCountPerIteration;
    private int dataTreasureGenerationCountPerIteration;

    private void generateResourceTreasures() {
        for (int generationIndex = 0; generationIndex < getResourceTreasureGenerationCountPerIteration(); generationIndex++) {
            int randomXTile = MathUtils.random(getTileWidth());
            int randomYTile = MathUtils.random(getTileHeight());
            int randomResourceTreasureTypeIndex = MathUtils.random(getResourceTreasureTypes().size);
            ResourceTreasureType randomResourceTreasuretype = getResourceTreasureTypes().get(randomResourceTreasureTypeIndex);

            ResourceTreasure resourceTreasure = new ResourceTreasure(randomResourceTreasuretype, null, randomXTile, randomYTile);

            getCertainPriorityEntities(randomResourceTreasuretype.getDrawingPriority()).add(resourceTreasure);
        }
    }

    private void generateDataTreasures() {
        for (int generationIndex = 0; generationIndex < getDataTreasureGenerationCountPerIteration(); generationIndex++) {
            int randomXTile = MathUtils.random(getTileWidth());
            int randomYTile = MathUtils.random(getTileHeight());
            int randomResourceTreasureTypeIndex = MathUtils.random(getDataTreasureTypes().size);
            DataTreasureType dataTreasureType = getDataTreasureTypes().get(randomResourceTreasureTypeIndex);

            DataTreasure dataTreasure = new DataTreasure(dataTreasureType, null, randomXTile, randomYTile);

            getCertainPriorityEntities(dataTreasureType.getDrawingPriority()).add(dataTreasure);
        }
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public List<Island> getIslands() {
        return islands;
    }

    public Array<Entity<?, ?>>[] getAllPriorityEntities() {
        return entities;
    }

    public Array<Entity<?, ?>> getCertainPriorityEntities(int entityPriority) {
        return getAllPriorityEntities()[entityPriority];
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public MatchScreen getMatchScreen() {
        return matchScreen;
    }

    public Array<DataTreasureType> getDataTreasureTypes() {
        return dataTreasureTypes;
    }

    public Array<ResourceTreasureType> getResourceTreasureTypes() {
        return resourceTreasureTypes;
    }

    public int getDataTreasureGenerationCountPerIteration() {
        return dataTreasureGenerationCountPerIteration;
    }

    public int getResourceTreasureGenerationCountPerIteration() {
        return resourceTreasureGenerationCountPerIteration;
    }
}
