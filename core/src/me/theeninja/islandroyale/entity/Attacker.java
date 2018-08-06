package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.bullet.BulletProjectile;
import me.theeninja.islandroyale.entity.bullet.BulletProjectileType;

/**
 *
 * @param <A>
 * @param <B>
 * @param <C>
 * @param <D>
 */
public interface Attacker<A extends BulletProjectile<A, B, C, D>, B extends BulletProjectileType<A, B, C, D>, C extends InteractableEntity<C, D> & Attacker, D extends InteractableEntityType<C, D>> {
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
    default InteractableEntity<?, ?> getNewTargetEntity(C entity, MatchMap matchMap) {
        // We keep the minimum distance SQUARED in order to improve performance
        float minDistanceSquared = Float.MAX_VALUE;
        InteractableEntity<?, ?> closestEntity = null;

        for (int entityPriority = 0; entityPriority < EntityType.NUMBER_OF_PRIORITIES; entityPriority++) {
            Array<Entity<?, ?>> priorityEntities = matchMap.getCertainPriorityEntities(entityPriority);

            for (Entity<?, ?> otherEntity : priorityEntities) {
                if (!(otherEntity.getEntityType() instanceof InteractableEntityType))
                    continue;

                InteractableEntity<?, ?> interactableOtherEntity = (InteractableEntity<?, ?>) otherEntity;

                // Do not let the entity target itself
                if (entity == interactableOtherEntity)
                    continue;

                // Do not let entitty target entities of same player
                //if (entity.getOwner() == otherEntity.getOwner())
                //    continue;

                float xDiff = entity.getSprite().getX() - otherEntity.getSprite().getX();
                float yDiff = entity.getSprite().getY() - otherEntity.getSprite().getY();

                float distanceSquared = xDiff * xDiff + yDiff * yDiff;

                //System.out.println("Against Entity " + entity.getEntityType().getName() + "> Distance = " + distanceSquared + " Range = " + (getRange() * getRange()));

                // Do not let entity target entities outside of range
                if (distanceSquared > getRange() * getRange())
                    continue;

                if (distanceSquared < minDistanceSquared) {
                    minDistanceSquared = distanceSquared;
                    closestEntity = interactableOtherEntity;
                }
            }
        }

        return closestEntity;
    }

    /**
     * @param attackerEntity The entitiy that is currently targeting attackedEntity.
     * @return Whether the attacking entity has launched enough attacks at the attacked entity to guarantee that its
     *         health will be less than or equal to 0 without the need for more launches of attacks. Note that the
     *         health of the attacked entity need not be 0 at the time this method is called for it to return true,
     *         as projectiles that have been launched but have not yet landed are taken into consideration.
     */
    default boolean isNewTargetEntityRequired(C attackerEntity) {
        if (getTargetEntity() == null)
            return true;

        boolean isCurrentTargetDead = getTargetEntity().getHealth() <= 0;

        float currentDistanceSquared = Entity.rangeBetweenSquared(attackerEntity, getTargetEntity());
        float rangeSquared = attackerEntity.getRange() * attackerEntity.getRange();
        boolean isTargetOutOfRange = currentDistanceSquared > rangeSquared;

        System.out.println("Current Targets Health " + getTargetEntity().getHealth());
        System.out.println("Current Range Squared " + rangeSquared);
        System.out.println("Current Target Distance " + currentDistanceSquared);

        return isCurrentTargetDead || isTargetOutOfRange;
    }

    default float damageHealth(float health, float damage) {
        return health - damage;
    }

    default A newProjectile(C attackerEntity) {
        B projectileType = EntityType.getEntityType(getStaticProjectileID());

        float attackerCenterX = attackerEntity.getSprite().getX() + attackerEntity.getSprite().getWidth() / 2;
        float attackerCenterY = attackerEntity.getSprite().getY() + attackerEntity.getSprite().getHeight() / 2;

        A projectile = newGenericProjectile(projectileType, attackerEntity.getOwner(), attackerCenterX, attackerCenterY, attackerEntity);

        projectile.setSpeed(projectile.getMovementSpeed());

        return projectile;
    }

    A newGenericProjectile(B projectileType, Player player, float x, float y, C attackerEntity);

    float getRange();
    void setRange(float range);

    float getTimeUntilAttack();
    void setTimeUntilAttack(float timeUntilAttack);

    InteractableEntity<?, ?> getTargetEntity();
    void setTargetEntity(InteractableEntity<?, ?> targetEntity);

    float getFireRate();
    void setFireRate(float fireRate);

    float getDamage();
    void setDamage(float damage);
}
