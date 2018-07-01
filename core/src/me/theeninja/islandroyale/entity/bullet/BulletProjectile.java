package me.theeninja.islandroyale.entity.bullet;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.*;

public abstract class BulletProjectile<A extends BulletProjectile<A, B, C, D>, B extends BulletProjectileType<A, B, C, D>, C extends InteractableEntity<C, D> & Attacker, D extends InteractableEntityType<C, D>> extends Entity<A, B> {
    private final C initiatorEntity;
    private final InteractableEntity<?, ?> targetEntity;

    @EntityAttribute
    private float damage;

    @EntityAttribute
    private float movementSpeed;

    public BulletProjectile(B entityType, Player owner, float x, float y, C initiatorEntity) {
        super(entityType, owner, x, y);
        this.initiatorEntity = initiatorEntity;

        /* We must store this ina  new variable rather than referencing the target through getTargetEntity since
        the target may be updated after the static bullet entity is fired, however all previously initated routes
        should remain the same.
         */
        this.targetEntity = getInitiatorEntity().getTargetEntity();
    }

    private boolean hasCollided = false;

    @Override
    public boolean shouldRemove() {
        return hasCollided;
    }

    public void setCollided(boolean hasCollided) {
        this.hasCollided = hasCollided;
    }

    @Override
    public void check(float delta, Player player, MatchMap matchMap) {
        float bottomXOfTarget = (getTargetEntity().getSprite().getX());
        float bottomYOfTarget = (getTargetEntity().getSprite().getY());

        float upperXOfTarget = bottomXOfTarget + getTargetEntity().getEntityType().getTileWidth();
        float upperYOfTarget = bottomYOfTarget + getTargetEntity().getEntityType().getTileHeight();

        float bottomXOfProjectile = (getSprite().getX());
        float bottomYOfProjectile = (getSprite().getY());

        float upperXOfProjectile = bottomXOfProjectile + getTargetEntity().getEntityType().getTileWidth();
        float upperYOfProjectile = bottomYOfProjectile + getTargetEntity().getEntityType().getTileHeight();

        boolean isXContained = bottomXOfTarget <= bottomXOfProjectile && upperXOfProjectile <= upperXOfTarget;
        boolean isYContained = bottomYOfTarget <= bottomYOfProjectile && upperYOfProjectile <= upperYOfTarget;

        // Indicates that the target entity fully contains the bullet launched at it
        if (isXContained && isYContained) {
            setCollided(true);

            getTargetEntity().damage(getDamage());

            return;
        }

        float centerXOfTarget = (bottomXOfTarget + upperXOfTarget) / 2;
        float centerYOfTarget = (bottomYOfTarget + upperYOfTarget) / 2;

        float centerXOfProjectile = (bottomXOfProjectile + upperXOfProjectile) / 2;
        float centerYOfProjectile = (bottomYOfProjectile + upperYOfProjectile) / 2;

        float yDistance = centerYOfTarget - centerYOfProjectile;
        float xDistance = centerXOfTarget - centerXOfProjectile;

        // Arc tangent only returns valid value in quadrant 1 or 4, i.e other entity has to be
        // to the right of this entity. Solution is two if statements below
        float resultingAngle = (float) Math.atan(yDistance / xDistance);

        // Shift from 1st to 3rd quadrant OR 4th to 2nd quadrant (handles both cases).
        if (xDistance < 0)
            resultingAngle += Math.PI;

        setDirection(resultingAngle);
        getSprite().setRotation((float) Math.toDegrees(resultingAngle) - 45);
    }

    @Override
    public void present(Camera projector, Stage stage) {

    }

    public C getInitiatorEntity() {
        return initiatorEntity;
    }

    public InteractableEntity<?, ?> getTargetEntity() {
        return targetEntity;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }
}
