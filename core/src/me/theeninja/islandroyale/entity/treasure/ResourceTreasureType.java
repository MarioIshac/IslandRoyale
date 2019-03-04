package me.theeninja.islandroyale.entity.treasure;

import me.theeninja.islandroyale.Resource;

import java.util.Random;

public class ResourceTreasureType extends TreasureType<ResourceTreasure, ResourceTreasureType> {
    public Resource resource;

    public int minResourceCount;
    public int maxResourceCount;

    private final Random random = new Random();

    public int getRandomResourceCount() {
        return getRandom().nextInt(getMaxResourceCount() + 1 - getMinResourceCount()) + getMinResourceCount();
    }

    public Random getRandom() {
        return random;
    }

    public Resource getResource() {
        return resource;
    }

    public int getMinResourceCount() {
        return minResourceCount;
    }

    public int getMaxResourceCount() {
        return maxResourceCount;
    }
}
