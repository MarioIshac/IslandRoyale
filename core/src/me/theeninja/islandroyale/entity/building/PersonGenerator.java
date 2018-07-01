package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.IslandTileType;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.controllable.Person;
import me.theeninja.islandroyale.entity.controllable.PersonType;

public class PersonGenerator extends OffenseBuilding<PersonGenerator, PersonGeneratorType, Person, PersonType> {
    public PersonGenerator(PersonGeneratorType entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    @Override
    protected PersonGenerator getReference() {
        return this;
    }

    @Override
    public Vector2 getAvailableCoordinates(PersonType entityType, float buildingX, float buildingY, MatchMap matchMap) {
        Island associatedIsland = matchMap.getIsland(buildingX, buildingY);

        float relativeToIslandX = buildingX - associatedIsland.x;
        float relativeToIslandY = buildingY - associatedIsland.y;

        for (int offsetX = -1; offsetX <= getEntityType().getTileWidth(); offsetX ++) {
            for (int offsetY = -1; offsetY <= getEntityType().getTileHeight(); offsetY++) {
                boolean xInBuilding = 0 <= offsetX && offsetX < getEntityType().getTileWidth();
                boolean yInBuilding = 0 <= offsetY && offsetY < getEntityType().getTileHeight();

                if (xInBuilding && yInBuilding)
                    continue;

                int x = (int) (relativeToIslandX + offsetX);
                int y = (int) (relativeToIslandY + offsetY);

                // Indicates that x coordinate is at edge of island
                if (!(0 < x && x < associatedIsland.getMaxWidth()))
                    continue;

                // Indicates that y coordinate is at edge of island
                if (!(0 < y && y < associatedIsland.getMaxHeight()))
                    continue;

                IslandTileType islandTileType = associatedIsland.getRepr()[x][y];

                if (islandTileType != null)
                    return new Vector2(x, y);
            }
        }

        return null;
    }

    @Override
    Person newGenericSpecificEntity(PersonType entityType, Player owner, float x, float y) {
        return new Person(entityType, owner, x, y);
    }

}
