package me.theeninja.islandroyale.entity.building;

import me.theeninja.islandroyale.entity.controllable.Person;
import me.theeninja.islandroyale.entity.controllable.PersonType;

public class PersonGeneratorType extends OffenseBuildingType<PersonGenerator, PersonGeneratorType, Person, PersonType> {
    @Override
    public int getEntityTypeIndex() {
        return PERSON_GENERATOR_TYPE;
    }
}
