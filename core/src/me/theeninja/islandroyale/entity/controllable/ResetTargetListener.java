package me.theeninja.islandroyale.entity.controllable;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.EntityType;

public class ResetTargetListener extends InputListener {
    private final Entity<? extends ControllableEntityType<?>> entity;

    ResetTargetListener(Entity<? extends ControllableEntityType<?>> entity) {
        this.entity = entity;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        EntityType.setProperty(getEntity(), ControllableEntityType.TARGET_COORDS_PATH_LABEL, null);
        return true;
    }

    public Entity<? extends ControllableEntityType<?>> getEntity() {
        return entity;
    }
}
