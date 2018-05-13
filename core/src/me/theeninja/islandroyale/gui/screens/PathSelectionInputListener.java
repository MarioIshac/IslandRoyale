package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;

import static com.badlogic.gdx.Input.Keys.*;

public class PathSelectionInputListener extends InputListener {
    private boolean inLeft;
    private boolean inRight;
    private boolean inUp;
    private boolean inDown;

    /**
     * Set to true upon new construction of this listener.
     * Set to false upon path confirmed of that listener.
     */
    public static Entity<? extends ControllableEntityType<?>> CURRENTLY_SHOWN_ENTITY = null;

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
                System.out.println("Path size " + getPath().size);
                EntityType.setProperty(getEntity(), ControllableEntityType.TARGET_COORDS_PATH_LABEL, getPath());

                // No longer in process of selecting a path, so update respective property
                EntityType.setProperty(getEntity(), ControllableEntityType.PATH_SELECTOR_LISTENER_TABLE, null);

                revertToPlayerMap();

                CURRENTLY_SHOWN_ENTITY = null;
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

    public void update() {
        if (inLeft)
            getPath().add(getPathEnd().cpy().add(LEFT_TRANSLATE));
        if (inRight)
            getPath().add(getPathEnd().cpy().add(RIGHT_TRANSLATE));
        if (inUp)
            getPath().add(getPathEnd().cpy().add(UP_TRANSLATE));
        if (inDown)
            getPath().add(getPathEnd().cpy().add(DOWN_TRANSLATE));
        if (getPathEnd().x < 0 || getPathEnd().y < 0)
            getPath().removeIndex(getPath().size - 1);
    }

    private final Entity<? extends ControllableEntityType<?>> entity;

    private static final Vector2 LEFT_TRANSLATE = new Vector2(-1, 0);
    private static final Vector2 RIGHT_TRANSLATE = new Vector2(1, 0);
    private static final Vector2 UP_TRANSLATE = new Vector2(0, 1);
    private static final Vector2 DOWN_TRANSLATE = new Vector2(0, -1);

    private final Array<Vector2> path = new Array<>();

    private Vector2 getPathEnd() {
        if (getPath().size == 0)
            return new Vector2(getEntity().getSprite().getX(), getEntity().getSprite().getY());

        return getPath().get(getPath().size - 1);
    }

    public PathSelectionInputListener(Entity<? extends ControllableEntityType<?>> entity) {
        this.entity = entity;
        showFullMap();

        CURRENTLY_SHOWN_ENTITY = entity;

        EntityType.setProperty(entity, ControllableEntityType.PATH_SELECTOR_LISTENER_TABLE, this);
    }

    private void showFullMap() {
        getEntity().getStage().getViewport().setWorldSize(MatchScreen.WHOLE_WORLD_TILE_WIDTH, MatchScreen.WHOLE_WORLD_TILE_HEIGHT);
        getEntity().getStage().getViewport().apply(true);
    }

    private void revertToPlayerMap() {

        getEntity().getStage().getViewport().setWorldSize(MatchScreen.VISIBLE_WORLD_TILE_WIDTH, MatchScreen.VISIBLE_WORLD_TILE_HEIGHT);
        getEntity().getStage().getViewport().apply(true);
    }

    public Entity<? extends ControllableEntityType<?>> getEntity() {
        return entity;
    }

    public Array<Vector2> getPath() {
        return path;
    }
}
