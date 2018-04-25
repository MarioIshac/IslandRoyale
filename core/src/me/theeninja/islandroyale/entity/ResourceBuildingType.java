package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import me.theeninja.islandroyale.IslandRoyaleGame;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.Resource;

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
    public void initialize(Entity<BuildingEntityType<ResourceBuildingType>> entity) {

    }

    @Override
    public void check(Entity<BuildingEntityType<ResourceBuildingType>> entity, float delta, Player player, MatchMap matchMap) {
        float amountOfResource = delta * getBaseRate();
        float multiplier = getResourceProductionMultiplier(entity.getLevel());

        amountOfResource *= multiplier;

        player.getInventory().put(getResource(), amountOfResource);
    }

    @Override
    public void present(Entity<BuildingEntityType<ResourceBuildingType>> entity, Batch batch, int centerPixelX, int centerPixelY) {
        // TODO update textures to visualize resource prouction
    }

    private float getResourceProductionMultiplier(int level) {
        float result = baseRate;

        while (level-- > 1)
            result *= RESOURCE_MULTIPLIER_PER_LEVEL;

        return result;
    }
}