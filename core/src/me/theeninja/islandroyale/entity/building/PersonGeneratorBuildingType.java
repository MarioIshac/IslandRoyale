package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.PersonEntityType;

public class PersonGeneratorBuildingType extends OffenseBuildingType<PersonGeneratorBuildingType, PersonEntityType> {
    @Override
    public Entity<PersonEntityType> getEntityRequested(PersonEntityType entityType, Player player, Vector2 buildingPos, MatchMap matchMap) {
        return new Entity<>(entityType, player, buildingPos);
    }
}
