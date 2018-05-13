package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import me.theeninja.islandroyale.IslandRoyaleGame;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.Resource;
import me.theeninja.islandroyale.entity.Entity;

public class ResourceBuildingType extends BuildingEntityType<ResourceBuildingType> {
    private static final float RESOURCE_MULTIPLIER_PER_LEVEL = 1.1f;

    private Resource resource;

    public static void a() {
        ResourceBuildingType resourceBuildingType = new ResourceBuildingType();
        resourceBuildingType.resource = Resource.WOOD;
        resourceBuildingType.getInventoryCost().put(Resource.WOOD, 20f);

        System.out.println(IslandRoyaleGame.JSON.toJson(resourceBuildingType, ResourceBuildingType.class));
    }

    /**
     * Represents the base amount of {@code resource} (at level 1) that is to be produced per second.
     */
    private float baseRate;

    /*private ResourceBuildingType(String name, float baseHealth, int maxLevel, int tileWidth, int tileHeight, int minGroundFiles, Resource resource, float baseRate) {
        super(name, baseHealth, maxLevel, tileWidth, tileHeight, minGroundFiles);
        this.resource = resource;
        this.baseRate = baseRate;
    }*/

    public float getBaseRate() {
        return baseRate;
    }

    public Resource getResource() {
        return resource;
    }

    @Override
    public void configureEditor(Entity<ResourceBuildingType> entity, Table verticalGroup) {

    }

    @Override
    public void setUp(Entity<ResourceBuildingType> entity) {
        super.setUp(entity);
    }

    @Override
    public void check(Entity<ResourceBuildingType> entity, float delta, Player player, MatchMap matchMap) {
        float amountOfResource = delta * getBaseRate();
        int currentLevel = getProperty(entity, LEVEL_LABEL);

        float multiplier = getResourceProductionMultiplier(currentLevel);

        amountOfResource *= multiplier;

        player.getInventory().put(getResource(), amountOfResource);
    }

    @Override
    public void present(Entity<ResourceBuildingType> entity, Camera projector, Stage stage) {
        // TODO update textures to visualize resource prouction
    }

    private float getResourceProductionMultiplier(int level) {
        float result = baseRate;

        while (level-- > 1)
            result *= RESOURCE_MULTIPLIER_PER_LEVEL;

        return result;
    }
}