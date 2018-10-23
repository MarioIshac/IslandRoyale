package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;
import me.theeninja.islandroyale.MatchMap;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityType<A extends Entity<A, B>, B extends EntityType<A, B>> {
    private int maxLevel;

    private String texturePath;

    public abstract int getEntityTypeIndex();

    protected abstract int getBaseLevel(A entity);

    private transient Texture texture;

    public final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private int tileWidth;
    private int tileHeight;
    private static final String ENTITY_DIRECTORY = "entity/";

    private static final String INTERACTABLE_DIRECTORY = ENTITY_DIRECTORY + "interactable/";
    private static final String BULLET_PROJECTILE_DIRECTORY = ENTITY_DIRECTORY + "bullet/";
    private static final String TREASURE_DIRECTORY = ENTITY_DIRECTORY + "treasure/";

    private static final String BUILDING_DIRECTORY = INTERACTABLE_DIRECTORY + "building/";
    private static final String CONTROLLABLE_DIRECTORY = INTERACTABLE_DIRECTORY + "controllable/";

    public static final String RESOURCE_DIRECTORY = BUILDING_DIRECTORY + "resource/";
    public static final String DEFENSE_DIRECTORY = BUILDING_DIRECTORY + "defense/";
    public static final String OFFENSE_DIRECTORY = BUILDING_DIRECTORY + "offense/";
    public static final String HEADQUARTERS_DIRECTORY = BUILDING_DIRECTORY + "headquarters/";

    public static final String INTERACTABLE_PROJECTILE_DIRECTORY = CONTROLLABLE_DIRECTORY + "projectile/";
    public static final String PERSON_DIRECTORY = CONTROLLABLE_DIRECTORY + "person/";
    public static final String TRANSPORT_DIRECTORY = CONTROLLABLE_DIRECTORY + "transport/";

    public static final String TRANSPORT_GENERATOR_DIRECTORY = OFFENSE_DIRECTORY + "transport_generator/";
    public static final String PERSON_GENERATOR_DIRECTORY = OFFENSE_DIRECTORY + "person_generator/";
    public static final String PROJECTILE_GENERATOR_DIRECTORY = OFFENSE_DIRECTORY + "projectile_generator/";

    public static final String DEFENSE_BULLET_PROJECTILE_DIRECTORY = BULLET_PROJECTILE_DIRECTORY + "defense/";
    public static final String PERSON_BULLET_PROJECTILE_DIRECTORY = BULLET_PROJECTILE_DIRECTORY + "person/";

    public static final String RESOURCE_TREASURE_DIRECTORY = TREASURE_DIRECTORY + "resource/";
    public static final String DATA_TREASURE_DIRECTORY = TREASURE_DIRECTORY + "data/";

    public static final int DATA_TREASURE_TYPE = 0;
    public static final int RESOURCE_TREASURE_TYPE = 1;
    public static final int TRANSPORTER_TYPE = 2;
    public static final int PERSON_TYPE = 3;
    public static final int INTERACTABLE_PROJECTILE_ENTITY_TYPE = 4;
    public static final int DEFENSE_BULLET_PROJECTILE_TYPE = 5;
    public static final int PERSON_BULLET_PROJECTILE_TYPE = 6;
    public static final int TRANSPORTER_GENERATOR_TYPE = 7;
    public static final int RESOURCE_GENERATOR_TYPE = 8;
    public static final int PROJECTILE_GENERATOR_TYPE = 9;
    public static final int PERSON_GENERATOR_TYPE = 10;
    public static final int HEAD_QUARTERS_TYPE = 11;
    public static final int DEFENSE_BUILDING_ENTITY_TYPE = 12;

    public static final int NUMBER_OF_ENTITY_TYPES = 13;

    public static final int TREASURE_SEEKER_PRIORITY_MIN = 1;
    public static final int TREASURE_SEEKER_PRIORITY_MAX = 2;

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
}
