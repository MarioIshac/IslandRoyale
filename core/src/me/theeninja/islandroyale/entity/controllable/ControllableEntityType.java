package me.theeninja.islandroyale.entity.controllable;

import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;

public abstract class ControllableEntityType<T extends ControllableEntityType<T>> extends EntityType<T> {
    public static final String MOVING_DIRECTORY = "moving/";

    public static final float MOVEMENT_SPEED_MULTIPLIER = 1.1f;

    /**
     * Represents the amount of tiles per second this entity moves at level 1.
     */
    private float baseMovementSpeed;

    @Override
    public void initialize(Entity<T> entity) {

    }

    @Override
    public void check(Entity<T> entity, float delta, Player player, MatchMap matchMap) {
        updateMoveAttributes(entity, delta);
    }

    public void updateMoveAttributes(Entity<T> entity, float delta) {

    }

    private float applyMovementSpeedMultiplier(int level) {
        return EntityType.applyMultiplier(level, baseMovementSpeed, MOVEMENT_SPEED_MULTIPLIER);
    }
}
