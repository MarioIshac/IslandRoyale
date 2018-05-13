package me.theeninja.islandroyale.ai;

import com.badlogic.gdx.math.Vector3;
import me.theeninja.islandroyale.Island;
import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.Entity;
import me.theeninja.islandroyale.entity.controllable.ControllableEntityType;

import java.util.function.Consumer;

public class HumanPlayer extends Player {
    public HumanPlayer(Island mainIsland) {
        super(mainIsland);
    }

    @Override
    public void requestTransportationRoute(Entity<? extends ControllableEntityType<?>> entity, MatchMap matchMap, Consumer<Vector3[]> postAcceptanceConsumer) {

    }
}
