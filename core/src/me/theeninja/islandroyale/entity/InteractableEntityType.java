package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import me.theeninja.islandroyale.*;
import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

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
    public static final String TABLE_LABEL = "verticalGroup";

    private static final int FIRST_LEVEL = 1;

    public abstract void configureEditor(Entity<T> entity, Table table);

    @Override
    public void check(Entity<T> entity, float delta, Player player, MatchMap matchMap) {
        if (shouldRemove(entity))
            setProperty(entity, DESCRIPTOR_SHOWN_LABEL, false);
    }

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

        Table table = new Table();
        table.setDebug(true);

        configureEditor(entity, table);

        setProperty(entity, TABLE_LABEL, table);
    }

    @Override
    public void present(Entity<T> entity, Camera projectorCamera, Stage targetStage) {
        Table displayActor = getProperty(entity, TABLE_LABEL);

        boolean isDescriptorShown = getProperty(entity, DESCRIPTOR_SHOWN_LABEL);

        if (isDescriptorShown) {
            Vector3 coords = new Vector3(entity.getSprite().getX(), entity.getSprite().getY(), 0);
            projectorCamera.project(coords);

            //displayActor.pack();

            // Show this table directly under entity, not over it
            displayActor.setPosition(coords.x, coords.y - displayActor.getHeight());

            targetStage.addActor(displayActor);
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
