package me.theeninja.islandroyale.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import me.theeninja.islandroyale.entity.building.*;
import me.theeninja.islandroyale.entity.bullet.DefenseBulletProjectile;
import me.theeninja.islandroyale.entity.bullet.DefenseBulletProjectileType;
import me.theeninja.islandroyale.entity.bullet.PersonBulletProjectile;
import me.theeninja.islandroyale.entity.bullet.PersonBulletProjectileType;
import me.theeninja.islandroyale.entity.controllable.*;
import me.theeninja.islandroyale.entity.treasure.DataTreasure;
import me.theeninja.islandroyale.entity.treasure.DataTreasureType;
import me.theeninja.islandroyale.entity.treasure.ResourceTreasure;
import me.theeninja.islandroyale.entity.treasure.ResourceTreasureType;

public abstract class EntityType<A extends Entity<A, B>, B extends EntityType<A, B>> {
    private int maxLevel;

    private String texturePath;

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

    public static final int TREASURE_SEEKER_PRIORITY_MIN = 1;
    public static final int TREASURE_SEEKER_PRIORITY_MAX = 2;

    @SuppressWarnings("unchecked")
    public static final class Unsafe {
        public static final Class<? extends EntityType>[] ENTITY_TYPE_CLASS_INDICES = new Class[] {
            DataTreasureType.class,
            ResourceTreasureType.class,
            TransporterType.class,
            PersonType.class,
            InteractableProjectileEntityType.class,
            DefenseBulletProjectileType.class,
            PersonBulletProjectileType.class,
            TransporterGeneratorType.class,
            ResourceGeneratorType.class,
            ProjectileGeneratorType.class,
            PersonGeneratorType.class,
            HeadQuartersType.class,
            DefenseBuildingType.class
        };

        public static final Class<? extends Entity>[] ENTITY_CLASS_INDICES = new Class[] {
            DataTreasure.class,
            ResourceTreasure.class,
            Transporter.class,
            Person.class,
            InteractableProjectileEntity.class,
            DefenseBulletProjectile.class,
            PersonBulletProjectile.class,
            TransporterGenerator.class,
            ResourceGenerator.class,
            ProjectileGenerator.class,
            PersonGenerator.class,
            HeadQuarters.class,
            DefenseBuilding.class
        };

        private static final int MIN_ENTITY_PRODUCER_TYPE_INDEX = getReverseValue(ENTITY_TYPE_CLASS_INDICES, TransporterType.class);

        private static final Class<? extends OffenseBuilding>[] PRODUCED_ENTITY_PRODUCER_TYPES = new Class[] {
            TransporterGenerator.class,
            PersonGenerator.class,
            InteractableProjectileEntity.class
        };

        public static Class<? extends OffenseBuilding> getEntityProducerClass(final Class<? extends ControllableEntity> controllableEntityClass) {
            final int entityTypeKey = getReverseValue(ENTITY_CLASS_INDICES, controllableEntityClass);

            return PRODUCED_ENTITY_PRODUCER_TYPES[entityTypeKey - MIN_ENTITY_PRODUCER_TYPE_INDEX];
        }

        public static <B extends EntityType<?, ?>> int getEntityTypeKey(Class<? extends B> entityTypeClass) {
            return getReverseValue(ENTITY_TYPE_CLASS_INDICES, entityTypeClass);
        }

        public static <A extends Entity<?, ?>> int getEntityKey(Class<? extends A> entityClass) {
            return getReverseValue(ENTITY_CLASS_INDICES, entityClass);
        }

        private static <T> int getReverseValue(T[] classes, T value) {
            for (int possibleEntityTypeIndex = 0; possibleEntityTypeIndex < ENTITY_TYPE_CLASS_INDICES.length; possibleEntityTypeIndex++) {
                final T possibleValue = classes[possibleEntityTypeIndex];

                if (possibleValue == value) {
                    return possibleEntityTypeIndex;
                }
            }

            throw new IllegalStateException("SubClass exists without associated serialization type key in EntityType.Unsafe");
        }
    }

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
