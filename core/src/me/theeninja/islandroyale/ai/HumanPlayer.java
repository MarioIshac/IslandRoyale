package me.theeninja.islandroyale.ai;

import me.theeninja.islandroyale.MatchMap;
import me.theeninja.islandroyale.entity.controllable.ControllableEntity;

public class HumanPlayer extends Player {
    public HumanPlayer(String playerName) {
        super(playerName);
    }

    @Override
    public void requestTransportationRoute(ControllableEntity<?, ?> entity, MatchMap matchMap) {
        entity.getPathSelectionInputListener().request();
    }
}
