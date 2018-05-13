package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
import me.theeninja.islandroyale.gui.screens.MatchScreen;
import me.theeninja.islandroyale.gui.screens.PathSelectionInputListener;

public abstract class ControllableEntityType<T extends ControllableEntityType<T>> extends InteractableEntityType<T> {
    public static final float MOVEMENT_SPEED_MULTIPLIER = 1.1f;

    public static final String TARGET_COORDS_DISPLAY_LABEL = "targetCoordsDisplay";

    /**
     * If the corresponding value is null, that indicates that no new path has been selected.
     */
    public static final String TARGET_COORDS_PATH_LABEL = "targetCoordsPath";

    /**
     * If the corresponding value is null, that indicates that there is no new path being selected.
     * Do note that the current entity can have both {@value TARGET_COORDS_PATH_LABEL} and this non-null.
     * That would mean the user is reselecting a path for an entity that already has a path.
     */
    public static final String PATH_SELECTOR_LISTENER_TABLE = "pathSelectorListener";

    @Override
    public void setUp(Entity<T> entity) {
        super.setUp(entity);
    }

    @Override
    public void configureEditor(Entity<T> entity, Table table) {
        Label targetHeader = new Label("Route", MatchScreen.FLAT_EARTH_SKIN);

        // Empty text will be updated later in check method
        TextButton targetCoordsLabel = new TextButton("Route To", MatchScreen.FLAT_EARTH_SKIN);
        setProperty(entity, TARGET_COORDS_DISPLAY_LABEL, targetCoordsLabel);
        InputListener newTargetListener = new NewTargetListener(targetCoordsLabel, entity);
        targetCoordsLabel.addListener(newTargetListener);

        TextButton resetTargetButton = new TextButton("Reset", MatchScreen.FLAT_EARTH_SKIN);
        InputListener resetTargetListener = new ResetTargetListener(entity);

        resetTargetButton.addListener(resetTargetListener);

        table.add(targetHeader).colspan(2).row();
        table.add(targetCoordsLabel);
        table.add(resetTargetButton);
    }

    public void updateMoveAttributes(Entity<T> entity) {
        Array<Vector2> targetPath = getProperty(entity, TARGET_COORDS_PATH_LABEL);

        if (targetPath != null) {
            float minDistance2 = Float.MAX_VALUE;
            Vector2 closestPathComponent = null;
            Vector2 entityLocation = new Vector2(entity.getSprite().getX(), entity.getSprite().getY());

            for (Vector2 pathComponent : targetPath) {
                if (closestPathComponent == null) {
                    closestPathComponent = pathComponent;
                    continue;
                }

                float distance2 = closestPathComponent.dst2(pathComponent);

                if (pathComponent.dst2(entityLocation) < minDistance2) {
                    closestPathComponent = pathComponent;
                    minDistance2 = distance2;
                }
            }

            float xDiff = closestPathComponent.x - entityLocation.x;
            float yDiff = closestPathComponent.y - entityLocation.y;

            double angle = Math.atan(yDiff / xDiff);

            System.out.println("X difference: " + xDiff);
            System.out.println("Y difference: " + yDiff);
            System.out.println("Angle " + Math.toDegrees(angle));

            if (xDiff < 0)
                angle += Math.PI;

            System.out.println("Angle Modified " + Math.toDegrees(angle));

            entity.setDirection((float) angle);
            setDefaultSpeed(entity);
        }
    }

    @Override
    public void present(Entity<T> entity, Camera mapCamera, Stage hudStage) {
        super.present(entity, mapCamera, hudStage);

        PathSelectionInputListener pathSelectionInputListener = getProperty(entity, PATH_SELECTOR_LISTENER_TABLE);
        Array<Vector2> path = getProperty(entity, TARGET_COORDS_PATH_LABEL);

        // Indicates that a new path is being selected
        if (pathSelectionInputListener != null) {
            pathSelectionInputListener.update();

            SHAPE_RENDERER_PRESENTER.setColor(Color.RED);
            SHAPE_RENDERER_PRESENTER.begin(ShapeRenderer.ShapeType.Filled);

            for (Vector2 pathComponent : pathSelectionInputListener.getPath()) {
                SHAPE_RENDERER_PRESENTER.circle(pathComponent.x, pathComponent.y, 2);
            }

            SHAPE_RENDERER_PRESENTER.end();
        }

        if (path != null && PathSelectionInputListener.CURRENTLY_SHOWN_ENTITY != null) {
            SHAPE_RENDERER_PRESENTER.setColor(PathSelectionInputListener.CURRENTLY_SHOWN_ENTITY == entity ? Color.PINK : Color.BLUE);
            SHAPE_RENDERER_PRESENTER.begin(ShapeRenderer.ShapeType.Filled);

            for (Vector2 pathComponent : path) {
                SHAPE_RENDERER_PRESENTER.circle(pathComponent.x, pathComponent.y, 2);
            }

            SHAPE_RENDERER_PRESENTER.end();
        }
    }

    private static final String NO_TARGET_COORDS_SIGNAL = "None";

    private String stringify(Vector2 vector2) {
        return  Math.round(vector2.x) + " | " +
                Math.round(vector2.y);
    }

    @Override
    public void check(Entity<T> entity, float delta, Player player, MatchMap matchMap) {
        TextButton textButton = getProperty(entity, TARGET_COORDS_DISPLAY_LABEL);
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
