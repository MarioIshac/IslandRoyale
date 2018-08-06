package me.theeninja.islandroyale.ai;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;

import java.util.Random;
import java.util.function.Consumer;

public class AIPlayer extends Player {
    private int level;
    private final Array<Vector2[]> previousChosenRoutes = new Array<>();
    private final Random random = new Random();

    AIPlayer(int level, Island island, int initialIslandReach) {
        super(island, initialIslandReach);
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

    public Array<Vector2[]> getPreviousChosenRoutes() {
        return previousChosenRoutes;
    }

    public Random getRandom() {
        return random;
    }
}
