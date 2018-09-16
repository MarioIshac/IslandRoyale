package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.EntityAttribute;
import me.theeninja.islandroyale.entity.InteractableEntity;
import me.theeninja.islandroyale.entity.Skins;
import me.theeninja.islandroyale.gui.screens.Match;
import me.theeninja.islandroyale.gui.screens.PathSelectionInputListener;

public abstract class ControllableEntity<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends InteractableEntity<A, B> {
    private static final String COORDINATES_SELECT = "Route To";
    private static final String COORDINATES_RESET = "Reset";

    private TextButton targetSelector;
    private TextButton targetResettor;

    @Override
    public void initializeConstructorDependencies() {
        super.initializeConstructorDependencies();

        this.targetSelector = new TextButton(COORDINATES_SELECT, Skins.getInstance().getFlatEarthSkin());
        this.targetResettor = new TextButton(COORDINATES_RESET, Skins.getInstance().getFlatEarthSkin());
        this.pathSelectionInputListener = new PathSelectionInputListener<>(getReference());
    }

    @EntityAttribute
    private float movementSpeed;

    @EntityAttribute
    private float productionTime;

    public ControllableEntity(B entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);

        setMovementSpeed(getEntityType().getBaseMovementSpeed());
        setProductionTime(getEntityType().getProductionTime());
    }

    public TextButton getTargetSelector() {
        return targetSelector;
    }

    private final Array<Vector2> path = new Array<>();
    private int pathIndex = 0;

    private static final float EPSILON = 1e-3f;

    private float getXDifference(Vector2 pathComponent) {
        float xDifference = pathComponent.x - getX();

        xDifference = Math.abs(xDifference) < EPSILON ? 0 : xDifference;

        System.out.println("X Difference " + xDifference);

        return xDifference;
    }

    private float getYDifference(Vector2 pathComponent) {
        float yDifference = pathComponent.y - getY();

        yDifference = Math.abs(yDifference) < EPSILON ? 0 : yDifference;

        System.out.println("Y Difference " + yDifference);

        return yDifference;
    }

    public float orient(Vector2 pathComponent) {
        float xDiff = getXDifference(pathComponent);
        float yDiff = getYDifference(pathComponent);

        return (float) Math.atan2(yDiff, xDiff);
    }

    private boolean isPastX(double angleToComponent, double xDiff) {
        boolean angleInPositiveXQuadrants = (-Math.PI / 2 <= angleToComponent && angleToComponent <= Math.PI / 2);
        System.out.println("Is Past X: " + (angleInPositiveXQuadrants ? (xDiff <= 0) : (xDiff >= 0)));

        return angleInPositiveXQuadrants ? (xDiff <= 0) : (xDiff >= 0);

    }

    private boolean isPastY(double angleToComponent, double yDiff) {
        boolean angleInPositiveYQuadrants = (0 <= angleToComponent) && (angleToComponent <= Math.PI);
        System.out.println("Is Past Y: " + (angleInPositiveYQuadrants ? (yDiff <= 0) : (yDiff >= 0)));

        return angleInPositiveYQuadrants ? (yDiff <= 0) : (yDiff >= 0);
    }

    private boolean isEndOfPathReached() {
        return getPathIndex() == getPath().size;
    }

    void updateMoveAttributes() {
        // No path assigned
        if (getPath().size == 0)
            return;

        // No more path to travel
        if (isEndOfPathReached()) {
            setSpeed(0);

            return;
        }

        Vector2 nextPathComponent = getPath().get(getPathIndex());

        // Indicates that we have passed the path component we are on route to, adjust course with respect
        // to next available path component.
        if (isPastX(getDirection(), getXDifference(nextPathComponent)) &&
                isPastY(getDirection(), getYDifference(nextPathComponent))) {

            setPathIndex(getPathIndex() + 1);

            // No more path to travel
            if (isEndOfPathReached()) {
                setSpeed(0);

                return;
            }
        }

        System.out.println("Path Index " + getPathIndex());

        float angleToComponent = orient(getPath().get(getPathIndex()));

        System.out.println(("Angle To Component " + angleToComponent));

        setDirection(angleToComponent);

        // In case user upgrades entity during travel of entity -> movement speed changes -> update required for actual speed
        setSpeed(getMovementSpeed());

        System.out.println();
    }

    public Array<Vector2> getPath() {
        return path;
    }

    public TextButton getTargetResettor() {
        return targetResettor;
    }

    private PathSelectionInputListener<A, B> pathSelectionInputListener;

    @Override
    public void check(float delta, Player player, Match match) {
        super.check(delta, player, match);

        boolean hasPath = getPath().size != 0;

        if (hasPath) {
            Vector2 pathEnd = getPath().get(path.size - 1);
            getTargetSelector().setText(pathEnd.toString());
        }

        else
            getTargetSelector().setText(COORDINATES_SELECT);
    }

    private static final Color OLD_SELF_PATH_COLOR = Color.RED;
    private static final Color NEW_SELF_PATH_COLOR = Color.BLUE;
    private static final Color OTHER_PATH_COLOR = Color.PINK;

    private static void drawPath(Array<Vector2> path, Color color, ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(color);

        for (Vector2 pathComponent : path)
            shapeRenderer.circle(pathComponent.x, pathComponent.y, 2);
    }

    @Override
    public void present(Camera mapCamera, Stage hudStage, ShapeRenderer shapeRenderer) {
        super.present(mapCamera, hudStage, shapeRenderer);

        if (!PathSelectionInputListener.areAnyInUse()) {
            System.out.println("None are in use");
            return;
        }

        System.out.println("Currently Shown " + PathSelectionInputListener.getCurrentlyShownEntity());

        shapeRenderer.setProjectionMatrix(mapCamera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        if (getPathSelectionInputListener().isInUse()) {
            getPathSelectionInputListener().update();

            drawPath(this.getPath(), OLD_SELF_PATH_COLOR, shapeRenderer);
            drawPath(getPathSelectionInputListener().getPath(), NEW_SELF_PATH_COLOR, shapeRenderer);
        }

        else
            drawPath(getPathSelectionInputListener().getPath(), OTHER_PATH_COLOR, shapeRenderer);

        shapeRenderer.end();
    }

    public PathSelectionInputListener<A, B> getPathSelectionInputListener() {
        return pathSelectionInputListener;
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

    public float getProductionTime() {
        return productionTime;
    }

    public void setProductionTime(float productionTime) {
        this.productionTime = productionTime;
    }
}
