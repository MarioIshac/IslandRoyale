package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.InteractableEntityType;

public abstract class ControllableEntityType<T extends ControllableEntityType<T>> extends InteractableEntityType<T> {
    public static final String MOVING_DIRECTORY = "moving/";

    public static final float MOVEMENT_SPEED_MULTIPLIER = 1.1f;

    @Override
    public void configureEditor(Entity<T> entity, VerticalGroup verticalGroup) {

    }

    /**
     * Represents the amount of tiles per second this entity moves at level 1.
     */
    private float baseMovementSpeed;

    public void setDefaultSpeed(Entity<T> entity) {
        int level = getProperty(entity, LEVEL_LABEL);
        float movementSpeedPerSec = applyMovementSpeedMultiplier(level);

        entity.setSpeed(movementSpeedPerSec);
    }

    private float applyMovementSpeedMultiplier(int level) {
        return InteractableEntityType.applyMultiplier(level, getBaseMovementSpeed(), MOVEMENT_SPEED_MULTIPLIER);
    }

    public float getBaseMovementSpeed() {
        return baseMovementSpeed;
    }
}
