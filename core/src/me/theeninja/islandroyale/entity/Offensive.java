package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.MatchMap;

import java.util.Map;

public interface Offensive<T extends InteractableEntityType<T>> {
    String ATTACKING_TARGET_LABEL = "attackingTarget";

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
    default Entity<? extends InteractableEntityType<?>> getNewTargetEntity(Entity<T> entity, MatchMap matchMap, float tileRange) {
        // We keep the minimum distance SQUARED in order to improve performance
        float minDistanceSquared = Float.MAX_VALUE;
        Entity<? extends InteractableEntityType<?>> closestEntity = null;

        for (Entity<? extends EntityType<?>> otherEntity : matchMap.getEntities()) {
            if (!(otherEntity.getType() instanceof InteractableEntityType))
                continue;

            Entity<? extends InteractableEntityType<?>> interactableOtherEntity =
                    (Entity<? extends InteractableEntityType<?>>) otherEntity;

            // Do not let the entity target itself...
            if (entity == otherEntity)
                continue;

            // Do not let entitty target entities of same player...
            //if (entity.getOwner() == otherEntity.getOwner())
            //    continue;

            float xDiff = entity.getSprite().getX() - otherEntity.getSprite().getX();
            float yDiff = entity.getSprite().getY() - otherEntity.getSprite().getY();

            float distanceSquared = xDiff * xDiff + yDiff * yDiff;

            // Do not let entity target entities outside of range
            if (distanceSquared > tileRange * tileRange)
                continue;

            if (distanceSquared < minDistanceSquared) {
                minDistanceSquared = distanceSquared;
                closestEntity = interactableOtherEntity;
            }
        }

        return closestEntity;
    }

    default boolean isNewTargetEntityRequired(Entity<? extends InteractableEntityType<?>> currentTargetEntity) {
        if (currentTargetEntity == null)
            return true;

        float currentTargetsHealth = EntityType.getProperty(currentTargetEntity, InteractableEntityType.HEALTH_LABEL);

        return currentTargetsHealth <= 0;
    }

    default float damageHealth(float health, float damage) {
        return health - damage;
    }
}
