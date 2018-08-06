package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import me.theeninja.islandroyale.EntityListener;

public class UpgradeListener<A extends InteractableEntity<A, B>, B extends InteractableEntityType<A, B>> extends EntityListener<A, B> {
    public UpgradeListener(A entity) {
        super(entity);
    }

    private boolean isUpgradable;

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        if (isUpgradable()) {
            int oldLevel = getEntity().getLevel();
            getEntity().setLevel(oldLevel + 1);
        }

        return true;
    }

    public boolean isUpgradable() {
        return isUpgradable;
    }

    public void setUpgradable(boolean upgradable) {
        isUpgradable = upgradable;
    }
}
