package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import me.theeninja.islandroyale.*;

public abstract class InteractableEntityType<A extends InteractableEntity<A, B>, B extends InteractableEntityType<A, B>> extends EntityType<A, B> {
    private static final int STARTER_LEVEL = 1;

    public abstract void configureEditor(A entity, Table table);

    private float baseHealth;

    private Inventory inventoryCost;

    public float getBaseHealth() {
        return baseHealth;
    }

    @Override
    protected void setUpEntity(A entity) {
        super.setUpEntity(entity);

        entity.setHealth(getBaseHealth());
    }

    @Override
    protected int getBaseLevel(A entity) {
        return STARTER_LEVEL;
    }
}
