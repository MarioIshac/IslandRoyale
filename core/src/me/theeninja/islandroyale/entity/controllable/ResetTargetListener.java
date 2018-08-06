package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import me.theeninja.islandroyale.EntityListener;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;

public class ResetTargetListener<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends EntityListener<A, B> {
    ResetTargetListener(A entity) {
        super(entity);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        getEntity().setPathIndex(0);
        getEntity().getPath().clear();
        getEntity().getPathSelectionInputListener().getPath().clear();

        getEntity().setSpeed(0);

        return true;
    }
}
