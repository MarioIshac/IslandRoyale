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

    /**
     * When a projectile is launched, it is assumed that the sprite is along the x-axis to guarantee that
     * the the projectile head and tail are both on the line containing the target entity and shooter entity.
     * <br />
     * However, in the situation where projectile sprite designers find it easier to design a projectile along a different line
     * (as opposed to the x-axis), {@code angleOffset} represents the angle offset in degrees of the line that the sprite is along relative to
     * the x-axis. For instance, a sprite along the y axis pointing upwards woould have an {@code angleOffset} of 90 degrees.
     */
    private int angleOffset;

    public float getBaseMovementSpeed() {
        return baseMovementSpeed;
    }

    @Override
    protected int getBaseLevel(A entity) {
        // A level X attacker fires a level X bullet
        return entity.getInitiatorEntity().getLevel();
    }

    public int getAngleOffset() {
        return angleOffset;
    }
}
