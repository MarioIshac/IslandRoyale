package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;

public abstract class MovingEntityType<T extends MovingEntityType<T>> extends EntityType<T> {
    public static final String MOVING_DIRECTORY = "moving/";

    public static final float MOVEMENT_SPEED_MULTIPLIER = 1.1f;

    /**
     * Represents the amount of tiles per second this entity moves at level 1.
     */
    private float baseMovementSpeed;

    private static final String X_LABEL = "x";
    private static final String Y_LABEL = "y";
    private static final String ANGLE_LABEL = "angle";

    @Override
    public void initialize(Entity<T> entity) {
        entity.getProperties().put(X_LABEL, -1f);
        entity.getProperties().put(Y_LABEL, -1f);
        entity.getProperties().put(ANGLE_LABEL, -1f);
    }

    public void move(Entity<T> entity, float delta) {
        float xPos = (float) entity.getProperties().get(X_LABEL);
        float yPos = (float) entity.getProperties().get(Y_LABEL);
        float angle = (float) entity.getProperties().get(ANGLE_LABEL);

        float speed = applyMovementSpeedMultiplier(entity.getLevel());
        float distance = speed * delta;

        float xOffset = (float) (Math.sin(angle) * distance);
        float yOffset = (float) (Math.cos(angle) * distance);

        entity.getProperties().put(X_LABEL, xPos + xOffset);
        entity.getProperties().put(Y_LABEL, yPos + yOffset);
    }

    private float applyMovementSpeedMultiplier(int level) {
        return EntityType.applyMultiplier(level, baseMovementSpeed, MOVEMENT_SPEED_MULTIPLIER);
    }
}
