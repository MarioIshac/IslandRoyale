package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.ai.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class EntityType<T extends EntityType<T>> {
    private transient Texture texture;
    private String texturePath;

    public final static ShapeRenderer SHAPE_RENDERER_PRESENTER = new ShapeRenderer();

    public abstract int getDrawingPriority();

    private int tileWidth;
    private int tileHeight;

    public static final Map<Integer, EntityType<? extends EntityType<?>>> IDS = new HashMap<>();

    public static <MT extends EntityType<MT>> MT getEntityType(int id) {
        return (MT) IDS.get(id);
    }

    public static final String ENTITY_DIRECTORY = "entity/";

    public static final String INTERACTABLE_DIRECTORY = ENTITY_DIRECTORY + "interactable/";

    private static final String BUILDING_DIRECTORY = INTERACTABLE_DIRECTORY + "building/";
    private static final String MOVING_DIRECTORY = INTERACTABLE_DIRECTORY + "moving/";
    public static final String INTERACTABLE_PROJECTILE_DIRECTORY = MOVING_DIRECTORY + "projectile/";
    public static final String STATIC_PROJECTILE_DIRECTORY = ENTITY_DIRECTORY + "projectile/";

    public static final String RESOURCE_DIRECTORY = BUILDING_DIRECTORY + "resource/";
    public static final String DEFENSE_DIRECTORY = BUILDING_DIRECTORY + "defense/";
    public static final String OFFENSE_DIRECTORY = BUILDING_DIRECTORY + "offense/";
    public static final String PERSON_DIRECTORY = MOVING_DIRECTORY + "person/";
    public static final String TRANSPORT_DIRECTORY = MOVING_DIRECTORY + "transport/";

    public static final String TRANSPORT_GENERATOR_DIRECTORY = OFFENSE_DIRECTORY + "transport_generator/";
    public static final String PERSON_GENERATOR_DIRECTORY = OFFENSE_DIRECTORY + "person_generator/";


    private int id;

    private String name;

    public void initialize() {

    }

    public abstract void setUp(Entity<T> entity);

    public abstract boolean shouldRemove(Entity<T> entity);

    /**
     * Performed every call to {@link me.theeninja.islandroyale.gui.screens.MatchScreen#render(float)}.
     * Should perform any NON-VISUAL updates. This should be followed up by {@link #present(Entity, Stage)}
     */
    public abstract void check(Entity<T> entity, float delta, Player player, MatchMap matchMap);

    /**
     * Performed every call to {@link me.theeninja.islandroyale.gui.screens.MatchScreen#render(float)}.
     * Should perform any VISUAL updates. This should follow {@link #check(Entity, float, Player, MatchMap)}
     */
    public abstract void present(Entity<T> entity, Camera projector, Stage stage);

    public static <T> T getProperty(Entity<? extends EntityType<?>> entity, String label) {
        return (T) entity.getProperties().get(label);
    }

    public static <T> void setProperty(Entity<? extends EntityType<?>> entity, String label, T value) {
        entity.getProperties().put(label, value);
    }

    public static <T> void changeProperty(Entity<? extends EntityType<?>> entity, String label, Function<T, T> changer) {
        T propertyValue = getProperty(entity, label);
        propertyValue = changer.apply(propertyValue);
        setProperty(entity, label, propertyValue);
    }

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
