package me.theeninja.islandroyale;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<Resource, Float> resourceMap = new HashMap<>();

    /**
     * Initialize all resources within the map to 0 amount for allowance of
     * calculation methods on the inventory.
     */
    public Inventory() {
        for (Resource resource : Resource.values())
            getResourceMap().put(resource, 0f);
    }

    public void put(Resource resource, float resourceAmount) {
        getResourceMap().computeIfPresent(resource, (presentResource, currentResourceAmount) ->
            currentResourceAmount + resourceAmount
        );
    }

    /**
     * @param resource
     * @param resourceAmount
     * @return Whether there is {@code resouceAmount} of {@code resouce} within
     *         the inventory.
     */
    public boolean has(Resource resource, float resourceAmount) {
        float currentAmountOfResource = getResourceMap().get(resource);
        return currentAmountOfResource >= resourceAmount;
    }

    public boolean has(Inventory requiredInventory) {
        Map<Resource, Float> resourceAmounts = requiredInventory.getResourceMap();
        for (Map.Entry<Resource, Float> entry : resourceAmounts.entrySet()) {
            Resource resource = entry.getKey();
            float resourceAmount = entry.getValue();

            if (!has(resource, resourceAmount))
                return false;
        }

        return true;
    }

    /**
     * Subtract {@code resourceAmount} from the amount of {@code resource}.
     * Call {@link Inventory#has(Resource, float)} before this method to validate
     * that the subtraction will not cause a negative amount to remain.
     *
     * @param resource
     * @param resourceAmount
     */
    public void remove(Resource resource, float resourceAmount) {
        getResourceMap().computeIfPresent(resource, (presentResource, currentResourceAmount) ->
            currentResourceAmount - resourceAmount
        );
    }

    public void remove(Inventory inventory) {
        Map<Resource, Float> resourceAmounts = inventory.getResourceMap();
        for (Map.Entry<Resource, Float> entry : resourceAmounts.entrySet()) {
            Resource resource = entry.getKey();
            float resourceAmount = entry.getValue();

            remove(resource, resourceAmount);
        }
    }

    public float get(Resource resource) {
        return getResourceMap().get(resource);
    }

    public Map<Resource, Float> getResourceMap() {
        return resourceMap;
    }
}
