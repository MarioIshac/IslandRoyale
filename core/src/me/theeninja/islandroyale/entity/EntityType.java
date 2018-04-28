package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import me.theeninja.islandroyale.*;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityType<T extends EntityType<T>> {
    public static final Map<Integer, EntityType<? extends EntityType<?>>> IDS = new HashMap<>();

    public static <T extends EntityType<T>> T getEntityType(int id) {
        return (T) IDS.get(id);
    }

    private static final String BUILDING_DIRECTORY = "building/";
    private static final String MOVING_DIRECTORY = "moving/";
    public static final String PROJECTILE_DIRECTORY = "projectile/";

    public static final String RESOURCE_DIRECTORY = BUILDING_DIRECTORY + "resource/";
    public static final String DEFENSE_DIRECTORY = BUILDING_DIRECTORY + "defense/";
    public static final String OFFENSE_DIRECTORY = BUILDING_DIRECTORY + "offense/";
    public static final String PERSON_DIRECTORY = MOVING_DIRECTORY + "person/";
    public static final String TRANSPORT_DIRECTORY = MOVING_DIRECTORY + "transport/";

    private int id;

    private String name;
    private float baseHealth;
    private int maxLevel;
    private Inventory inventoryCost;

    public static final String ENTITY_DIRECTORY = "entity/";

    private transient Texture texture;
    private String texturePath;

    public String getName() {
        return name;
    }

    public float getBaseHealth() {
        return baseHealth;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public boolean canCharge(Inventory inventory) {
        return inventory.has(getInventoryCost());
    }

    public void charge(Inventory inventory) {
        inventory.remove(getInventoryCost());
    }

    public abstract void initialize(Entity<T> entity);

    /**
     * Performed every call to {@link me.theeninja.islandroyale.gui.screens.MatchScreen#render(float)}.
     * Should perform any NON-VISUAL updates. This should be followed up by {@link #present(Entity, Batch, float, float)}
     */
    public abstract void check(Entity<T> entity, float delta, Player player, MatchMap matchMap);

    /**
     * Performed every call to {@link me.theeninja.islandroyale.gui.screens.MatchScreen#render(float)}.
     * Should perform any VISUAL updates. This should follow {@link #check(Entity, float, Player, MatchMap)}
     */
    public abstract void present(Entity<T> entity, Batch batch, float centerPixelX, float centerPixelY);

    public Texture getTexture() {
        return texture;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
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

    public static <T> T getProperty(Entity<? extends EntityType> entity, String label) {
        return (T) entity.getProperties().get(label);
    }

    public static <T> void setProperty(Entity<? extends EntityType> entity, String label, T value) {
        entity.getProperties().put(label, value);
    }

    public Inventory getInventoryCost() {
        return inventoryCost;
    }

    public int getID() {
        return id;
    }
}
