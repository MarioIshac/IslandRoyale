package me.theeninja.islandroyale.gui.screens;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;
import me.theeninja.islandroyale.entity.InteractableEntity;
import me.theeninja.islandroyale.entity.controllable.Transporter;

public class MatchScreenInputListener implements InputProcessor {

    private final MatchScreen matchScreen;

    private static final int SHOW_MAP_KEY = Input.Keys.M;
    private boolean mapShown;

    MatchScreenInputListener(MatchScreen matchScreen) {
        this.matchScreen = matchScreen;
    }

    @Override
    public boolean keyDown( int keyCode) {
        switch (keyCode) {
            case SHOW_MAP_KEY: {

                setMapShown(true);
                return true;
            }

            default: return false;
        }
    }

    @Override
    public boolean keyUp(int keyCode) {
        switch (keyCode) {
            case SHOW_MAP_KEY: {

                setMapShown(false);
                return true;
            }

            default: return false;
        }
    }

    public MatchScreen getMatchScreen() {
        return matchScreen;
    }

    public boolean isMapShown() {
        return mapShown;
    }

    public void setMapShown(boolean mapShown) {
        this.mapShown = mapShown;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 checkEntityCoords = new Vector3(screenX, screenY, 0);
        Vector3 checkDescriptorCoords = checkEntityCoords.cpy();

        getMatchScreen().getMapCamera().unproject(checkEntityCoords);
        getMatchScreen().getHUDCamera().unproject(checkDescriptorCoords);

        // Represents whether any entity on the match map was touched
        boolean touchedEntity = false;

        // Iterate over higher priority entities first, in order to handle touch events first
        for (int entityTypeIndex = 0; entityTypeIndex < EntityType.Unsafe.ENTITY_TYPE_CLASS_INDICES.length; entityTypeIndex++) {
            Array<Entity<?, ?>> priorityEntities = getMatchScreen().getMatch().getMatchMap().getEntities()[entityTypeIndex];

            for (Entity<?, ?> entity : priorityEntities) {
                // Entity is not interactable, no need to handle attempted interaction
                if (!(entity instanceof InteractableEntity))
                    continue;

                InteractableEntity<?, ?> interactableEntity = (InteractableEntity<?, ?>) entity;

                // If entity is transporter and a person entity type has requested to board this transporter,
                // do not bring up descriptor and allow listeners further down to handle this event
                if (interactableEntity instanceof Transporter) {
                    Transporter transporter = (Transporter) entity;

                    if (transporter.getRequester() != null)
                        continue;
                }

                float entityMaxX = entity.getX() + entity.getWidth();
                float entityMaxY = entity.getY() + entity.getHeight();

                boolean touchInXBounds = entity.getX() <= checkEntityCoords.x && checkEntityCoords.x <= entityMaxX;
                boolean touchInYBounds = entity.getY() <= checkEntityCoords.y && checkEntityCoords.y <= entityMaxY;

                boolean touchInEntityBounds = touchInXBounds && touchInYBounds;

                boolean touchInDescriptorBounds = false;

                if (interactableEntity.isDescriptorShown()) {
                    Actor descriptor = interactableEntity.getDescriptor();

                    boolean xInBounds = descriptor.getX() < checkDescriptorCoords.x && checkDescriptorCoords.x < descriptor.getX() + descriptor.getWidth();
                    boolean yInBounds = descriptor.getY() < checkDescriptorCoords.y && checkDescriptorCoords.y < descriptor.getY() + descriptor.getHeight();

                    touchInDescriptorBounds = xInBounds && yInBounds;
                }

                // If we have already touched an entity, do not register a second touch upon an entity
                // that is below the entity that firstly handled the touch.
                // In other words, guarantee that only one entity is touched.
                boolean touchHandledByEntity = (touchInEntityBounds || touchInDescriptorBounds) && !touchedEntity;

                interactableEntity.setDescriptorShown(touchHandledByEntity);

                if (touchHandledByEntity)
                    touchedEntity = true;
            }
        }

        System.out.println("Any Entity Touched " + touchedEntity);

        return touchedEntity;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
