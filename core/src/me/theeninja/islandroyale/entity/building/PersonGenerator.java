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
    Person newGenericSpecificEntity(PersonType entityType, Player owner, float x, float y) {
        return new Person(entityType, owner, x, y);
    }

}
