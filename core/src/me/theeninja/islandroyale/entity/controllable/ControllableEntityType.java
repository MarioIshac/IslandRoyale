package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

public abstract class ControllableEntityType<T extends ControllableEntityType<T>> extends InteractableEntityType<T> {
    public static final float MOVEMENT_SPEED_MULTIPLIER = 1.1f;

    public static final String TARGET_COORDS_LABEL = "targetCoords";
    public static final String TARGET_COORDS_DISPLAY_LABEL = "targetCoordsDisplay";
    public static final String TARGET_COORDS_PATH_LABEL = "targetCoordsPath";

    @Override
    public void setUp(Entity<T> entity) {
        super.setUp(entity);

        setProperty(entity, TARGET_COORDS_PATH_LABEL, new Array<Float>());
    }

    @Override
    public void configureEditor(Entity<T> entity, Table table) {
        Label targetHeader = new Label("Target", MatchScreen.FLAT_EARTH_SKIN);

        // Empty text will be updated later in check method
        TextButton targetCoordsLabel = new TextButton("", MatchScreen.FLAT_EARTH_SKIN);
        setProperty(entity, TARGET_COORDS_DISPLAY_LABEL, targetCoordsLabel);

        TextButton resetTargetButton = new TextButton("Reset", MatchScreen.FLAT_EARTH_SKIN);
        InputListener resetTargetListener = new ResetTargetListener(entity);

        resetTargetButton.addListener(resetTargetListener);

        table.add(targetHeader).colspan(2).row();
        table.add(targetCoordsLabel);
        table.add(resetTargetButton);
    }

    private static final String NO_TARGET_COORDS_SIGNAL = "None";

    private String stringify(Vector3 vector3) {
        return  Math.round(vector3.x) + " | " +
                Math.round(vector3.y) + " | " +
                Math.round(vector3.z) + " | ";
    }

    @Override
    public void check(Entity<T> entity, float delta, Player player, MatchMap matchMap) {
        TextButton textButton = getProperty(entity, TARGET_COORDS_DISPLAY_LABEL);
        Vector3 targetCoords = getProperty(entity, TARGET_COORDS_LABEL);

        textButton.setText(targetCoords == null ? NO_TARGET_COORDS_SIGNAL : stringify(targetCoords));
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
