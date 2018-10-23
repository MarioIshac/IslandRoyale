package me.theeninja.islandroyale.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.*;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

public class AIPlayer extends Player {
    private int level;
    private final Array<Vector2[]> previousChosenRoutes = new Array<>();
    private final Random random = new Random();

    private static final double BASE_LEARNING_RATE = 0.001;

    private static double getLearningRate(int level) {
        return level * BASE_LEARNING_RATE;
    }

    AIPlayer(int level, String playerName, IntMap<Entity<?, ?>> entityMap) {
        super(playerName);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void requestTransportationRoute(ControllableEntity<?, ?> entity, MatchMap matchMap) {
        int pathLength = getRandom().nextInt();

        Vector2[] transportationRoute = new Vector2[pathLength];

        for (int pathComponentIndex = 0; pathComponentIndex < transportationRoute.length; pathComponentIndex++) {
            float pathComponentX = getRandom().nextFloat();
            float pathComponentY = getRandom().nextFloat();

            Vector2 pathComponent = new Vector2(pathComponentX, pathComponentY);

            transportationRoute[pathComponentIndex] = pathComponent;
        }

        entity.getPath().addAll(transportationRoute);
        getPreviousChosenRoutes().add(transportationRoute);
    }

    @Override
    public void update(MatchMap matchMap) {
        updateEntityResponses(matchMap);
    }

    private final Array<InteractableEntity<?, ?>> entityResponses = new Array<>();

    private void updateEntityResponses(MatchMap matchMap) {
        Class<? extends EntityType<?, ?>> entityTypeClass = null;
        try {
            EntityTypeentityTypeClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public Random getRandom() {
        return random;
    }

    public Array<Vector2[]> getPreviousChosenRoutes() {
        return previousChosenRoutes;
    }

    public Array<InteractableEntity<?, ?>> getEntityResponses() {
        return entityResponses;
    }
}
