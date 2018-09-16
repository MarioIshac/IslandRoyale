package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.utils.IntMap;

public class EntityTypeManager {
    private final IntMap<EntityType<?, ?>> entityIDMap = new IntMap<>();

    @SuppressWarnings("unchecked") // As there is no possible way to perform type safety given an integer id that
    // Represents many different types of entities
    public <Y extends Entity<Y, Z>, Z extends EntityType<Y, Z>> Z getEntityType(int id) {
        return (Z) getEntityIDMap().get(id);
    }

    IntMap<EntityType<?, ?>> getEntityIDMap() {
        return entityIDMap;
    }
}
