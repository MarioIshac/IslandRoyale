package me.theeninja.islandroyale.entity.building;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.controllable.Person;
import me.theeninja.islandroyale.entity.controllable.PersonType;
import me.theeninja.islandroyale.gui.screens.Match;

public class PersonGenerator extends OffenseBuilding<PersonGenerator, PersonGeneratorType, Person, PersonType> {
    public PersonGenerator(PersonGeneratorType entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);
    }

    @Override
    protected PersonGenerator getReference() {
        return this;
    }

    @Override
    Person newGenericSpecificEntity(PersonType entityType, Player owner, float x, float y, Match match) {
        return new Person(entityType, owner, x, y, match);
    }

}
