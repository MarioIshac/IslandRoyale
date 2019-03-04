package me.theeninja.islandroyale.entity.building;

import me.theeninja.islandroyale.Resource;

public class ResourceGeneratorType extends BuildingType<ResourceGenerator, ResourceGeneratorType> {
    private static final float RESOURCE_MULTIPLIER_PER_LEVEL = 1.1f;

    private Resource resource;

    /**
     * Represents the base amount of {@code resource} (at level 1) that is to be produced per second.
     */
    private float baseRate;

    public float getBaseRate() {
        return baseRate;
    }

    public Resource getResource() {
        return resource;
    }
}