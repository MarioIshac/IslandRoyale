package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityType<A extends Entity<A, B>, B extends EntityType<A, B>> {
    private int maxLevel;

    private String texturePath;

    public abstract int getDrawingPriority();

    protected void setUpEntity(A entity) {
        int baseLevel = getBaseLevel(entity);

        entity.setLevel(baseLevel);
    }

    protected abstract int getBaseLevel(A entity);

    private transient Texture texture;

    public final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private int tileWidth;
    private int tileHeight;

    public static final Map<Integer, EntityType<?, ?>> IDS = new HashMap<>();

    public static <Y extends Entity<Y, Z>, Z extends EntityType<Y, Z>> Z getEntityType(int id) {
        return (Z) IDS.get(id);
    }

    private static final String ENTITY_DIRECTORY = "entity/";

    private static final String INTERACTABLE_DIRECTORY = ENTITY_DIRECTORY + "interactable/";
    private static final String BULLET_PROJECTILE_DIRECTORY = ENTITY_DIRECTORY + "bullet/";

    private static final String BUILDING_DIRECTORY = INTERACTABLE_DIRECTORY + "building/";
    private static final String CONTROLLABLE_DIRECTORY = INTERACTABLE_DIRECTORY + "controllable/";

    public static final String INTERACTABLE_PROJECTILE_DIRECTORY = CONTROLLABLE_DIRECTORY + "projectile/";

    public static final String RESOURCE_DIRECTORY = BUILDING_DIRECTORY + "resource/";
    public static final String DEFENSE_DIRECTORY = BUILDING_DIRECTORY + "defense/";
    public static final String OFFENSE_DIRECTORY = BUILDING_DIRECTORY + "offense/";

    public static final String PERSON_DIRECTORY = CONTROLLABLE_DIRECTORY + "person/";
    public static final String TRANSPORT_DIRECTORY = CONTROLLABLE_DIRECTORY + "transport/";

    public static final String TRANSPORT_GENERATOR_DIRECTORY = OFFENSE_DIRECTORY + "transport_generator/";
    public static final String PERSON_GENERATOR_DIRECTORY = OFFENSE_DIRECTORY + "person_generator/";
    public static final String PROJECTILE_GENERATOR_DIRECTORY = OFFENSE_DIRECTORY + "projectile_generator/";

    public static final String DEFENSE_BULLET_PROJECTILE_DIRECTORY = BULLET_PROJECTILE_DIRECTORY + "defense/";
    public static final String PERSON_BULLET_PROJECTILE_DIRECTORY = BULLET_PROJECTILE_DIRECTORY + "person/";

    private int id;

    private String name;

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public String getTexturePath() {
        return texturePath;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setUpEntityType(B entityType) {
        // By default, nothing done
    }
}
