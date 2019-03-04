package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.utils.IntMap;

import java.util.Iterator;

public class EntityTypeManager {
    private final IntMap<EntityType<?, ?>> entityIDMap = new IntMap<>();

    @SuppressWarnings("unchecked") // As there is no possible way to perform type safety given an integer id that
    // Represents many different types of entitiesz
    public <Y extends Entity<Y, Z>, Z extends EntityType<Y, Z>> Z getEntityType(int id) {
        return (Z) getEntityIDMap().get(id);
    }

    public int getEntityTypeCount(int entityTypeKey) {
        int entityTypeCount = 0;

        for (EntityType<?, ?> entityType : getEntityIDMap().values()) {

            int currentEntityTypeKey = EntityType.Unsafe.getEntityTypeKey(entityType.getClass());

            if (currentEntityTypeKey == entityTypeKey) {
                entityTypeCount++;
            }
        }

        return entityTypeCount;
    }

    public IntMap<EntityType<?, ?>> getEntityIDMap() {
        return entityIDMap;
    }

    public InteractableEntityType<?, ?> getRandomInteractableEntityTypeInstance(final int entityTypeKey, int entityTypeIndex) {
        Iterator<EntityType<?, ?>> entityTypes = getEntityIDMap().values().iterator();
        InteractableEntityType<?, ?> currentInteractableEntityType = null;

        while (entityTypeIndex >= 0 && entityTypes.hasNext()) {
            EntityType<?, ?> entityType = entityTypes.next();
            Class<? extends EntityType> nextEntityTypeClass = entityType.getClass();

            int nextEntityTypeKey = EntityType.Unsafe.getEntityTypeKey(nextEntityTypeClass);


            if (nextEntityTypeKey == entityTypeKey) {
                entityTypeIndex--;
                currentInteractableEntityType = (InteractableEntityType<?, ?>) entityType;
            }
        }

        if (currentInteractableEntityType == null) {
            throw new IllegalArgumentException("No entity types loaded to iterate over.");
        }

        return currentInteractableEntityType;
    }
}
