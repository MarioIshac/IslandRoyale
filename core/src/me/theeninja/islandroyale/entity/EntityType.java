package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.*;

import java.util.HashMap;
import java.util.Map;

public abstract class EntityType<T extends EntityType<T>> {
    private static final Map<Class<? extends EntityType>, String> TYPES_MAP = new HashMap<>();

    public static final String BUILDING_DIRECTORY = "building/";
    public static final String MOVING_DIRECTORY = "moving/";

    public static final String RESOURCE_DIRECTORY = BUILDING_DIRECTORY + "resource/";
    public static final String DEFENSE_DIRECTORY = BUILDING_DIRECTORY + "defense/";
    public static final String OFFENSE_DIRECTORY = BUILDING_DIRECTORY + "offense/";
    public static final String PERSON_DIRECTORY = MOVING_DIRECTORY + "person/";
    public static final String TRANSPORT_DIRECTORY = MOVING_DIRECTORY + "transport/";

    static {
        TYPES_MAP.put(ResourceBuildingType.class, RESOURCE_DIRECTORY);
        TYPES_MAP.put(DefenseBuildingType.class, DEFENSE_DIRECTORY);
        TYPES_MAP.put(OffenseBuildingType.class, OFFENSE_DIRECTORY);
        TYPES_MAP.put(PersonEntityType.class, PERSON_DIRECTORY);
        TYPES_MAP.put(TransportEntityType.class, TRANSPORT_DIRECTORY);
    }

    public static <T extends EntityType> Array<T> loadEntityTypes(Class<T> targetClass) {
        Array<T> types = new Array<>();

        TYPES_MAP.forEach((subClass, directory) -> {
            if (targetClass != subClass)
                return;

            String expandedDirectory = EntityType.ENTITY_DIRECTORY + directory;
            FileHandle typesDescriptorFileHandle = Gdx.files.internal(expandedDirectory + "types.txt");
            String text = typesDescriptorFileHandle.readString();

            if (text.isEmpty())
                return;

            String[] typeFileNames = text.split("\\r?\\n");

            for (String typeFileName : typeFileNames) {
                FileHandle typeFileHandle = Gdx.files.internal(expandedDirectory + typeFileName);
                T type = IslandRoyaleGame.JSON.fromJson(targetClass, typeFileHandle);

                String expandedTexturePath = expandedDirectory + type.getTexturePath();
                FileHandle textureFileHandle = Gdx.files.internal(expandedTexturePath);
                Texture texture = new Texture(textureFileHandle);

                type.setTexture(texture);

                types.add(type);
            }
        });

        return types;
    }

    private String name;
    private float baseHealth;
    private int maxLevel;
    private Inventory inventoryCost = new Inventory();
    //private final transient Map<Resource, Float> resourceCostsConverted = new HashMap<>();

    EntityType() {
        //for (Map.Entry<String, Float> entry : inventoryCost.entrySet())
        //    resourceCostsConverted.put(Resource.valueOf(entry.getKey()), entry.getValue());
    }

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
     * Should perform any NON-VISUAL updates. This should be followed up by {@link #present(Entity, Batch, int, int)}
     */
    public abstract void check(Entity<T> entity, float delta, Player player, MatchMap matchMap);

    /**
     * Performed every call to {@link me.theeninja.islandroyale.gui.screens.MatchScreen#render(float)}.
     * Should perform any VISUAL updates. This should follow {@link #check(Entity, float, Player, MatchMap)}
     */
    public abstract void present(Entity<T> entity, Batch batch, int centerPixelX, int centerPixelY);

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
}
