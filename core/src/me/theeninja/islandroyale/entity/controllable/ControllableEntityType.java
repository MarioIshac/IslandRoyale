package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.InteractableEntityType;
import me.theeninja.islandroyale.entity.Skins;
import me.theeninja.islandroyale.gui.screens.MatchScreen;
import me.theeninja.islandroyale.gui.screens.PathSelectionInputListener;

public abstract class ControllableEntityType<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends InteractableEntityType<A, B> {
    @Override
    public void configureEditor(A entity, Table table) {
        Label targetHeader = new Label(NO_TARGET_COORDS_SIGNAL, Skins.getInstance().getFlatEarthSkin());

        InputListener newTargetListener = new NewTargetListener<>(entity);
        entity.getTargetSelector().addListener(newTargetListener);

        InputListener resetTargetListener = new ResetTargetListener<>(entity);
        entity.getTargetResettor().addListener(resetTargetListener);

        table.add(targetHeader).colspan(2).row();
        table.add(entity.getTargetSelector());
        table.add(entity.getTargetResettor());
    }

    private double calculateAngle(float x, float y) {
        double currentAngle = Math.atan(y / x);

        // Arc tangent only returns in 1st and 4th quadrant despite x having the possibility of being negative
        // Adjust to 3rd and 2nd quadrant respectively if x is negative
        if (x < 0)
            currentAngle += Math.PI;

        return currentAngle;
    }

    private static final double EIGHTH_OF_CIRCLE = Math.PI / 4;

    private double roundToNearestEighthAngle(double currentAngle) {
        return Math.round(currentAngle / EIGHTH_OF_CIRCLE) * EIGHTH_OF_CIRCLE;
    }

    private void updateDirection(A entity, Vector2 targetPathComponent) {
        float xDiff = targetPathComponent.x - entity.getSprite().getX();
        float yDiff = targetPathComponent.y - entity.getSprite().getY();

        float angle = (float) calculateAngle(xDiff, yDiff);

        entity.setDirection(angle);
    }

    private static final String NO_TARGET_COORDS_SIGNAL = "None";

    private String stringify(Vector2 vector2) {
        return  Math.round(vector2.x) + " | " +
                Math.round(vector2.y);
    }

    @Override
    protected void setUpEntity(A entity) {
        entity.setMovementSpeed(getBaseMovementSpeed());
    }

    /**
     * Represents the amount of tiles per second this entity moves at level 1.
     */
    private float baseMovementSpeed;

    public float getBaseMovementSpeed() {
        return baseMovementSpeed;
    }
}
