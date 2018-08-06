package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;

import javax.naming.ldap.Control;
import javax.sound.midi.ControllerEventListener;

public class QueueButtonListener<C extends ControllableEntity<C, D>, D extends ControllableEntityType<C, D>> extends InputListener {
    private final D entityTypeProduced;

    QueueButtonListener(D entityTypeProduced) {
        this.entityTypeProduced = entityTypeProduced;
    }

    /**
     * Represents whether the user has already quieried an entity before letting the mouse up.
     * This is reset to false once the mouse has been released (or let up).
     */
    private boolean hasQueriedEntity = false;

    /**
     * Represents whether the user intends to query and entity with identify of {@code id}
     * during this frame.
     */
    private boolean shouldQueryEntity = false;

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        System.out.println("Attempt at query");
        setShouldQueryEntity(true);
        return true;
    }

    public boolean shouldQueryEntity() {
        return shouldQueryEntity;
    }

    public void setShouldQueryEntity(boolean shouldQueryEntity) {
        System.out.println(("Queried Entity set to " + shouldQueryEntity));
        this.shouldQueryEntity = shouldQueryEntity;
    }

    public boolean hasQueriedEntity() {
        return hasQueriedEntity;
    }

    public void setHasQueriedEntity(boolean hasQueriedEntity) {
        this.hasQueriedEntity = hasQueriedEntity;
    }

    public D getEntityTypeProduced() {
        return entityTypeProduced;
    }
}
