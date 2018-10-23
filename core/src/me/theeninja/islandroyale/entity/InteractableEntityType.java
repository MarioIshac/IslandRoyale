package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.OrderedSet;
import me.theeninja.islandroyale.*;
import me.theeninja.islandroyale.gui.screens.Match;

public abstract class InteractableEntityType<A extends InteractableEntity<A, B>, B extends InteractableEntityType<A, B>> extends EntityType<A, B> {
    private static final int STARTER_LEVEL = 1;

    public abstract void configureEditor(A entity, Match match);

    private float baseHealth;
    private EntityResponseIntent entityResponseIntent;
    private Inventory inventoryCost;
    private IntArray entityTypeResponses;

    public float getBaseHealth() {
        return baseHealth;
    }

    @Override
    protected int getBaseLevel(A entity) {
        return STARTER_LEVEL;
    }

    public Inventory getInventoryCost() {
        return inventoryCost;
    }

    public EntityResponseIntent getEntityResponseIntent() {
        return entityResponseIntent;
    }

    public IntArray getEntityTypeResponses() {
        return entityTypeResponses;
    }
}
