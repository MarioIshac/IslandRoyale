package me.theeninja.islandroyale.ai;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;

import java.util.function.Consumer;

public class AIPlayer extends Player {
    private int level;
    private final Array<Vector3[]> previousChosenRoutes = new Array<>();

    AIPlayer(int level, Island island) {
        super(island);
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public void requestTransportationRoute(ControllableEntity<?, ?> entity, MatchMap matchMap, Consumer<Vector3[]> postAcceptanceConsumer) {

    }

    public Array<Vector3[]> getPreviousChosenRoutes() {
        return previousChosenRoutes;
    }
}
