package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;

import static com.badlogic.gdx.Input.Keys.*;

public class PathSelectionInputListener<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends InputListener {
    private boolean inLeft;
    private boolean inRight;
    private boolean inUp;
    private boolean inDown;

    private Vector3 pathComponentToAdd;

    /**
     * Set to true upon new construction of this listener.
     * Set to false upon path confirmed of that listener.
     */
    public static ControllableEntity<?, ?> CURRENTLY_SHOWN_ENTITY = null;

    @Override
    public boolean keyDown(InputEvent event, int keyCode) {
        switch (keyCode) {
            case LEFT: {
                inLeft = true;
                return true;
            }
            case RIGHT : {
                inRight = true;
                return true;
            }
            case UP: {
                inUp = true;
                return true;
            }
            case DOWN: {
                inDown = true;
                return true;
            }
            case ENTER: {
                // No longer in process of selecting a path, so update respective property
                getEntity().setPathSelectionInputListener(null);

                revertToPlayerMap();

                CURRENTLY_SHOWN_ENTITY = null;

                System.out.println("Size of path " + getPath());

                getEntity().getPath().clear();
                getEntity().getPath().addAll(this.getPath());
                getEntity().setPathIndex(0);

                return true;
            }
            default: return false;
        }
    }

    @Override
    public boolean keyUp(InputEvent event, int keyCode) {
        switch (keyCode) {
            case LEFT: {
                inLeft = false;
                return true;
            }
            case RIGHT: {
                inRight = false;
                return true;
            }
            case UP: {
                inUp = false;
                return true;
            }
            case DOWN: {
                inDown = false;
                return true;
            }
            default:
                return false;
        }
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        Camera matchCamera = entity.getStage().getCamera();
        pathComponentToAdd = new Vector3(x, y, 0);
        matchCamera.unproject(pathComponentToAdd);

        return true;
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
        pathComponentToAdd = null;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer) {
        Camera matchCamera = entity.getStage().getCamera();
        pathComponentToAdd = new Vector3(x, Gdx.graphics.getHeight() - y, 0);
        matchCamera.unproject(pathComponentToAdd);
    }

    public void update() {
        // Indicates whether the user is relying on drawing the path through touch (not null) or keys (null)
        if (pathComponentToAdd == null) {
            // Handles case where user is drawing path with keys. We initialize the pre-translation vector
            // to path end
            if (inLeft || inRight || inUp || inDown)
                pathComponentToAdd = new Vector3(getPathEnd(), 0);

            // In this case, no path component has been set through the toucher and no keyboard modification
            // of the path is present, meaning that the user has attempted to make no modifications to the path.
            // No updates are required.
            else
                return;
        }

        // Handles key drawing of path, while touch drawing of path is unaffected by this
        if (inLeft)
            pathComponentToAdd.x -= 1;
        if (inRight)
            pathComponentToAdd.x += 1;
        if (inUp)
            pathComponentToAdd.y += 1;
        if (inDown)
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
    }

    private final A entity;

    private final Array<Vector2> path = new Array<>();

    private Vector2 getPathEnd() {
        if (getPath().size == 0)
            return new Vector2(getEntity().getSprite().getX(), getEntity().getSprite().getY());

        return getPath().get(getPath().size - 1);
    }

    public PathSelectionInputListener(A entity) {
        this.entity = entity;
        showFullMap();

        CURRENTLY_SHOWN_ENTITY = entity;

        //EntityType.setProperty(entity, ControllableEntityType.PATH_SELECTOR_LISTENER_TABLE, this);
    }

    private void showFullMap() {
        getEntity().getStage().getViewport().setWorldSize(MatchScreen.WHOLE_WORLD_TILE_WIDTH, MatchScreen.WHOLE_WORLD_TILE_HEIGHT);
        getEntity().getStage().getViewport().apply(true);
    }

    private void revertToPlayerMap() {
        getEntity().getStage().getViewport().setWorldSize(MatchScreen.VISIBLE_WORLD_TILE_WIDTH, MatchScreen.VISIBLE_WORLD_TILE_HEIGHT);
        getEntity().getStage().getViewport().apply(true);
    }

    public A getEntity() {
        return entity;
    }

    public Array<Vector2> getPath() {
        return path;
    }
}
