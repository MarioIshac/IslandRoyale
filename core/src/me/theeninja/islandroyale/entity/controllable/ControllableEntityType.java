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
        Label targetHeader = new Label(NO_TARGET_COORDS_SIGNAL, MatchScreen.FLAT_EARTH_SKIN);

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

    private void updateDirection(Entity<T> entity, Vector2 targetPathComponent) {
        float xDiff = targetPathComponent.x - entity.getSprite().getX();
        float yDiff = targetPathComponent.y - entity.getSprite().getY();

        float angle = (float) calculateAngle(xDiff, yDiff);

        entity.setDirection(angle);
    }

    private final static float EPSILON = 1e-3f;

    public void updateMoveAttributes(Entity<T> entity) {
        Array<Vector2> targetPath = getProperty(entity, TARGET_COORDS_PATH_LABEL);

        if (targetPath != null) {
            setDefaultSpeed(entity);

            boolean noMorePathLeft = false;

            int nextAvailableIndex = 0;
            while (targetPath.get(nextAvailableIndex) == null) {
                nextAvailableIndex++;
                if (nextAvailableIndex == targetPath.size) {
                    noMorePathLeft = true;
                    break;
                }
            }

            if (noMorePathLeft) {
                entity.setSpeed(0);
                setProperty(entity, TARGET_COORDS_PATH_LABEL, null);
                return;
            }



            Vector2 nextPathComponent = targetPath.get(nextAvailableIndex);

            float xDiff = entity.getSprite().getX() - nextPathComponent.x;
            float yDiff = entity.getSprite().getY() - nextPathComponent.y;

            xDiff = Math.abs(xDiff) < EPSILON ? 0 : xDiff;
            yDiff = Math.abs(yDiff) < EPSILON ? 0 : yDiff;

            double currentAngle = entity.getDirection();
            currentAngle = roundToNearestEighthAngle(currentAngle);






            boolean angleInPositiveYQuadrants = (0 <= currentAngle) && (currentAngle <= Math.PI);
            boolean pastY = angleInPositiveYQuadrants ? (yDiff >= 0) : (yDiff <= 0);



            boolean angleInPositiveXQuadrants = (currentAngle <= Math.PI / 2) || (3 * Math.PI / 2 <= currentAngle);
            boolean pastX = angleInPositiveXQuadrants ? (xDiff >= 0) : (xDiff <= 0);



            // Indicates that we have passed the closest path component, adjust course with respect
            // to next available path component.
            if (pastY && pastX) {
                targetPath.set(nextAvailableIndex, null);

                nextAvailableIndex++;

                if (targetPath.size == nextAvailableIndex)
                    entity.setSpeed(0);
                else {
                    nextPathComponent = targetPath.get(nextAvailableIndex);
                    updateDirection(entity, nextPathComponent);
                }
            }

            /*Vector2 closestPathComponent = null;
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





            if (xDiff < 0)
                angle += Math.PI;



            entity.setDirection((float) angle);
            setDefaultSpeed(entity); */
        }
    }

    @Override
    public void present(Entity<T> entity, Camera mapCamera, Stage hudStage) {
        super.present(entity, mapCamera, hudStage);

        PathSelectionInputListener pathSelectionInputListener = getProperty(entity, PATH_SELECTOR_LISTENER_TABLE);
        Array<Vector2> path = getProperty(entity, TARGET_COORDS_PATH_LABEL);

        // Indicates that others paths are present
        if (path != null && PathSelectionInputListener.CURRENTLY_SHOWN_ENTITY != null) {
            // Represents whether the entity currently undergoing a new path selection, if any entity at all,
            // is this entity. If no entity is undergoing a new path selection, this is false.
            boolean isThisEntityCurrentlyShown = PathSelectionInputListener.CURRENTLY_SHOWN_ENTITY == entity;

            shapeRenderer.setColor(isThisEntityCurrentlyShown ? Color.PINK : Color.BLUE);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (Vector2 pathComponent : path)
                shapeRenderer.circle(pathComponent.x, pathComponent.y, 2);

            shapeRenderer.end();
        }

        // Indicates that a new path is being selected
        if (pathSelectionInputListener != null) {
            pathSelectionInputListener.update();

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (Vector2 pathComponent : pathSelectionInputListener.getPath())
                shapeRenderer.circle(pathComponent.x, pathComponent.y, 2);

            shapeRenderer.end();
        }
    }

    private static final String NO_TARGET_COORDS_SIGNAL = "None";

    private String stringify(Vector2 vector2) {
        return  Math.round(vector2.x) + " | " +
                Math.round(vector2.y);
    }

    @Override
    public void check(Entity<T> entity, float delta, Player player, MatchMap matchMap) {
        super.check(entity, delta, player, matchMap);

        TextButton textButton = getProperty(entity, TARGET_COORDS_DISPLAY_LABEL);
        Array<Vector2> path = getProperty(entity, TARGET_COORDS_PATH_LABEL);

        textButton.setText(path == null ?
                NO_TARGET_COORDS_SIGNAL :
                path.get(path.size - 1) == null ?
                        NO_TARGET_COORDS_SIGNAL :
                        stringify(path.get(path.size - 1)));
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
