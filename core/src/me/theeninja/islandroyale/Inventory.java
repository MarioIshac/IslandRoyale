package me.theeninja.islandroyale;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<Resource, Integer> resourceMap = new HashMap<Resource, Integer>();

    Inventory() {
        for (Resource resource : Resource.values())
            getResourceMap().put(resource, 0);

    }

    public Map<Resource, Integer> getResourceMap() {
        return resourceMap;
    }
}
