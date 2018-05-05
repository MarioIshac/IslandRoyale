package me.theeninja.islandroyale.entity.building;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class QueueButtonListener extends InputListener {
    private final int id;

    QueueButtonListener(int id) {
        this.id = id;
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
        System.out.println("Should produce id " + getId());

        setShouldQueryEntity(true);
        return true;
    }

    public boolean shouldQueryEntity() {
        return shouldQueryEntity;
    }

    public void setShouldQueryEntity(boolean shouldQueryEntity) {
        this.shouldQueryEntity = shouldQueryEntity;
    }

    public int getId() {
        return id;
    }

    public boolean hasQueriedEntity() {
        return hasQueriedEntity;
    }

    public void setHasQueriedEntity(boolean hasQueriedEntity) {
        this.hasQueriedEntity = hasQueriedEntity;
    }
}
