package me.theeninja.islandroyale.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.building.OffenseBuilding;
import me.theeninja.islandroyale.entity.building.OffenseBuildingType;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.gui.screens.Match;
import me.theeninja.islandroyale.gui.screens.MatchScreen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AIPlayer extends Player {
    private int level;
    private final Array<Vector2[]> previousChosenRoutes = new Array<>();

    public AIPlayer(final int level, final String playerName) {
        super(playerName);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void requestTransportationRoute(final ControllableEntity<?, ?> entity, final MatchMap matchMap) {
        final int pathLength = MathUtils.random(1000);

        final Vector2[] transportationRoute = new Vector2[pathLength];

        for (int pathComponentIndex = 0; pathComponentIndex < transportationRoute.length; pathComponentIndex++) {
            final float pathComponentX = getX();
            final float pathComponentY = getY();

            final Vector2 pathComponent = new Vector2(pathComponentX, pathComponentY);

            transportationRoute[pathComponentIndex] = pathComponent;
        }

        entity.getPath().addAll(transportationRoute);
        getPreviousChosenRoutes().add(transportationRoute);
    }

    private Class<? extends InteractableEntityType> getRandomEntityTypeClass(final Class<? extends InteractableEntityType>[] entityTypeClasses) {
        final int entityTypeClassIndex = MathUtils.random(entityTypeClasses.length);

        return entityTypeClasses[entityTypeClassIndex];
    }

    @Override
    public void update(final Match match) {
        super.update(match);

        MatchMap matchMap = match.getMatchMap();

        for (int entityTypeKey = 0; entityTypeKey < matchMap.getEntities().length; entityTypeKey++) {
            final Array<Entity<?, ?>> entities = matchMap.getEntities()[entityTypeKey];

            for (int entityIndex = 0; entityIndex < entities.size; entityIndex++) {
                final Entity<?, ?> entity = entities.get(entityIndex);

                final float xDistanceFromPlayer = entity.getX() - getX();
                final float yDistanceFromPlayer = entity.getY() - getY();

                if (
                    // Entity position is right
                    xDistanceFromPlayer <= MatchScreen.VISIBLE_WORLD_TILE_WIDTH  / 2 &&
                    yDistanceFromPlayer <= MatchScreen.VISIBLE_WORLD_TILE_HEIGHT / 2 &&

                    // Is entity we should consider
                    entity instanceof InteractableEntity
                ) {
                    final InteractableEntity<?, ?> interactableEntity = (InteractableEntity<?, ?>) entity;

                    final Class<? extends InteractableEntityType> entityTypeClass = interactableEntity.getEntityType().getClass();
                    final Respondable respondable = entityTypeClass.getAnnotation(Respondable.class);

                    final Class<? extends InteractableEntityType>[] responseTypes = respondable.value();
                    final Class<? extends InteractableEntityType> chosenResponseType = getRandomEntityTypeClass(responseTypes);

                    handleResponse(match, chosenResponseType);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends InteractableEntityType<?, ?>> T getInteractableEntityTypeInstance(final EntityTypeManager entityTypeManager, final int entityTypeKey) {
        final int entityTypeCount = entityTypeManager.getEntityTypeCount(entityTypeKey);
        final int randomEntityTypeIndex = MathUtils.random(entityTypeCount);

        return (T) entityTypeManager.getRandomInteractableEntityTypeInstance(entityTypeKey, randomEntityTypeIndex);
    }

    private static <T> Class<?>[] getEntityTypeConstructorArguments(Class<T> entityTypeClass) {
        return new Class<?>[] {
            entityTypeClass,
            Player.class,

            // Position coordinates
            float.class, // x-coordinate
            float.class, // y-coordinate

            Match.class
        };
    };

    /**
     * Add entity of desired entity type class. Intended to be used after acquisition of the what entity type
     * is appropiate for responding to another player's entity.
     *
     *
     * @param match
     * @param entityTypeClass
     */
    @SuppressWarnings("unchecked")
    public void handleResponse(final Match match, final Class<? extends InteractableEntityType> entityTypeClass) {
        try {
            final int entityTypeKey = EntityType.Unsafe.getEntityTypeKey(entityTypeClass);

            final Class<? extends Entity> entityClass = EntityType.Unsafe.ENTITY_CLASS_INDICES[entityTypeKey];
            final Class<? extends InteractableEntity> interactableEntityClass = (Class<? extends InteractableEntity>) entityClass;

            final Class<?>[] entityClassConstructorArguments = getEntityTypeConstructorArguments(interactableEntityClass);

            final Constructor<? extends InteractableEntity> entityResponseConstructor = interactableEntityClass.getConstructor(entityClassConstructorArguments);

            final InteractableEntityType<?, ?> interactableEntityType = getInteractableEntityTypeInstance(match.getEntityTypeManager(), entityTypeKey);

            final boolean canAfford = getInventory().has(interactableEntityType.getInventoryCost());
            final int x = 0;
            final int y = 0;

            // TODO give proper args
            final InteractableEntity<?, ?> producedEntity = entityResponseConstructor.newInstance(interactableEntityType, null, x, y, null);

            if (canAfford) {
                if (ControllableEntity.class.isAssignableFrom(entityClass)) {
                    final ControllableEntity<?, ?> producedControllableEntity = (ControllableEntity<?, ?>) producedEntity;

                    final Array<OffenseBuilding> potentialSpawners = match.getMatchMap().<OffenseBuilding, OffenseBuildingType>getEntitiesOfTypeUnsafely(entityTypeKey);

                    float minimumGapSquared = Float.MAX_VALUE;
                    OffenseBuilding<?, ?, ?, ?> bestSpawner = null;

                    for (final OffenseBuilding<?, ?, ?, ?> potentialSpawner : potentialSpawners) {
                        final float gapSquared = Entity.rangeBetweenSquared(potentialSpawner, x, y);

                        if (gapSquared < minimumGapSquared) {
                            bestSpawner = potentialSpawner;
                            minimumGapSquared = gapSquared;
                        }
                    }

                    if (bestSpawner == null) {
                        final Class<? extends ControllableEntity> controllableEntityClass = (Class<? extends ControllableEntity>) interactableEntityClass;
                        final Class<? extends OffenseBuilding> offenseBuildingEntityClass = EntityType.Unsafe.getEntityProducerClass(controllableEntityClass);

                        final int offenseBuildingEntityClassKey = EntityType.Unsafe.getEntityKey(offenseBuildingEntityClass);

                        final Class<?>[] offenseBuildingConstructorArguments = getEntityTypeConstructorArguments(offenseBuildingEntityClass);
                        final Constructor<? extends OffenseBuilding> offenseBuildingConstructor = offenseBuildingEntityClass.getConstructor(offenseBuildingConstructorArguments);

                        final OffenseBuildingType<?, ?, ?, ?> offenseBuildingType = getInteractableEntityTypeInstance(match.getEntityTypeManager(), offenseBuildingEntityClassKey);
                        final OffenseBuilding<?, ?, ?, ?> offenseBuilding = offenseBuildingConstructor.newInstance(offenseBuildingType, null, x, y, match);

                        offenseBuilding.queryUnsafely(producedControllableEntity);
                    }
                    else {
                        bestSpawner.queryUnsafely(producedControllableEntity);
                    }
                }
                else {
                    match.getMatchMap().addEntityUnsafely(producedEntity, entityTypeKey);
                }

                float xDifference = producedEntity.getX() - getX();
                float yDifference = producedEntity.getY() - getY();

                float angle = MathUtils.atan2(yDifference, xDifference);

                producedEntity.setDirection(angle);
            }
        }
        catch (final NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private Array<Vector2[]> getPreviousChosenRoutes() {
        return previousChosenRoutes;
    }
}
