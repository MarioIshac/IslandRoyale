package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.math.Vector2;
import me.theeninja.islandroyale.MatchMap;

public interface Attacker<T extends InteractableEntityType<T>> {
    String ATTACKING_TARGET_LABEL = "attackingTarget";

    int getStaticProjectileID();

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
            if (entity == interactableOtherEntity)
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

    /**
     * @param attackerEntity The entitiy that is currently targeting attackedEntity.
     * @param attackedEntity The entity that is currently being targeted by entity.
     * @return Whether the attacking entity has launched enough attacks at the attacked entity to guarantee that its
     *         health will be less than or equal to 0 without the need for more launches of attacks. Note that the
     *         health of the attacked entity need not be 0 at the time this method is called for it to return true,
     *         as projectiles that have been launched but have not yet landed are taken into consideration.
     */
    default <AttackerType extends InteractableEntityType<AttackerType> & Attacker> boolean isNewTargetEntityRequired(
            Entity<AttackerType> attackerEntity,
            Entity<? extends EntityType<?>> attackedEntity) {
        if (attackedEntity == null)
            return true;

        float currentTargetsHealth = EntityType.getProperty(attackedEntity, InteractableEntityType.HEALTH_LABEL);
        boolean isCurrentTargetDead = currentTargetsHealth <= 0;

        float currentDistanceSquared = Entity.rangeBetweenSquared(attackerEntity, attackedEntity);
        float baseRangeSquared = attackerEntity.getType().getBaseRange() * attackerEntity.getType().getBaseRange();
        boolean isTargetOutOfRange = currentDistanceSquared > baseRangeSquared;

        System.out.println("Current Targets Health " + currentTargetsHealth);
        System.out.println("Current Range Squared " + baseRangeSquared);
        System.out.println("Current Target Distance " + currentDistanceSquared);

        return isCurrentTargetDead || isTargetOutOfRange;
    }

    default float damageHealth(float health, float damage) {
        return health - damage;
    }

    default <AttackerType extends InteractableEntityType<AttackerType> & Attacker> Entity<StaticProjectileEntityType> newProjectile(
            Entity<AttackerType> attackerEntity,
            Entity<? extends InteractableEntityType<?>> attackedEntity) {
        StaticProjectileEntityType projectileType = EntityType.getEntityType(getStaticProjectileID());

        Vector2 pos = new Vector2(
            attackerEntity.getSprite().getX(),
            attackerEntity.getSprite().getY()
        );

        Entity<StaticProjectileEntityType> projectile = new Entity<>(projectileType, attackerEntity.getOwner(), pos);
        projectileType.provide(projectile, attackerEntity, attackedEntity);

        projectile.setSpeed(projectileType.getMovementSpeed());

        return projectile;
    }
}
