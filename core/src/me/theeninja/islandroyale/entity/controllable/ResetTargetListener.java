package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;

public class ResetTargetListener<A extends ControllableEntity<A, B>, B extends ControllableEntityType<A, B>> extends InputListener {
    private final A entity;

    ResetTargetListener(A entity) {
        this.entity = entity;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        getEntity().getPath().clear();
        return true;
    }

    public A getEntity() {
        return entity;
    }
}
