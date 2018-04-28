package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.TransportEntityType;

import java.util.Map;

public class TransportGeneratorBuildingType extends OffenseBuildingType<TransportGeneratorBuildingType, TransportEntityType> {
    @Override
    public Entity<TransportEntityType> getEntityRequested(TransportEntityType entityType, Player player, Vector2 buildingPos, MatchMap matchMap) {
        Island associatedIsland = null;
        Vector2 islandLoc = null;

        for (Map.Entry<Island, Vector2> entry : matchMap.getIslands().entrySet()) {
            Island island = entry.getKey();
            Vector2 location = entry.getValue();

            boolean inXBounds = location.x <= buildingPos.x && buildingPos.x <= (location.x + island.getMaxWidth());
            boolean inYBounds = location.y <= buildingPos.y && buildingPos.y <= (location.y + island.getMaxHeight());

            if (inXBounds && inYBounds) {
                associatedIsland = island;
                islandLoc = location;
                break;
            }
        }

        if (associatedIsland == null)
            throw new UnsupportedOperationException("building not added to map yet");

        Vector2 locationRelativeToIsland = buildingPos.cpy().sub(islandLoc);

        return new Entity<>(entityType, player, locationRelativeToIsland);
    }
}
