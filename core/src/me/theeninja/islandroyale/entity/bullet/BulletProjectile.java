package me.theeninja.islandroyale.entity.bullet;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.gui.screens.Match;

public abstract class BulletProjectile<A extends BulletProjectile<A, B, C, D>, B extends BulletProjectileType<A, B, C, D>, C extends InteractableEntity<C, D> & Attacker, D extends InteractableEntityType<C, D>> extends Entity<A, B> {
    private final C initiatorEntity;
    private final InteractableEntity<?, ?> targetEntity;

    private static final String BASE_DAMAGE_FIELD_NAME = "baseDamage";

    @EntityAttribute(BASE_DAMAGE_FIELD_NAME)
    private float damage;

    private static final String BASE_MOVEMENT_SPEED_FIELD_NAME = "baseMovementSpeed";

    @EntityAttribute(BASE_MOVEMENT_SPEED_FIELD_NAME)
    private float movementSpeed;

    public BulletProjectile(B entityType, Player owner, float x, float y, C initiatorEntity) {
        super(entityType, owner, x, y);
        this.initiatorEntity = initiatorEntity;

        /* We must store this ina  new variable rather than referencing the target through getTargetEntity since
        the target may be updated after the static bullet entity is fired, however all previously initated routes
        should remain the same.
         */
        this.targetEntity = getInitiatorEntity().getTargetEntity();

        setMovementSpeed(getEntityType().getBaseMovementSpeed());

        setLevel(getEntityType().getBaseLevel(getReference()));
    }

    private boolean hasCollided = false;

    @Override
    public boolean shouldRemove() {
        return this.hasCollided;
    }

    public void setCollided(boolean hasCollided) {
        this.hasCollided = hasCollided;
    }

    @Override
    public void check(float delta, Player player, Match match) {
        boolean doesProjectileOverlapTarget = overlap(this, getTargetEntity());

        if (doesProjectileOverlapTarget) {
            System.out.println("Does overlap");
            setCollided(true);

            getTargetEntity().damage(getDamage());

            return;
        }

        float centerXOfTarget = getTargetEntity().getOriginX();
        float centerYOfTarget = getTargetEntity().getOriginY();

        float yDistance = centerYOfTarget - getOriginX();
        float xDistance = centerXOfTarget - getOriginY();

        // Arc tangent only returns valid value in quadrant 1 or 4, i.e other entity has to be
        // to the right of this entity. Solution is two if statements below
        float resultingAngle = (float) Math.atan2(yDistance, xDistance);

        setDirection(resultingAngle);

        float resultingDegreeAngle = (float) Math.toDegrees(resultingAngle);

        // Apply angle offset to guarantee that any projectile head or tail is along the segment between target and shooter entity
        resultingDegreeAngle += getEntityType().getAngleOffset();

        setRotation(resultingDegreeAngle);
    }

    @Override
    public void present(Camera projector, Stage hudStage, ShapeRenderer shapeRenderer) {
        // TODO Apply/Add Support for Particle Effects
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
