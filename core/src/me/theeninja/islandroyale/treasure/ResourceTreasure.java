package me.theeninja.islandroyale.treasure;

import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.Resource;

public class ResourceTreasure extends Treasure {

    private final Resource resource;
    private final int resourceCount;

    public ResourceTreasure(Resource resource, int resourceCount) {
        this.resource = resource;
        this.resourceCount = resourceCount;
    }

    @Override
    void onTreasureFound(Player player) {
        player.getInventory().getResourceMap().computeIfPresent(
            getResource(),
            (res, count) -> count + getResourceCount()
        );
    }

    @Override
    public String getTexturePath() {
        return null;
    }

    public Resource getResource() {
        return resource;
    }

    public int getResourceCount() {
        return resourceCount;
    }
}
