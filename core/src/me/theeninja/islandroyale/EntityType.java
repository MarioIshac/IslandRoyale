package me.theeninja.islandroyale;

import java.util.HashMap;
import java.util.Map;

public class EntityType<T extends EntityType> {
    public static final Map<Integer, me.theeninja.islandroyale.entity.EntityType<? extends me.theeninja.islandroyale.entity.EntityType<?>>> IDS = new HashMap<>();

    public static <T extends me.theeninja.islandroyale.entity.EntityType<T>> T getEntityType(int id) {
        return (T) IDS.get(id);
    }

    private static final String BUILDING_DIRECTORY = "building/";
    private static final String MOVING_DIRECTORY = "moving/";
    public static final String PROJECTILE_DIRECTORY = "projectile/";

    public static final String RESOURCE_DIRECTORY = BUILDING_DIRECTORY + "resource/";
    public static final String DEFENSE_DIRECTORY = BUILDING_DIRECTORY + "defense/";
    public static final String OFFENSE_DIRECTORY = BUILDING_DIRECTORY + "offense/";
    public static final String PERSON_DIRECTORY = MOVING_DIRECTORY + "person/";
    public static final String TRANSPORT_DIRECTORY = MOVING_DIRECTORY + "transport/";
}
