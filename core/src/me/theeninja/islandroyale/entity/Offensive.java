package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.MatchMap;

import java.util.Map;

public interface Offensive<T extends EntityType<T>> {
    static final String ATTACKING_TARGET_LABEL = "attackingTarget";

    /**
     * This should only be called when a new target to attack is required. The way attacking works,
     * if an enemy comes in closer than other enemy, if the first enemy still has target, it will not lose
     * that state.
     *
     * @param entity The entity that we are determining the closest target to.
     * @param matchMap The map that contains all the entities to take into consideration.
     *
     * @return another entity that is closest to {@code entity}
     */
    default Entity<? extends EntityType<?>> getNewTargetEntity(Entity<T> entity, MatchMap matchMap, float tileRange) {
        // We keep the minimum distance SQUARED in order to improve performance
        float minDistanceSquared = Float.MAX_VALUE;
        Entity<? extends EntityType> closestEntity = null;

        Vector2 entityLocation = matchMap.getEntities().get(entity);

        for (Map.Entry<Entity<? extends EntityType<?>>, Vector2> entry : matchMap.getEntities().entrySet()) {
            Entity<? extends EntityType> otherEntity = entry.getKey();
            Vector2 otherLocation = entry.getValue();

            // Do not let the entity target itself...
            if (entity == otherEntity)
                continue;

            // Do not let entitty target entities of same player...
            if (entity.getOwner() == otherEntity.getOwner())
                continue;

            float xDiff = entityLocation.x - otherLocation.x;
            float yDiff = entityLocation.y - otherLocation.y;

            float distanceSquared = xDiff * xDiff + yDiff * yDiff;

            // Do not let entity target entities outside of range
            if (distanceSquared > tileRange * tileRange)
                continue;

            if (distanceSquared < minDistanceSquared) {
                minDistanceSquared = distanceSquared;
                closestEntity = otherEntity;
            }
        }

        return closestEntity;
    }
}
