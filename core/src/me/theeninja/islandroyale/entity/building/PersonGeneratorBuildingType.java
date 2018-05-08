package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.IslandTileType;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.PersonEntityType;

public class PersonGeneratorBuildingType extends OffenseBuildingType<PersonGeneratorBuildingType, PersonEntityType> {
    @Override
    public Entity<PersonEntityType> produceEntity(PersonEntityType entityType, Player player, Vector2 buildingPos, MatchMap matchMap) {
        Island associatedIsland = matchMap.getIsland(buildingPos);
        Vector2 islandPos = associatedIsland.getPositionOnMap();
        Vector2 relativeToIslandPos = buildingPos.cpy().sub(islandPos);

        for (int offsetX = -1; offsetX <= getTileWidth(); offsetX ++) {
            for (int offsetY = -1; offsetY <= getTileHeight(); offsetY++) {
                boolean xInBuilding = 0 <= offsetX && offsetX < getTileWidth();
                boolean yInBuilding = 0 <= offsetY && offsetY < getTileHeight();

                if (xInBuilding && yInBuilding)
                    continue;

                int x = (int) (relativeToIslandPos.x + offsetX);
                int y = (int) (relativeToIslandPos.y + offsetY);

                if (!(0 < x && x < associatedIsland.getMaxWidth()))
                    continue;
                if (!(0 < y && y < associatedIsland.getMaxHeight()))
                    continue;

                IslandTileType islandTileType = associatedIsland.getRepr()[x][y];

                if (islandTileType != null)
                    return new Entity<>(entityType, player, new Vector2(x, y));
            }
        }

        return null;
    }
}
