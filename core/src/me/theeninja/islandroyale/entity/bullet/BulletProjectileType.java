package me.theeninja.islandroyale.entity.bullet;

import me.theeninja.islandroyale.entity.Attacker;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.InteractableEntity;
import me.theeninja.islandroyale.entity.InteractableEntityType;

/**
 * The parameters are in the context of subclassing this abstract class.
 *
 * @param <A> The entity class associated with {@code B} (representing an entity).
 * @param <B> The subclass (representing an entity type).
 * @param <C> The entity class associated with {@code D} (representing an entity).
 * @param <D> The class representing the entity type that will fire a bullet bullet.
 */
public abstract class BulletProjectileType<A extends BulletProjectile<A, B, C, D>, B extends BulletProjectileType<A, B, C, D>, C extends InteractableEntity<C, D> & Attacker, D extends InteractableEntityType<C, D>> extends EntityType<A, B> {
    private float baseMovementSpeed;

    @Override
    protected void setUpEntity(A entity) {
        super.setUpEntity(entity);

        entity.setMovementSpeed(getBaseMovementSpeed());
    }

    @Override
    public int getDrawingPriority() {
        return 4;
    }

    public float getBaseMovementSpeed() {
        return baseMovementSpeed;
    }

    @Override
    protected int getBaseLevel(A entity) {
        // A level X attacker fires a level X bullet
        return entity.getInitiatorEntity().getLevel();
    }
}
