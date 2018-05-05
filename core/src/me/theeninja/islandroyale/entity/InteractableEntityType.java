package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import me.theeninja.islandroyale.*;

import java.util.HashMap;
import java.util.Map;

public abstract class InteractableEntityType<T extends InteractableEntityType<T>> extends EntityType<T> {
    private float baseHealth;
    private float baseRange;
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
    public static final String DESCRIPTOR_SHOWN_LABEL = "descriptorShown";
    private static final String VERTICAL_GROUP_LABEL = "verticalGroup";

    private static final int FIRST_LEVEL = 1;

    public abstract void configureEditor(Entity<T> entity, VerticalGroup verticalGroup);

    @Override
    public final boolean shouldRemove(Entity<T> entity) {
        float currentHealth = getProperty(entity, HEALTH_LABEL);
        return currentHealth <= 0;
    }

    @Override
    public void setUp(Entity<T> entity) {
        setProperty(entity, LEVEL_LABEL, FIRST_LEVEL);
        setProperty(entity, HEALTH_LABEL, applyHealthMultiplier(FIRST_LEVEL));
        setProperty(entity, DESCRIPTOR_SHOWN_LABEL, true);

        VerticalGroup verticalGroup = new VerticalGroup();

        configureEditor(entity, verticalGroup);

        setProperty(entity, VERTICAL_GROUP_LABEL, verticalGroup);
    }

    @Override
    public void present(Entity<T> entity, Stage stage) {
        Actor displayActor = getProperty(entity, VERTICAL_GROUP_LABEL);

        boolean isDescriptorShown = getProperty(entity, DESCRIPTOR_SHOWN_LABEL);

        if (isDescriptorShown) {
            Vector3 screenCoords = new Vector3(entity.getSprite().getX(), entity.getSprite().getY(), 0);

            stage.getCamera().project(screenCoords);

            displayActor.setPosition(screenCoords.x * 16, screenCoords.y * 16);

            stage.addActor(displayActor);
        }

        else
            displayActor.remove();
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

    public float getBaseRange() {
        return baseRange;
    }
}
