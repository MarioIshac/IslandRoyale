package me.theeninja.islandroyale;

import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import me.theeninja.islandroyale.entity.InteractableEntity;
import me.theeninja.islandroyale.entity.InteractableEntityType;

public abstract class EntityListener<A extends InteractableEntity<A, B>, B extends InteractableEntityType<A, B>> extends InputListener {
    private final A entity;

    protected EntityListener(A entity) {
        this.entity = entity;
    }

    public A getEntity() {
        return entity;
    }
}
