package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.EntityListener;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;

import static com.badlogic.gdx.Input.Keys.*;

public class PathSelectionInputListener<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends EntityListener<A, B> {
    private boolean inLeft;
    private boolean inRight;
    private boolean inUp;
    private boolean inDown;

    private Vector3 pathComponentToAdd;

    public static ControllableEntity<?, ?> currentlyShownEntity = null;

    public PathSelectionInputListener(A entity) {
        super(entity);
    }

    public static ControllableEntity<?, ?> getCurrentlyShownEntity() {
        return currentlyShownEntity;
    }

    public static void setCurrentlyShownEntity(ControllableEntity<?, ?> currentlyShownEntity) {
        PathSelectionInputListener.currentlyShownEntity = currentlyShownEntity;
    }

    public void finish() {
        setCurrentlyShownEntity(null);

        revertToPlayerMap();
        getEntity().getStage().removeListener(this);
    }

    public void request() {
        setCurrentlyShownEntity(getEntity());

        getEntity().getStage().addListener(this);
        showFullMap();
    }

    public void copy() {
        System.out.println("Path");

        System.out.println();

        getEntity().getPath().addAll(this.getPath());

        Vector2 firstNewPathComponent = getPath().get(0);

        float startingAngle = getEntity().orient(firstNewPathComponent);
        getEntity().setDirection(startingAngle);
    }

    @Override
    public boolean keyDown(InputEvent event, int keyCode) {
        if (!isInUse()) {
            return false;
        }

        switch (keyCode) {
            case LEFT: {
                setInLeft(true);
                return true;
            }

            case RIGHT : {
                setInRight(true);
                return true;
            }

            case UP: {
                System.out.println("Up");
                setInUp(true);
                return true;
            }

            case DOWN: {
                setInDown(true);
                return true;
            }

            case ENTER: {
                copy();
                finish();

                return true;
            }

            default: return false;
        }
    }

    @Override
    public boolean keyUp(InputEvent event, int keyCode) {
        if (!isInUse())
            return false;

        switch (keyCode) {
            case LEFT: {
                setInLeft(false);
                return true;
            }

            case RIGHT: {
                setInRight(false);
                return true;
            }

            case UP: {
                System.out.println("Un Up");
                setInUp(false);
                return true;
            }

            case DOWN: {
                setInDown(false);
                return true;
            }

            default:
                return false;
        }
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Camera matchCamera = getEntity().getStage().getCamera();
        this.pathComponentToAdd = new Vector3(x, y, 0);
        matchCamera.unproject(pathComponentToAdd);

        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        pathComponentToAdd = null;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        Camera mapCamera = getEntity().getStage().getCamera();
        pathComponentToAdd = new Vector3(x, Gdx.graphics.getHeight() - y, 0);
        mapCamera.unproject(pathComponentToAdd);
    }

    public void update() {
        // Indicates whether the user is relying on drawing the path through touch (not null) or keys (null)
        if (pathComponentToAdd == null) {
            // Handles case where user is drawing path with keys. We initialize the pre-translation vector
            // to path end
            if (isInLeft() || isInRight() || isInUp() || isInDown())
                pathComponentToAdd = new Vector3(getPathEnd(), 0);

            // In this case, no path component has been set through the toucher and no keyboard modification
            // of the path is present, meaning that the user has attempted to make no modifications to the path.
            // No updates are required.
            else
                return;
        }

        // Handles key drawing of path, while touch drawing of path is unaffected by this
        if (isInLeft())
            pathComponentToAdd.x -= 1;
        if (isInRight())
            pathComponentToAdd.x += 1;
        if (isInUp())
            pathComponentToAdd.y += 1;
        if (isInDown())
            pathComponentToAdd.y -= 1;
        //

        // Checks whether the user attempted to draw the path out of bounds using the keys
        boolean isPathComponentXInBounds = 0 <= pathComponentToAdd.x && pathComponentToAdd.x < MatchScreen.WHOLE_WORLD_TILE_WIDTH;
        boolean isPathComponentYInBounds = 0 <= pathComponentToAdd.y && pathComponentToAdd.y < MatchScreen.WHOLE_WORLD_TILE_HEIGHT;

        // Always true for touch drawing, true for key drawing if translations derived from keys pressed do not
        // take path outside of match map bounds.
        if (isPathComponentXInBounds && isPathComponentYInBounds)
            // this means that the user did not extend the path out of bounds, hence we can add their intended
            // extra path component
            getPath().add(new Vector2(pathComponentToAdd.x, pathComponentToAdd.y));

        pathComponentToAdd = null;
    }

    private final Array<Vector2> path = new Array<>();

    private Vector2 getPathEnd() {
        if (getPath().size == 0)
            return new Vector2(getEntity().getSprite().getX(), getEntity().getSprite().getY());

        return getPath().get(getPath().size - 1);
    }

    private void showFullMap() {
        getEntity().getStage().getViewport().setWorldSize(MatchScreen.WHOLE_WORLD_TILE_WIDTH, MatchScreen.WHOLE_WORLD_TILE_HEIGHT);
        getEntity().getStage().getViewport().apply(true);
    }

    private void revertToPlayerMap() {
        getEntity().getStage().getViewport().setWorldSize(MatchScreen.VISIBLE_WORLD_TILE_WIDTH, MatchScreen.VISIBLE_WORLD_TILE_HEIGHT);
        getEntity().getStage().getViewport().apply(true);
    }

    public Array<Vector2> getPath() {
        return path;
    }

    public boolean isInUse() {
        return getCurrentlyShownEntity() == getEntity();
    }

    public static boolean areAnyInUse() {
        return getCurrentlyShownEntity() != null;
    }

    public boolean isInLeft() {
        return inLeft;
    }

    public void setInLeft(boolean inLeft) {
        this.inLeft = inLeft;
    }

    public boolean isInRight() {
        return inRight;
    }

    public void setInRight(boolean inRight) {
        this.inRight = inRight;
    }

    public boolean isInUp() {
        return inUp;
    }

    public void setInUp(boolean inUp) {
        this.inUp = inUp;
    }

    public boolean isInDown() {
        return inDown;
    }

    public void setInDown(boolean inDown) {
        this.inDown = inDown;
    }
}
