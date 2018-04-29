package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import me.theeninja.islandroyale.*;

import java.util.HashMap;
import java.util.Map;

public abstract class InteractableEntityType<T extends InteractableEntityType<T>> extends EntityType<T> {
    private float baseHealth;
    private int maxLevel;
    private Inventory inventoryCost;

    public float getBaseHealth() {
        return baseHealth;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public static final String HEALTH_LABEL = "health";
    protected static final String LEVEL_LABEL = "level";

    private static final int FIRST_LEVEL = 1;

    @Override
    public boolean shouldRemove(Entity<T> entity) {
        float currentHealth = getProperty(entity, HEALTH_LABEL);
        return currentHealth <= 0;
    }

    @Override
    public void initialize(Entity<T> entity) {
        setProperty(entity, LEVEL_LABEL, FIRST_LEVEL);
        setProperty(entity, HEALTH_LABEL, applyHealthMultiplier(FIRST_LEVEL));
    }

    public boolean canCharge(Inventory inventory) {
        return inventory.has(getInventoryCost());
    }

    public void charge(Inventory inventory) {
        inventory.remove(getInventoryCost());
    }
    public static float applyMultiplier(int level, float base, float multiplier) {
        while (level-- > 0)
            base *= multiplier;
        return base;
    }

    public static final float HEALTH_MULTIPLIER = 1.2f;

    public float applyHealthMultiplier(int level) {
        return applyMultiplier(level, getBaseHealth(), HEALTH_MULTIPLIER);
    }

    public Inventory getInventoryCost() {
        return inventoryCost;
    }
}
