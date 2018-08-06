package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import me.theeninja.islandroyale.EntityListener;

public class UserRemovalListener<A extends InteractableEntity<A, B>, B extends InteractableEntityType<A, B>> extends EntityListener<A, B> {
    public UserRemovalListener(A entity) {
        super(entity);
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        getEntity().setHasUserRemoved(true);

        return true;
    }
}
