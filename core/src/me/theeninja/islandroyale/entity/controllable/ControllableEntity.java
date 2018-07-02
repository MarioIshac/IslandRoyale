package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.EntityAttribute;
import me.theeninja.islandroyale.entity.InteractableEntity;
import me.theeninja.islandroyale.entity.Skins;
import me.theeninja.islandroyale.gui.screens.PathSelectionInputListener;

public abstract class ControllableEntity<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends InteractableEntity<A, B> {
    private static final String COORDINATES_SELECT = "Route To";
    private static final String COORDINATES_RESET = "Reset";

    private final TextButton targetSelector = new TextButton(COORDINATES_SELECT, Skins.getInstance().getFlatEarthSkin());
    private final TextButton targetResettor = new TextButton(COORDINATES_RESET, Skins.getInstance().getFlatEarthSkin());

    @EntityAttribute
    private float movementSpeed;

    private final Array<Vector2> path = new Array<>();
    private int pathIndex = -1;

    public ControllableEntity(B entityType, Player owner, float x, float y) {
        super(entityType, owner, x, y);
    }

    public TextButton getTargetSelector() {
        return targetSelector;
    }

    public void updateMoveAttributes() {
        // No path assigned
        if (getPath().size == 0)
            return;

        if (getPathIndex() == getPath().size) {
            // No more path to travel, speed = 0
            setSpeed(0);

            return;
        }

        Vector2 nextPathComponent = getPath().get(getPathIndex());

        float xDiff = getSprite().getX() - nextPathComponent.x;
        float yDiff = getSprite().getY() - nextPathComponent.y;

        xDiff = Math.abs(xDiff) < 1e-3 ? 0 : xDiff;
        yDiff = Math.abs(yDiff) < 1e-3 ? 0 : yDiff;

        double currentAngle = getDirection();
        //currentAngle = roundToNearestEighthAngle(currentAngle);

        boolean angleInPositiveYQuadrants = (0 <= currentAngle) && (currentAngle <= Math.PI);
        boolean pastY = angleInPositiveYQuadrants ? (yDiff >= 0) : (yDiff <= 0);

        boolean angleInPositiveXQuadrants = (currentAngle <= Math.PI / 2) || (3 * Math.PI / 2 <= currentAngle);
        boolean pastX = angleInPositiveXQuadrants ? (xDiff >= 0) : (xDiff <= 0);

        // Indicates that we have passed the closest path component, adjust course with respect
        // to next available path component.
        if (pastY && pastX) {
            this.pathIndex += 1;

            /*if (targetPath.size == nextAvailableIndex)
                entity.setSpeed(0);
            else {
                nextPathComponent = targetPath.get(nextAvailableIndex);
                updateDirection(entity, nextPathComponent);
            }*/
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

    public Array<Vector2> getPath() {
        return path;
    }

    public TextButton getTargetResettor() {
        return targetResettor;
    }

    private PathSelectionInputListener<A, B> pathSelectionInputListener = null;

    @Override
    public void check(float delta, Player player, MatchMap matchMap) {
        super.check(delta, player, matchMap);

        boolean hasPath = getPath().size == 0;

        if (!hasPath)
            getTargetSelector().setText(COORDINATES_SELECT);
        else {
            Vector2 pathEnd = path.get(path.size - 1);
            getTargetSelector().setText(pathEnd.toString());
        }
    }

    @Override
    public void present(Camera projector, Stage stage, ShapeRenderer shapeRenderer) {
        super.present(projector, stage, shapeRenderer);

        // Indicates that others paths belonging to different entitiesa re present
        if (path != null && PathSelectionInputListener.CURRENTLY_SHOWN_ENTITY != null) {
            // Represents whether the entity currently undergoing a new path selection, if any entity at all,
            // is this entity. If no entity is undergoing a new path selection, this is false.
            boolean isThisEntityCurrentlyShown = PathSelectionInputListener.CURRENTLY_SHOWN_ENTITY == this;

            shapeRenderer.setColor(isThisEntityCurrentlyShown ? Color.PINK : Color.BLUE);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (Vector2 pathComponent : getPath())
                shapeRenderer.circle(pathComponent.x, pathComponent.y, 2);

            shapeRenderer.end();
        }

        // Indicates that a new path is being selected
        if (getPathSelectionInputListener() != null) {
            getPathSelectionInputListener().update();

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            for (Vector2 pathComponent : getPathSelectionInputListener().getPath())
                shapeRenderer.circle(pathComponent.x, pathComponent.y, 2);

            shapeRenderer.end();
        }
    }

    public PathSelectionInputListener<A, B> getPathSelectionInputListener() {
        return pathSelectionInputListener;
    }

    public void setPathSelectionInputListener(PathSelectionInputListener<A, B> pathSelectionInputListener) {
        this.pathSelectionInputListener = pathSelectionInputListener;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setMovementSpeed(float movementSpeed) {
        this.movementSpeed = movementSpeed;
    }
}
