package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.PersonEntityType;

public class PersonGeneratorBuildingType extends OffenseBuildingType<PersonGeneratorBuildingType, PersonEntityType> {
    @Override
    public Entity<PersonEntityType> produceEntity(PersonEntityType entityType, Player player, Vector2 buildingPos, MatchMap matchMap) {
        Island associatedIsland = matchMap.getIsland(buildingPos);
        Vector2 islandPos = matchMap.getIslands().get(associatedIsland);
        Vector2 relativeToIslandPos = buildingPos.cpy().sub(islandPos);

        // Put person to right of building
        relativeToIslandPos.add(getTileWidth(), 0);

        return new Entity<>(entityType, player, relativeToIslandPos);
    }
}
